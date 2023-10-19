package kitchenpos.api.table;

import kitchenpos.api.config.ApiTestConfig;
import kitchenpos.ui.dto.request.OrderTableChangeGuestNumberRequest;
import kitchenpos.ui.dto.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
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
        final OrderTableChangeGuestNumberRequest request = new OrderTableChangeGuestNumberRequest(4);

        // when
        final Long orderTableId = 1L;
        final OrderTableResponse response = new OrderTableResponse(orderTableId, request.getNumberOfGuests(), false, null);
        when(tableService.changeNumberOfGuests(eq(orderTableId), eq(request))).thenReturn(response);

        // then
        mockMvc.perform(put("/api/tables/{id}/number-of-guests", orderTableId)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId().intValue())))
                .andExpect(jsonPath("$.numberOfGuests", is(response.getNumberOfGuests())));
    }
}
