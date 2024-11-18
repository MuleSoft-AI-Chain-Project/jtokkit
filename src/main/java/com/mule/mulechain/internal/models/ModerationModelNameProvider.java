package com.mule.mulechain.internal.models;

import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;
import org.mule.runtime.extension.api.values.ValueResolvingException;

import java.util.Set;

public class ModerationModelNameProvider implements ValueProvider {

    private static final Set<Value> VALUES_FOR = ValueBuilder.getValuesFor(
            "omni-moderation-latest",
            "text-moderation-latest"
    );

    @Override
    public Set<Value> resolve() throws ValueResolvingException {

        return VALUES_FOR;
    }

}
