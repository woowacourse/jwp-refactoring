package kitchenpos.api.table;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.ordertable.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.application.dto.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TableChangeEmptyApiTest extends ApiTestConfig {

    @DisplayName("주문 테이블 주문 가능 상태로 변경 API 테스트")
    @Test
    void changeEmptyTable() throws Exception {
        // given
        final Long orderTableId = 1L;
        final OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(false);

        // when
        final OrderTableResponse response = new OrderTableResponse(orderTableId, 1, request.getEmpty(), null);
        when(tableService.changeEmpty(eq(orderTableId), eq(request))).thenReturn(response);

        // then
        mockMvc.perform(put("/api/tables/{id}/empty", orderTableId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId().intValue())))
                .andExpect(jsonPath("$.empty", is(response.isEmpty())));
    }
}
