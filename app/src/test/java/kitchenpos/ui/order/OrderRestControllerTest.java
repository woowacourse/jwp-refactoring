package kitchenpos.ui.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kitchenpos.application.order.dto.ChangeOrderStatusResponse;
import kitchenpos.application.order.dto.CreateOrderResponse;
import kitchenpos.application.order.dto.SearchOrderResponse;
import kitchenpos.ui.ControllerTest;
import kitchenpos.ui.order.dto.ChangeOrderStatusRequest;
import kitchenpos.ui.order.dto.CreateOrderRequest;
import kitchenpos.ui.order.dto.OrderLineItemRequest;
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
        List<SearchOrderResponse> searchOrderResponses = List.of(
                SearchOrderResponse.from(식사중인_주문(1L)),
                SearchOrderResponse.from(식사중인_주문(2L))
        );
        given(orderService.list()).willReturn(searchOrderResponses);
        String response = objectMapper.writeValueAsString(searchOrderResponses);

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
