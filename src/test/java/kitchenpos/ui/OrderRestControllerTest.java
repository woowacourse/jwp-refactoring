package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.ControllerTest;
import kitchenpos.application.OrderService;
import kitchenpos.application.dto.request.OrderChangeRequest;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.response.OrderResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends ControllerTest {

    private final String defaultOrderUrl = "/api/orders";
    private final OrderResponse orderResponse = new OrderResponse(1L, 11L, "COOKING", LocalDateTime.now(), new ArrayList<>());

    @MockBean
    private OrderService orderService;

    @Test
    void 주문을_생성할_수_있다() throws Exception {
        // given
        when(orderService.create(any(OrderCreateRequest.class))).thenReturn(orderResponse);

        // when
        ResultActions response = postRequestWithJson(defaultOrderUrl, new OrderCreateRequest());

        // then
        response.andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(orderResponse)));
    }

    @Test
    void 주문_목록을_조회할_수_있다() throws Exception {
        // given
        List<OrderResponse> orderResponses = Arrays.asList(orderResponse);
        when(orderService.list()).thenReturn(orderResponses);

        // when
        ResultActions response = getRequest(defaultOrderUrl);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(orderResponses)));
    }

    @Test
    void 주문_상태를_변경할_수_있다() throws Exception {
        // given
        when(orderService.changeOrderStatus(any(Long.class), any(OrderChangeRequest.class))).thenReturn(orderResponse);

        // when
        ResultActions response = putRequestWithJson(defaultOrderUrl + "/1/order-status", new OrderChangeRequest());

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(orderResponse)));
    }
}
