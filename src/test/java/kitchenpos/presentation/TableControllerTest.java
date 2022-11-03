package kitchenpos.presentation;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.order.dto.request.EmptyRequest;
import kitchenpos.order.dto.request.NumberOfGuestsRequest;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class TableControllerTest extends ControllerTest {

    @Test
    @DisplayName("주문테이블을 등록한다.")
    void create() throws Exception {
        // given
        final String request = objectMapper.writeValueAsString(new OrderTableRequest(0, true));
        given(tableService.create(any()))
                .willReturn(new OrderTableResponse(1L, null, 0, true));

        // when
        final ResultActions perform = mockMvc.perform(
                        post("/api/tables")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("id", notNullValue()));
    }

    @Test
    @DisplayName("주문테이블 목록을 조회한다.")
    void list() throws Exception {
        // given
        given(tableService.list())
                .willReturn(List.of(
                        new OrderTableResponse(1L, null, 1, true),
                        new OrderTableResponse(2L, null, 2, true)));

        // when
        final ResultActions perform = mockMvc.perform(get("/api/tables"))
                .andDo(print());

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("테이블의 빈 상태를 변경한다.")
    void changeEmpty() throws Exception {
        // given
        final String request = objectMapper.writeValueAsString(new EmptyRequest(false));
        given(tableService.changeEmpty(any(), any()))
                .willReturn(new OrderTableResponse(1L, null, 1, false));

        // when
        final ResultActions perform = mockMvc.perform(put("/api/tables/{orderTableId}/empty", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }

    @Test
    @DisplayName("테이블의 손님수를 변경한다.")
    void changeNumberOfGuests() throws Exception {
        // given
        final String request = objectMapper.writeValueAsString(new NumberOfGuestsRequest(5));
        given(tableService.changeNumberOfGuests(any(), any()))
                .willReturn(new OrderTableResponse(1L, null, 5, false));

        // when
        final ResultActions perform = mockMvc.perform(put("/api/tables/{orderTableId}/number-of-guests", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }
}
