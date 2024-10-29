package com.mule.mulechain.internal.operations;

import static org.apache.commons.io.IOUtils.toInputStream;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;
import static org.apache.commons.io.IOUtils.toInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.mule.mulechain.internal.JtokkitConfiguration;
import com.mule.mulechain.internal.models.ModerationParamsModelDetails;
import org.json.JSONException;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.mule.runtime.extension.api.annotation.Alias;


/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class JtokkitOperations {

  private static final Logger LOGGER = LoggerFactory.getLogger(JtokkitOperations.class);
  EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
  private static final String URL_BASE = "https://api.openai.com/v1/moderations";


  /**
   * This operation estimates the token for a given prompt.
   */
  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("Estimate-tokens")
  public InputStream estimateToken(@Config JtokkitConfiguration configuration, String prompt) {

    try {

      Encoding encoding = registry.getEncoding(EncodingType.valueOf(configuration.getEncodingType().toUpperCase()));
      int tokenCount = encoding.countTokens(prompt);

      JSONObject jsonObject = new JSONObject();
      jsonObject.put("tokenEstimation", tokenCount);
      LOGGER.debug(jsonObject.toString());

      return org.apache.commons.io.IOUtils.toInputStream(jsonObject.toString(), StandardCharsets.UTF_8);
    } catch (IllegalArgumentException e) {
      // This will catch errors related to enum value parsing
      LOGGER.error("Invalid encoding type: " + configuration.getEncodingType());
      throw e; // Rethrow to maintain standard error handling
    } catch (JSONException e) {
      // This will catch JSON-related exceptions
      LOGGER.error("Error while creating JSON object.");
      throw e; // Rethrow to maintain standard error handling
    } catch (Exception e) {
      // Catching any other unforeseen exceptions
      LOGGER.error("An unexpected error occurred: " + e.getMessage());
      throw e; // Rethrow to maintain standard error handling
    }

  }


  /**
   * Use OpenAI Moderation models to moderate the input (any, from user or llm)
   */
  @MediaType(value = APPLICATION_JSON, strict = false)
  @Alias("Moderate-content")
  public InputStream moderateInput(@Config JtokkitConfiguration configuration, String input, @ParameterGroup(name= "Additional properties") ModerationParamsModelDetails modelDetails) {

    JSONObject payload = new JSONObject();
    payload.put("model", modelDetails.getModelName());
    payload.put("input", input);

    String response = executeREST(configuration.getApiKey(), payload.toString());
    LOGGER.debug(response);
    return org.apache.commons.io.IOUtils.toInputStream(response, StandardCharsets.UTF_8);
  }

    private static HttpURLConnection getConnectionObject(URL url, String apiKey) throws IOException {
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setDoOutput(true);
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Authorization", "Bearer " + apiKey);
    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
    return conn;
  }

  private static String executeREST(String apiKey, String payload) {

    try {
      URL url = new URL(URL_BASE);
      HttpURLConnection conn = getConnectionObject(url, apiKey);

      try (OutputStream os = conn.getOutputStream()) {
        byte[] input = payload.getBytes(StandardCharsets.UTF_8);
        os.write(input, 0, input.length);
      }

      int responseCode = conn.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
          StringBuilder response = new StringBuilder();
          String responseLine;
          while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
          }
          return response.toString();
        }
      } else {
        return "Error: " + responseCode;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return "Exception occurred: " + e.getMessage();
    }

  }



}
