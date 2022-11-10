package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.ui.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ui.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class OrderTableRestControllerTest extends ControllerTest {

    @DisplayName("POST /api/tables")
    @Test
    void create() throws Exception {
        // given
        OrderTableRequest request = new OrderTableRequest(2, false);
        OrderTableResponse response = new OrderTableResponse(1L, 1L, 2, false);
        given(orderTabelApiService.create(any(OrderTableRequest.class)))
                .willReturn(response);

        // when
        ResultActions result = mockMvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tables/" + response.getId()))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("GET /api/tables")
    @Test
    void list() throws Exception {
        // given
        List<OrderTableResponse> responses = List.of(
                new OrderTableResponse(1L, 1L, 2, false),
                new OrderTableResponse(2L, 1L, 2, false)
        );
        given(orderTabelApiService.list()).willReturn(responses);

        // when
        ResultActions result = mockMvc.perform(get("/api/tables"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @DisplayName("PUT /api/tables/{orderTableId}/empty")
    @Test
    void changeEmpty() throws Exception {
        // given
        OrderTableChangeEmptyRequest request = new OrderTableChangeEmptyRequest(true);
        OrderTableResponse response = new OrderTableResponse(1L, 1L, 2, true);
        given(orderTabelApiService.changeEmpty(any(Long.class), any(OrderTableChangeEmptyRequest.class)))
                .willReturn(response);

        // when
        ResultActions result = mockMvc.perform(put("/api/tables/1/empty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("PUT /api/tables/{orderTableId}/number-of-guests")
    @Test
    void changeNumberOfGuests() throws Exception {
        // given
        OrderTableChangeNumberOfGuestsRequest request = new OrderTableChangeNumberOfGuestsRequest(3);
        OrderTableResponse response = new OrderTableResponse(1L, 1L, 2, true);
        given(orderTabelApiService.changeNumberOfGuests(any(Long.class),
                any(OrderTableChangeNumberOfGuestsRequest.class)))
                .willReturn(response);

        // when
        ResultActions result = mockMvc.perform(put("/api/tables/1/number-of-guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
