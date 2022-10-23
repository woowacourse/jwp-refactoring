package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.sun.tools.javac.util.List;

import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.OrderTableUpdateRequest;

class TableRestControllerTest extends ControllerTest {

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void create() throws Exception {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(3, true);
        OrderTableResponse orderTableResponse = new OrderTableResponse(
            1L, null, 3, true
        );

        given(tableService.create(any(OrderTableRequest.class)))
            .willReturn(orderTableResponse);

        // when
        ResultActions result = mockMvc.perform(post("/api/tables")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderTableRequest)));

        // then
        result.andExpect(status().isCreated())
            .andExpect(header().string("location", "/api/tables/1"))
            .andExpect(content().json(objectMapper.writeValueAsString(orderTableResponse)));
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() throws Exception {
        // given
        List<OrderTableResponse> orderTableResponses = List.of(
            new OrderTableResponse(
                1L, null, 3, true
            ),
            new OrderTableResponse(
                2L, 1L, 2, true
            )
        );

        given(tableService.list())
            .willReturn(orderTableResponses);

        // when
        ResultActions result = mockMvc.perform(get("/api/tables"));

        // then
        result.andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(orderTableResponses)));
    }

    @DisplayName("주문 테이블이 비었는지의 상태를 변경한다.")
    @Test
    void changeEmpty() throws Exception {
        // given
        OrderTableUpdateRequest tableUpdateRequest = new OrderTableUpdateRequest(false, null);
        OrderTableResponse orderTableResponse = new OrderTableResponse(
            1L, null, 3, false
        );

        given(tableService.changeEmpty(1L, false))
            .willReturn(orderTableResponse);

        // when
        ResultActions result = mockMvc.perform(put("/api/tables/1/empty")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(tableUpdateRequest)));

        // then
        result.andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(orderTableResponse)));
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다..")
    @Test
    void changeNumberOfGuests() throws Exception {
        // given
        OrderTableUpdateRequest tableUpdateRequest = new OrderTableUpdateRequest(null, 2);
        OrderTableResponse orderTableResponse = new OrderTableResponse(
            1L, null, 2, true
        );

        given(tableService.changeNumberOfGuests(1L, 2))
            .willReturn(orderTableResponse);

        // when
        ResultActions result = mockMvc.perform(put("/api/tables/1/number-of-guests")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(tableUpdateRequest)));

        // then
        result.andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(orderTableResponse)));
    }
}
