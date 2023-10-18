package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.application.dto.ordertable.ChangeOrderTableEmptyResponse;
import kitchenpos.application.dto.ordertable.ChangeOrderTableNumberOfGuestsResponse;
import kitchenpos.application.dto.ordertable.CreateOrderTableResponse;
import kitchenpos.application.dto.ordertable.SearchOrderTableResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.ChangeOrderTableEmptyRequest;
import kitchenpos.ui.dto.ChangeOrderTableNumberOfGuestsRequest;
import kitchenpos.ui.dto.CreateOrderTableRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class TableRestControllerTest extends ControllerTest {

    @Test
    void 주문_테이블_생성() throws Exception {
        // given
        CreateOrderTableRequest createOrderTableRequest = new CreateOrderTableRequest(0, true);
        String request = objectMapper.writeValueAsString(createOrderTableRequest);

        OrderTable orderTable = new OrderTable(1L, null, 0, true);
        CreateOrderTableResponse createOrderTableResponse = CreateOrderTableResponse.from(orderTable);
        given(tableService.create(any())).willReturn(createOrderTableResponse);
        String response = objectMapper.writeValueAsString(createOrderTableResponse);

        // when & then
        mockMvc.perform(post("/api/tables")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().json(response));
    }

    @Test
    void 주문_테이블_조회() throws Exception {
        // given
        List<SearchOrderTableResponse> searchOrderTableResponses = List.of(
                SearchOrderTableResponse.from(주문_테이블(1L)),
                SearchOrderTableResponse.from(주문_테이블(2L))
        );
        given(tableService.list()).willReturn(searchOrderTableResponses);
        String response = objectMapper.writeValueAsString(searchOrderTableResponses);

        // when & then
        mockMvc.perform(get("/api/tables"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void 주문_테이블_상태_변경() throws Exception {
        // given
        ChangeOrderTableEmptyRequest changeOrderTableEmptyRequest = new ChangeOrderTableEmptyRequest(true);
        String request = objectMapper.writeValueAsString(changeOrderTableEmptyRequest);

        ChangeOrderTableEmptyResponse changeOrderTableEmptyResponse =
                ChangeOrderTableEmptyResponse.from(new OrderTable(1L, null, 0, true));
        given(tableService.changeEmpty(any())).willReturn(changeOrderTableEmptyResponse);
        String response = objectMapper.writeValueAsString(changeOrderTableEmptyRequest);

        // when & then
        mockMvc.perform(put("/api/tables/1/empty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void 주문_테이블_인원_변경() throws Exception {
        // given
        ChangeOrderTableNumberOfGuestsRequest changeOrderTableNumberOfGuestsRequest =
                new ChangeOrderTableNumberOfGuestsRequest(2);
        String request = objectMapper.writeValueAsString(changeOrderTableNumberOfGuestsRequest);

        ChangeOrderTableNumberOfGuestsResponse changeOrderTableNumberOfGuestsResponse =
                ChangeOrderTableNumberOfGuestsResponse.from(new OrderTable(1L, null, 2, true));
        given(tableService.changeNumberOfGuests(any())).willReturn(changeOrderTableNumberOfGuestsResponse);
        String response = objectMapper.writeValueAsString(changeOrderTableNumberOfGuestsRequest);

        // when & then
        mockMvc.perform(put("/api/tables/1/number-of-guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }
}
