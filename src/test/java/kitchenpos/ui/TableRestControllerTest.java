package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import kitchenpos.ControllerTest;
import kitchenpos.table.application.TableService;
import kitchenpos.table.application.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.table.application.dto.OrderTableChangeStatusRequest;
import kitchenpos.table.application.dto.OrderTableCreateRequest;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.ui.TableRestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(TableRestController.class)
class TableRestControllerTest extends ControllerTest {

    private static final String TABLE_URL = "/api/tables";

    private final OrderTableResponse orderTableResponse = new OrderTableResponse(1L, 1L, 3, true);

    @Autowired
    private TableService tableService;

    @Test
    void table을_생성할_수_있다() throws Exception {
        // given
        when(tableService.create(any(OrderTableCreateRequest.class))).thenReturn(orderTableResponse);

        // when
        ResultActions response = postRequestWithJson(TABLE_URL, new OrderTableCreateRequest());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", TABLE_URL + "/" + 1))
                .andExpect(content().string(objectMapper.writeValueAsString(orderTableResponse)));
    }

    @Test
    void table_목록을_조회할_수_있다() throws Exception {
        // given
        List<OrderTableResponse> orderTableResponses = Arrays.asList(orderTableResponse);
        when(tableService.list()).thenReturn(orderTableResponses);

        // when
        ResultActions response = getRequest(TABLE_URL);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(orderTableResponses)));
    }

    @Test
    void table을_비울_수_있다() throws Exception {
        // given
        String url = TABLE_URL + "/1/empty";
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
        String url = TABLE_URL + "/1/number-of-guests";
        when(tableService.changeNumberOfGuests(any(Long.class),
                any(OrderTableChangeNumberOfGuestsRequest.class))).thenReturn(orderTableResponse);

        // when
        ResultActions response = putRequestWithJson(url, new OrderTableChangeNumberOfGuestsRequest(3));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(orderTableResponse)));
    }
}
