package com.mule.mulechain.internal;

import com.mule.mulechain.internal.helpers.configurationProvider;
import com.mule.mulechain.internal.operations.JtokkitOperations;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import org.mule.sdk.api.annotation.param.Optional;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Operations(JtokkitOperations.class)
public class JtokkitConfiguration {

  @Parameter
  @OfValues(configurationProvider.class)
  @Optional(defaultValue = "cl100k_base")
  private String encodingType;

  public String getEncodingType(){
    return encodingType;
  }

  @Parameter
  @Optional
  private String apiKey;

  public String getApiKey(){
    return apiKey;
  }
}
