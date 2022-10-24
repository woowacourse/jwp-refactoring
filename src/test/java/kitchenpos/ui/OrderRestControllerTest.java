package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class OrderRestControllerTest extends ControllerTest {

    @Autowired
    private OrderService orderService;

    @Test
    void create() throws Exception {
        // given
        long id = 1L;
        Order order = new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), List.of());
        order.setId(id);

        given(orderService.create(any())).willReturn(order);

        // when
        ResultActions actions = mockMvc.perform(post("/api/orders")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(order))
        );

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/orders/" + id));
    }

    @Test
    void list() throws Exception {
        // given
        Order order = new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), List.of());
        given(orderService.list()).willReturn(List.of(order));

        // when
        ResultActions actions = mockMvc.perform(get("/api/orders"));

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(order))));
    }

    @Test
    void changeOrderStatus() throws Exception {
        // given
        Order order = new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), List.of());
        given(orderService.changeOrderStatus(any(), any())).willReturn(order);

        // when
        ResultActions actions = mockMvc.perform(put("/api/orders/{orderId}/order-status", 1L)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(order))
        );

        // then
        actions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(order)));
    }
}
