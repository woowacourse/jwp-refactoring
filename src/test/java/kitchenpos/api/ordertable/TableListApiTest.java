package kitchenpos.api.ordertable;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.ordertable.application.dto.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TableListApiTest extends ApiTestConfig {

    @DisplayName("주문 테이블 전체 조회 API 테스트")
    @Test
    void listTable() throws Exception {
        // when
        final OrderTableResponse response = new OrderTableResponse(1L, 1, false, null);

        when(tableService.list()).thenReturn(List.of(response));

        // then
        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(response.getId().intValue())))
                .andExpect(jsonPath("$[0].tableGroupId", is(response.getTableGroupId())))
                .andExpect(jsonPath("$[0].numberOfGuests", is(response.getNumberOfGuests())))
                .andExpect(jsonPath("$[0].empty", is(response.isEmpty())));
    }
}
