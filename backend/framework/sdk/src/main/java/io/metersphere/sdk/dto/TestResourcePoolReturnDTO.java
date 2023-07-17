package io.metersphere.sdk.dto;

import io.metersphere.system.domain.TestResourcePool;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TestResourcePoolReturnDTO  extends TestResourcePool {
    private TestResourceReturnDTO testResourceReturnDTO;

    @Schema(title = "资源池是否在使用中")
    private Boolean inUsed;
}
