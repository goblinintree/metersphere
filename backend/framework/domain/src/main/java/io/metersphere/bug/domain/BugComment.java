package io.metersphere.bug.domain;

import io.metersphere.validation.groups.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import lombok.Data;

@Data
public class BugComment implements Serializable {
    @Schema(title = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{bug_comment.id.not_blank}", groups = {Updated.class})
    @Size(min = 1, max = 64, message = "{bug_comment.id.length_range}", groups = {Created.class, Updated.class})
    private String id;

    @Schema(title = "缺陷ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{bug_comment.bug_id.not_blank}", groups = {Created.class})
    @Size(min = 1, max = 64, message = "{bug_comment.bug_id.length_range}", groups = {Created.class, Updated.class})
    private String bugId;

    @Schema(title = "评论人")
    private String createUser;

    @Schema(title = "创建时间")
    private Long createTime;

    @Schema(title = "更新时间")
    private Long updateTime;

    @Schema(title = "内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{bug_comment.description.not_blank}", groups = {Created.class})
    @Size(min = 1, max = 65535, message = "{bug_comment.description.length_range}", groups = {Created.class, Updated.class})
    private String description;

    private static final long serialVersionUID = 1L;
}