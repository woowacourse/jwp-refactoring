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
import kitchenpos.domain.OrderStatus;
import kitchenpos.ui.dto.OrderChangeStatusRequest;
import kitchenpos.ui.dto.OrderLineItemRequest;
import kitchenpos.ui.dto.OrderLineItemResponse;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class OrderRestControllerTest extends ControllerTest {

    @DisplayName("POST /api/orders")
    @Test
    void create() throws Exception {
        // given
        OrderRequest request = new OrderRequest(1L, List.of(
                new OrderLineItemRequest(1L, 3L)
        ));

        OrderResponse response = new OrderResponse(
                1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                List.of(
                        new OrderLineItemResponse(1L, 1L, 1L, 3)
                )
        );

        given(orderApiService.create(any(OrderRequest.class))).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/orders/" + response.getId()))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @DisplayName("GET /api/orders")
    @Test
    void list() throws Exception {
        // given
        List<OrderResponse> responses = List.of(
                new OrderResponse(
                        1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                        List.of(
                                new OrderLineItemResponse(1L, 1L, 1L, 3)
                        )
                )
        );
        given(orderApiService.list()).willReturn(responses);

        // when
        ResultActions result = mockMvc.perform(get("/api/orders"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)));
    }

    @DisplayName("PUT /api/orders/{orderId}/order-status")
    @Test
    void changeOrderStatus() throws Exception {
        // given
        OrderChangeStatusRequest request = new OrderChangeStatusRequest(OrderStatus.MEAL.name());
        OrderResponse response = new OrderResponse(
                1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                List.of(
                        new OrderLineItemResponse(1L, 1L, 1L, 3)
                )
        );
        given(orderApiService.changeOrderStatus(any(Long.class), any(OrderChangeStatusRequest.class)))
                .willReturn(response);

        // when
        ResultActions result = mockMvc.perform(put("/api/orders/1/order-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
