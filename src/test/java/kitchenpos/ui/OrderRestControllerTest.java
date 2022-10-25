package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import kitchenpos.ControllerTest;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends ControllerTest {

    private final String defaultOrderUrl = "/api/orders";
    @MockBean
    private OrderService orderService;

    @Test
    void 주문을_생성할_수_있다() throws Exception {
        // given
        Order order = new Order();
        when(orderService.create(any(Order.class))).thenReturn(order);

        // when
        ResultActions response = postRequestWithJson(defaultOrderUrl, order);

        // then
        response.andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(order)));
    }

    @Test
    void 주문_목록을_조회할_수_있다() throws Exception {
        // given
        Order order = new Order();
        when(orderService.list()).thenReturn(Arrays.asList(order));

        // when
        ResultActions response = getRequest(defaultOrderUrl);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(Arrays.asList(order))));
    }

    @Test
    void 주문_상태를_변경할_수_있다() throws Exception {
        // given
        Order order = new Order();
        when(orderService.changeOrderStatus(any(Long.class), any(Order.class))).thenReturn(order);

        // when
        ResultActions response = putRequestWithJson(defaultOrderUrl + "/1/order-status", order);

        // then
        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(order)));
    }
}
