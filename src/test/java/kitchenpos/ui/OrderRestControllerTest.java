package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kitchenpos.annotation.ControllerTest;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ControllerTest
@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 주문을_저장한다() throws Exception {
        // given
        Order order = new Order();
        order.setId(1L);
        when(orderService.create(any(Order.class))).thenReturn(order);

        // when
        ResultActions result = mockMvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(order))
        );

        // then
        result.andExpectAll(
                status().isCreated(),
                header().stringValues("Location", "/api/orders/" + order.getId()),
                content().contentType(APPLICATION_JSON),
                content().json(objectMapper.writeValueAsString(order))
        );
    }

    @Test
    void 모든_주문_목록을_조회한다() throws Exception {
        // given
        List<Order> orders = List.of(new Order(), new Order());
        when(orderService.list()).thenReturn(orders);

        // when
        ResultActions result = mockMvc.perform(get("/api/orders")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(orders))
        );

        // then
        result.andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json(objectMapper.writeValueAsString(orders))
        );
    }

    @Test
    void 주문의_상태를_변경한다() throws Exception {
        // given
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        when(orderService.changeOrderStatus(anyLong(), any(Order.class))).thenReturn(order);

        // when
        ResultActions result = mockMvc.perform(put("/api/orders/{orderId}/order-status", order.getId())
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(order))
        );

        // then
        result.andExpectAll(
                status().isOk(),
                content().contentType(APPLICATION_JSON),
                content().json(objectMapper.writeValueAsString(order))
        );
    }
}
