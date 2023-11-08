package io.metersphere.system.service;

import io.metersphere.plugin.api.spi.AbstractProtocolPlugin;
import io.metersphere.plugin.api.spi.MsTestElement;
import io.metersphere.sdk.constants.PluginScenarioType;
import io.metersphere.sdk.dto.api.request.http.MsHTTPElement;
import io.metersphere.sdk.util.LogUtils;
import io.metersphere.system.domain.Plugin;
import io.metersphere.system.dto.ProtocolDTO;
import jakarta.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.pf4j.PluginWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ApiPluginService {

    @Resource
    private PluginLoadService pluginLoadService;
    @Resource
    private BasePluginService basePluginService;
    private static final String HTTP_PROTOCOL = "HTTP";

    /**
     * 获取协议插件的的协议列表
     * @param orgId
     * @return
     */
    public List<ProtocolDTO> getProtocols(String orgId) {
        // 查询组织下有权限的插件
        Set<String> pluginIds = basePluginService.getOrgEnabledPlugins(orgId, PluginScenarioType.API_PROTOCOL)
                .stream()
                .map(Plugin::getId)
                .collect(Collectors.toSet());

        // 过滤协议插件
        List<PluginWrapper> plugins = pluginLoadService.getMsPluginManager().getPlugins();
        List<PluginWrapper> pluginWrappers = plugins.stream()
                .filter(plugin -> pluginIds.contains(plugin.getPluginId()) && plugin.getPlugin() instanceof AbstractProtocolPlugin).toList();

        List protocols = new ArrayList<ProtocolDTO>();
        pluginWrappers.forEach(pluginWrapper -> {
            try {
                // 获取插件中 MsTestElement 的实现类
                List<Class<? extends MsTestElement>> extensionClasses = pluginWrapper.getPluginManager()
                        .getExtensionClasses(MsTestElement.class, pluginWrapper.getPluginId());

                AbstractProtocolPlugin protocolPlugin = ((AbstractProtocolPlugin) pluginWrapper.getPlugin());
                ProtocolDTO protocolDTO = new ProtocolDTO();
                protocolDTO.setProtocol(protocolPlugin.getProtocol());
                if (CollectionUtils.isNotEmpty(extensionClasses)) {
                    protocolDTO.setPolymorphicName(extensionClasses.get(0).getSimpleName());
                }
                if (StringUtils.isNoneBlank(protocolDTO.getProtocol(), protocolDTO.getPolymorphicName())) {
                    protocols.add(protocolDTO);
                }
            } catch (Exception e) {
                LogUtils.error(e);
            }
        });
        // 将 http 协议放最前面
        ProtocolDTO protocolDTO = new ProtocolDTO();
        protocolDTO.setProtocol(HTTP_PROTOCOL);
        protocolDTO.setPolymorphicName(MsHTTPElement.class.getSimpleName());
        protocols.addFirst(protocolDTO);
        return protocols;
    }
}
