package com.uestcpg.remotedoctor.RCProvider;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * Created by dmsoft on 2017/6/30.
 */

public class InputOrderModule extends DefaultExtensionModule{

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules =  super.getPluginModules(conversationType);
        pluginModules.add(new InputOrderPlugin());
        return pluginModules;
    }
}
