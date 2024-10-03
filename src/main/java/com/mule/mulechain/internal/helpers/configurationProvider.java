package com.mule.mulechain.internal.helpers;
import java.util.Set;

import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

public class configurationProvider  implements ValueProvider {

    @Override
    public Set<Value> resolve() throws ValueResolvingException {
        return ValueBuilder.getValuesFor("r50k_base", "p50k_base", "p50k_edit", "cl100k_base", "o200k_base");
    }

}
