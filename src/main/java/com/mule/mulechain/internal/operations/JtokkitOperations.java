package com.mule.mulechain.internal.operations;

import static org.apache.commons.io.IOUtils.toInputStream;
import static org.mule.runtime.extension.api.annotation.param.MediaType.APPLICATION_JSON;
import static org.apache.commons.io.IOUtils.toInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.mule.mulechain.internal.JtokkitConfiguration;
import org.json.JSONException;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Config;
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

}
