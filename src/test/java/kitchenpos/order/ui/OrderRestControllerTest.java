package kitchenpos.order.ui;

import static kitchenpos.TestFixtures.createOrderLineItem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import kitchenpos.TestFixtures;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderResponses;
import kitchenpos.order.application.dto.OrderStatusRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.OrderRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("주문 api")
@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    void create() throws Exception {
        final Order order = TestFixtures.createOrder();
        final String content = objectMapper.writeValueAsString(TestFixtures.createOrderRequest(order));
        when(orderService.create(any())).thenReturn(new OrderResponse(order));

        final MockHttpServletResponse response = mockMvc.perform(post("/api/orders")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertAll(
                () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.getHeader("Location")).isEqualTo("/api/orders/" + order.getId())
        );
    }

    @Test
    void list() throws Exception {
        final Order order = TestFixtures.createOrder();
        order.updateOrderLineItems(Collections.singletonList(createOrderLineItem(1L)));
        when(orderService.list()).thenReturn(new OrderResponses(Collections.singletonList(order)));

        final MockHttpServletResponse response = mockMvc.perform(get("/api/orders"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void changeOrderStatus() throws Exception {
        final Order order = TestFixtures.createOrder();
        order.updateOrderLineItems(Collections.singletonList(createOrderLineItem(1L)));
        final String content = objectMapper.writeValueAsString(new OrderStatusRequest(OrderStatus.MEAL.name()));
        when(orderService.changeOrderStatus(any(), any())).thenReturn(new OrderResponse(order));

        final MockHttpServletResponse response = mockMvc.perform(put("/api/orders/" + order.getId() + "/order-status")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
