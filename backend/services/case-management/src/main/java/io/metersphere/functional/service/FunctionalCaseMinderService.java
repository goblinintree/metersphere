package io.metersphere.functional.service;

import io.metersphere.functional.constants.MinderLabel;
import io.metersphere.functional.dto.FunctionalCaseMindDTO;
import io.metersphere.functional.dto.FunctionalMinderTreeDTO;
import io.metersphere.functional.dto.FunctionalMinderTreeNodeDTO;
import io.metersphere.functional.mapper.ExtFunctionalCaseMapper;
import io.metersphere.functional.request.FunctionalCaseMindRequest;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能用例脑图
 *
 * @date : 2023-5-17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FunctionalCaseMinderService {

    @Resource
    private ExtFunctionalCaseMapper extFunctionalCaseMapper;


    /**
     * 功能用例-脑图用例列表查询
     *
     * @param deleted 用例是否删除
     * @return FunctionalMinderTreeDTO
     */
    public List<FunctionalMinderTreeDTO> getMindFunctionalCase(FunctionalCaseMindRequest request, boolean deleted) {
        List<FunctionalMinderTreeDTO> list = new ArrayList<>();
        //查出当前模块下的所有用例
        List<FunctionalCaseMindDTO> functionalCaseMindDTOList = extFunctionalCaseMapper.getMinderCaseList(request, deleted);
        //构造父子级数据
        for (FunctionalCaseMindDTO functionalCaseMindDTO : functionalCaseMindDTOList) {
            FunctionalMinderTreeDTO root = new FunctionalMinderTreeDTO();
            FunctionalMinderTreeNodeDTO rootData = new FunctionalMinderTreeNodeDTO();
            rootData.setId(functionalCaseMindDTO.getId());
            rootData.setText(functionalCaseMindDTO.getName());
            rootData.setPriority(functionalCaseMindDTO.getPriority());
            rootData.setStatus(functionalCaseMindDTO.getReviewStatus());
            rootData.setResource(List.of(MinderLabel.CASE.toString(), functionalCaseMindDTO.getPriority()));
            List<FunctionalMinderTreeDTO> children = buildChildren(functionalCaseMindDTO);
            root.setChildren(children);
            root.setData(rootData);
            list.add(root);
        }
        return list;
    }

    private List<FunctionalMinderTreeDTO> buildChildren(FunctionalCaseMindDTO functionalCaseMindDTO) {
        List<FunctionalMinderTreeDTO> children = new ArrayList<>();
        if (functionalCaseMindDTO.getSteps() != null) {
            String stepText = new String(functionalCaseMindDTO.getSteps(), StandardCharsets.UTF_8);
            FunctionalMinderTreeDTO stepFunctionalMinderTreeDTO = getFunctionalMinderTreeDTO(stepText, MinderLabel.TEXT_DESCRIPTION.toString());
            children.add(stepFunctionalMinderTreeDTO);
        }
        if (functionalCaseMindDTO.getExpectedResult() != null) {
            String expectedResultText = new String(functionalCaseMindDTO.getExpectedResult(), StandardCharsets.UTF_8);
            FunctionalMinderTreeDTO expectedResultFunctionalMinderTreeDTO = getFunctionalMinderTreeDTO(expectedResultText, MinderLabel.EXPECTED_RESULT.toString());
            children.add(expectedResultFunctionalMinderTreeDTO);
        }
        if (functionalCaseMindDTO.getPrerequisite() != null) {
            String prerequisiteText = new String(functionalCaseMindDTO.getPrerequisite(), StandardCharsets.UTF_8);
            FunctionalMinderTreeDTO prerequisiteFunctionalMinderTreeDTO = getFunctionalMinderTreeDTO(prerequisiteText, MinderLabel.PREREQUISITE.toString());
            children.add(prerequisiteFunctionalMinderTreeDTO);
        }
        if (functionalCaseMindDTO.getDescription() != null) {
            String descriptionText = new String(functionalCaseMindDTO.getDescription(), StandardCharsets.UTF_8);
            FunctionalMinderTreeDTO descriptionFunctionalMinderTreeDTO = getFunctionalMinderTreeDTO(descriptionText, MinderLabel.DESCRIPTION.toString());
            children.add(descriptionFunctionalMinderTreeDTO);
        }
        return children;
    }

    @NotNull
    private static FunctionalMinderTreeDTO getFunctionalMinderTreeDTO(String text, String resource) {
        FunctionalMinderTreeDTO functionalMinderTreeDTO = new FunctionalMinderTreeDTO();
        FunctionalMinderTreeNodeDTO rootData = new FunctionalMinderTreeNodeDTO();
        rootData.setText(text);
        rootData.setResource(List.of(resource));
        functionalMinderTreeDTO.setChildren(new ArrayList<>());
        functionalMinderTreeDTO.setData(rootData);
        return functionalMinderTreeDTO;
    }

}
