package kitchenpos.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.Fixtures;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.ui.OrderRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @DisplayName("주문 생성")
    @Test
    void create() throws Exception {
        Order order = Fixtures.makeOrder();

        ObjectMapper objectMapper = new ObjectMapper();

        String content = objectMapper.writeValueAsString(order);

        given(orderService.create(any(OrderRequest.class)))
            .willReturn(order);

        mvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isCreated());
    }

    @DisplayName("주문 불러오기")
    @Test
    void list() throws Exception {
        List<Order> orders = new ArrayList<>();

        given(orderService.list())
            .willReturn(orders);

        mvc.perform(get("/api/orders")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @DisplayName("주문 상태 수정")
    @Test
    void update() throws Exception {
        Order updateOrder = new Order(1L, Fixtures.makeOrderTable().getId(), OrderStatus.MEAL);

        ObjectMapper objectMapper = new ObjectMapper();

        String content = objectMapper.writeValueAsString(updateOrder);

        given(orderService.changeOrderStatus(anyLong(), any(OrderRequest.class)))
            .willReturn(updateOrder);

        mvc.perform(put("/api/orders/{orderId}/order-status", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk());
    }


}
