package com.mule.mulechain.internal;

import com.mule.mulechain.internal.helpers.configurationProvider;
import com.mule.mulechain.internal.operations.JtokkitOperations;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.values.OfValues;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Operations(JtokkitOperations.class)
public class JtokkitConfiguration {

  @Parameter
  @OfValues(configurationProvider.class)
  private String encodingType;

  public String getEncodingType(){
    return encodingType;
  }
}
