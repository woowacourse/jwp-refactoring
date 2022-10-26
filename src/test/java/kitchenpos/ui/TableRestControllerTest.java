package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import kitchenpos.ControllerTest;
import kitchenpos.application.TableService;
import kitchenpos.application.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.request.OrderTableChangeStatusRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends ControllerTest {

    private final String defaultTableUrl = "/api/tables";
    private final OrderTableResponse orderTableResponse = new OrderTableResponse(1L, 1L, 3, true);

    @MockBean
    private TableService tableService;

    @Test
    void table을_생성할_수_있다() throws Exception {
        // given
        when(tableService.create(any(OrderTableCreateRequest.class))).thenReturn(orderTableResponse);

        // when
        ResultActions response = postRequestWithJson(defaultTableUrl, new OrderTableCreateRequest());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", defaultTableUrl + "/" + 1))
                .andExpect(content().string(objectMapper.writeValueAsString(orderTableResponse)));
    }

    @Test
    void table_목록을_조회할_수_있다() throws Exception {
        // given
        List<OrderTableResponse> orderTableResponses = Arrays.asList(orderTableResponse);
        when(tableService.list()).thenReturn(orderTableResponses);

        // when
        ResultActions response = getRequest(defaultTableUrl);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(orderTableResponses)));
    }

    @Test
    void table을_비울_수_있다() throws Exception {
        // given
        String url = defaultTableUrl + "/1/empty";
        when(tableService.changeEmpty(any(Long.class), any(OrderTableChangeStatusRequest.class))).thenReturn(
                orderTableResponse);

        // when
        ResultActions response = putRequestWithJson(url, new OrderTableChangeStatusRequest());

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(orderTableResponse)));
    }

    @Test
    void table_손님의_수를_변경할_수_있다() throws Exception {
        // given
        String url = defaultTableUrl + "/1/number-of-guests";
        when(tableService.changeNumberOfGuests(any(Long.class),
                any(OrderTableChangeNumberOfGuestsRequest.class))).thenReturn(orderTableResponse);

        // when
        ResultActions response = putRequestWithJson(url, new OrderTableChangeNumberOfGuestsRequest(3));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(orderTableResponse)));
    }
}
