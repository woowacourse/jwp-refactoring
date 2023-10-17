package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.application.dto.order.ChangeOrderStatusResponse;
import kitchenpos.application.dto.order.CreateOrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.ui.dto.ChangeOrderStatusRequest;
import kitchenpos.ui.dto.CreateOrderRequest;
import kitchenpos.ui.dto.OrderLineItemRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class OrderRestControllerTest extends ControllerTest {

    @Test
    void 주문_생성() throws Exception {
        // given
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, List.of(
                new OrderLineItemRequest(1L, 2)
        ));
        String request = objectMapper.writeValueAsString(createOrderRequest);

        CreateOrderResponse createOrderResponse = CreateOrderResponse.from(식사중인_주문(1L));
        given(orderService.create(any())).willReturn(createOrderResponse);
        String response = objectMapper.writeValueAsString(createOrderResponse);

        // when & then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().json(response));
    }

    @Test
    void 주문_조회() throws Exception {
        // given
        Order order1 = 주문(1L);
        Order order2 = 주문(2L);
        List<Order> orders = List.of(order1, order2);
        given(orderService.list()).willReturn(orders);
        String response = objectMapper.writeValueAsString(orders);

        // when & then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void 주문_상태_변경() throws Exception {
        // given
        ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest("COMPLETION");
        String request = objectMapper.writeValueAsString(changeOrderStatusRequest);

        ChangeOrderStatusResponse changeOrderStatusResponse = ChangeOrderStatusResponse.from(식사중인_주문(1L));
        given(orderService.changeOrderStatus(any())).willReturn(changeOrderStatusResponse);
        String response = objectMapper.writeValueAsString(changeOrderStatusResponse);

        // when & then
        mockMvc.perform(put("/api/orders/1/order-status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }
}
