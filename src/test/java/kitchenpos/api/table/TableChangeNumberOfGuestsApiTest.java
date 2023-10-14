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

class TableChangeNumberOfGuestsApiTest extends ApiTestConfig {

    @DisplayName("주문 테이블 주문자 수 변경 API 테스트")
    @Test
    void changeNumberOfGuestsTable() throws Exception {
        // given
        final String request = "{\n" +
                "  \"numberOfGuests\": 4\n" +
                "}";

        // when
        // FIXME: domain -> dto 로 변경
        final Long orderTableId = 1L;
        final int numberOfGuests = 4;
        final OrderTable expectedOrderTable = new OrderTable();
        expectedOrderTable.setId(orderTableId);
        expectedOrderTable.setNumberOfGuests(numberOfGuests);
        when(tableService.changeNumberOfGuests(eq(orderTableId), any(OrderTable.class))).thenReturn(expectedOrderTable);

        // then
        mockMvc.perform(put("/api/tables/{id}/number-of-guests", orderTableId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orderTableId.intValue())))
                .andExpect(jsonPath("$.numberOfGuests", is(expectedOrderTable.getNumberOfGuests())));
    }
}
