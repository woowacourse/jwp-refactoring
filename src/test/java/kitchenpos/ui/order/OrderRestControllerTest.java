package kitchenpos.ui.order;

import static kitchenpos.domain.common.OrderStatus.COOKING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dto.order.request.OrderCreateRequest;
import kitchenpos.dto.order.request.OrderLineItemCreateRequest;
import kitchenpos.dto.order.response.OrderLineItemResponse;
import kitchenpos.dto.order.response.OrderResponse;
import kitchenpos.ui.product.RestControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class OrderRestControllerTest extends RestControllerTest {

    @Test
    void 주문_생성에_성공한다() throws Exception {
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(1L, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(1L, List.of(orderLineItemRequest));
        OrderLineItemResponse expectedItem = new OrderLineItemResponse(1L, 1L, 1);
        OrderResponse expected = new OrderResponse(1L, 1L, COOKING.name(), LocalDateTime.now(),
                List.of(expectedItem));

        when(orderService.create(any(OrderCreateRequest.class))).thenReturn(expected);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }
}
