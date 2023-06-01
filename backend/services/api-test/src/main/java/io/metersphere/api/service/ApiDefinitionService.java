package io.metersphere.api.service;

import io.metersphere.api.domain.ApiDefinition;
import io.metersphere.api.domain.ApiDefinitionExample;
import io.metersphere.api.dto.ApiDefinitionDTO;
import io.metersphere.api.dto.ApiDefinitionListRequest;
import io.metersphere.api.mapper.ApiDefinitionMapper;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ApiDefinitionService {

    @Resource
    private ApiDefinitionMapper apiDefinitionMapper;

    public ApiDefinitionDTO create(ApiDefinitionDTO request, List<MultipartFile> bodyFiles) {
        return request;
    }


    public List<ApiDefinition> list(@NotNull ApiDefinitionListRequest request) {
        ApiDefinitionExample example = new ApiDefinitionExample();
        ApiDefinitionExample.Criteria criteria = example.createCriteria();
        criteria.andProjectIdEqualTo(request.getProjectId());
        if (StringUtils.isNotBlank(request.getName())) {
            criteria.andNameLike("%" + request.getName() + "%");
        }

        if (StringUtils.isNotEmpty(request.getPath())) {
            criteria.andPathEqualTo(request.getPath());
        }

        if (StringUtils.isNotEmpty(request.getMethod())) {
            criteria.andMethodEqualTo(request.getMethod());
        }
        if (StringUtils.isNotBlank(request.getProtocol())) {
            criteria.andProtocolEqualTo(request.getProtocol());
        }


        return apiDefinitionMapper.selectByExample(example);
    }
}
