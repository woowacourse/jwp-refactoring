package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.OrderFixture.createOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends ControllerTest {

    @MockBean
    private OrderService orderService;

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        Long orderId = 1L;
        Order order = createOrder();
        Order savedOrder = createOrder(orderId);

        when(orderService.create(any())).thenReturn(savedOrder);

        mockMvc.perform(post("/api/orders")
                .content(objectMapper.writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/orders/" + orderId))
                .andExpect(content().json(objectMapper.writeValueAsString(savedOrder)));
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        List<Order> orders = Arrays.asList(createOrder(1L), createOrder(2L));
        when(orderService.list()).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(orders)));
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() throws Exception {
        Long orderId = 1L;
        Order order = createOrder(orderId);
        order.setOrderStatus(OrderStatus.MEAL.name());

        when(orderService.changeOrderStatus(any(), any())).thenReturn(order);

        mockMvc.perform(put("/api/orders/{orderId}/order-status", orderId)
                .content(objectMapper.writeValueAsString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(order)));
    }
}