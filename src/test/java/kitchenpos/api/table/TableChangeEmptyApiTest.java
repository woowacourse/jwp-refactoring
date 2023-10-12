package kitchenpos.api.table;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
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
        final String request = "{\n" +
                "  \"empty\": false\n" +
                "}";

        // when
        // FIXME: domain -> dto 로 변경
        final Long orderTableId = 1L;
        final OrderTable expectedOrderTable = new OrderTable();
        expectedOrderTable.setId(orderTableId);
        expectedOrderTable.setEmpty(false);
        when(tableService.changeEmpty(eq(orderTableId), any(OrderTable.class))).thenReturn(expectedOrderTable);

        // then
        mockMvc.perform(put("/api/tables/{id}/empty", orderTableId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orderTableId.intValue())))
                .andExpect(jsonPath("$.empty", is(expectedOrderTable.isEmpty())));
    }
}
