package kitchenpos.api.tablegroup;

import kitchenpos.api.config.ApiTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TableGroupDeleteApiTest extends ApiTestConfig {

    @DisplayName("주문 테이블 주문 가능 상태로 변경 API 테스트")
    @Test
    void changeEmptyTable() throws Exception {
        // given
        final Long tableGroupId = 1L;

        // then
        mockMvc.perform(delete("/api/table-groups/{id}", tableGroupId))
                .andExpect(status().isNoContent());
    }
}
