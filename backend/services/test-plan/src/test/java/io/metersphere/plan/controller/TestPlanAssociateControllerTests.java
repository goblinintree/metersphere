package io.metersphere.plan.controller;

import io.metersphere.functional.request.FunctionalCasePageRequest;
import io.metersphere.sdk.util.JSON;
import io.metersphere.system.base.BaseTest;
import io.metersphere.system.controller.handler.ResultHolder;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class TestPlanAssociateControllerTests extends BaseTest {
    public static final String FUNCTIONAL_CASE_ASSOCIATION_URL = "/test-plan/association/page";

    @Test
    @Order(1)
    @Sql(scripts = {"/dml/init_test_plan_association.sql"}, config = @SqlConfig(encoding = "utf-8", transactionMode = SqlConfig.TransactionMode.ISOLATED))
    public void testGetPageList() throws Exception {
        FunctionalCasePageRequest request = new FunctionalCasePageRequest();
        request.setProjectId("123");
        request.setCurrent(1);
        request.setPageSize(10);
        this.requestPost(FUNCTIONAL_CASE_ASSOCIATION_URL, request);
        request.setSort(new HashMap<>() {{
            put("createTime", "desc");
        }});
        request.setTestPlanId("wxx_1");
        MvcResult mvcResult = this.requestPostWithOkAndReturn(FUNCTIONAL_CASE_ASSOCIATION_URL, request);
        String returnData = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResultHolder resultHolder = JSON.parseObject(returnData, ResultHolder.class);
        Assertions.assertNotNull(resultHolder);

    }
}
