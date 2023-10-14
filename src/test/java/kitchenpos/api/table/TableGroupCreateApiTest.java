package kitchenpos.api.table;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TableGroupCreateApiTest extends ApiTestConfig {

    @DisplayName("주문 테이블 그룹 생성 API 테스트")
    @Test
    void createTableGroup() throws Exception {
        // given
        final String request = "{\n" +
                "  \"orderTables\": [\n" +
                "    {\n" +
                "      \"id\": 1\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 2\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        // when
        // FIXME: domain -> dto 로 변경
        final Long expectedId = 1L;
        final TableGroup expectedTableGroup = new TableGroup();
        expectedTableGroup.setId(expectedId);
        when(tableGroupService.create(any(TableGroup.class))).thenReturn(expectedTableGroup);

        // then
        mockMvc.perform(post("/api/table-groups")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(String.format("/api/table-groups/%d", expectedId)));
    }
}
