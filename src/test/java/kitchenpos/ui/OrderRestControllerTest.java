package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.OrderService;
import kitchenpos.application.dto.OrderChangeStatusRequest;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static kitchenpos.fixture.OrderFixture.order;
import static kitchenpos.fixture.OrderLineItemFixture.orderLineItem;
import static kitchenpos.fixture.OrderTableFixtrue.orderTable;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    void 주문을_생성한다() throws Exception {
        // given
        Order createdOrder = order(orderTable(10, false), List.of(orderLineItem(1L, 10)));

        // when
        when(orderService.create(any(OrderRequest.class))).thenReturn(createdOrder);

        // then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(createdOrder)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/orders/" + createdOrder.getId()))
                .andExpect(content().string(objectMapper.writeValueAsString(createdOrder)));
    }

    @Test
    void 주문_목록을_조회한다() throws Exception {
        // given
        Order order1 = order(orderTable(1, false), List.of(orderLineItem(1L, 10)));
        Order order2 = order(orderTable(1, false), List.of(orderLineItem(1L, 10)));

        // when
        when(orderService.list()).thenReturn(List.of(order1, order2));

        // then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(List.of(order1, order2))));
    }

    @Test
    void 주문_상태를_변경한다() throws Exception {
        // given
        Long orderId = 1L;
        OrderChangeStatusRequest request = new OrderChangeStatusRequest(OrderStatus.COMPLETION);
        Order response = order(orderTable(1, false), OrderStatus.COMPLETION, List.of(orderLineItem(1L, 10)));

        // when
        when(orderService.changeOrderStatus(anyLong(), any(OrderChangeStatusRequest.class)))
                .thenReturn(response);

        // when
        mockMvc.perform(put("/api/orders/{id}/order-status", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
