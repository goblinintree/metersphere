package io.metersphere.plugin.api.api;

import io.metersphere.plugin.sdk.api.AbstractMsPlugin;

public abstract class AbstractApiPlugin  extends AbstractMsPlugin {
    private static final String API_PLUGIN_TYPE = "API";
    @Override
    public String getType() {
        return API_PLUGIN_TYPE;
    }
}
