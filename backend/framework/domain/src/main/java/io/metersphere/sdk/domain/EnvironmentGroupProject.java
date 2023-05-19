package io.metersphere.sdk.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.metersphere.validation.groups.Updated;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "环境组配置")
@TableName("environment_group_project")
@Data
public class EnvironmentGroupProject implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    @NotBlank(message = "{environment_group_project.id.not_blank}", groups = {Updated.class})
    @ApiModelProperty(name = "", required = true, allowableValues = "range[1, 50]")
    private String id;


    @ApiModelProperty(name = "环境组id", required = false, allowableValues = "range[1, 50]")
    private String environmentGroupId;


    @ApiModelProperty(name = "api test environment 环境ID", required = false, allowableValues = "range[1, 50]")
    private String environmentId;


    @ApiModelProperty(name = "项目id", required = false, allowableValues = "range[1, 50]")
    private String projectId;


}