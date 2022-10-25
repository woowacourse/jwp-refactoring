package kitchenpos.ui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class OrderRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("Order를 생성한다.")
    void create() throws Exception {
        Order order = createOrder(1L);
        given(orderService.create(any(Order.class))).willReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpectAll(status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "/api/orders/1"));
    }

    @Test
    @DisplayName("Order를 모두 조회한다.")
    void list() throws Exception {
        Order order1 = createOrder(1L);
        Order order2 = createOrder(2L);
        List<Order> orders = List.of(order1, order2);

        given(orderService.list()).willReturn(orders);
        mockMvc.perform(get("/api/orders"))
                .andExpectAll(status().isOk(),
                        content().string(objectMapper.writeValueAsString(orders)));
    }

    @Test
    @DisplayName("Order의 상태를 변경한다.")
    void changeOrderStatus() throws Exception {
        Order order = createOrder(1L);

        given(orderService.changeOrderStatus(anyLong(), any(Order.class))).willReturn(order);
        mockMvc.perform(put("/api/orders/1/order-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpectAll(status().isOk(),
                        content().string(objectMapper.writeValueAsString(order)));
    }

    private Order createOrder(final Long id) {
        Order order = new Order(id, 1L, OrderStatus.COOKING.name(), LocalDateTime.now());
        OrderLineItem orderLineItem1 = new OrderLineItem(order.getId(), 1L, 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(order.getId(), 1L, 2);
        order.addOrderLineItems(List.of(orderLineItem1, orderLineItem2));
        return order;
    }
}
