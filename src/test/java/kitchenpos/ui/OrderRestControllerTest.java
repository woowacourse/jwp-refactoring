package kitchenpos.ui;

import static kitchenpos.domain.OrderStatus.COOKING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.response.OrderLineItemCreateResponse;
import kitchenpos.dto.response.OrderResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class OrderRestControllerTest extends RestControllerTest {

    @Test
    void 주문_생성에_성공한다() throws Exception {
        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(1L, 1);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, List.of(orderLineItemCreateRequest));
        OrderLineItemCreateResponse orderLineItemCreateResponse = new OrderLineItemCreateResponse(1L, 1L, 1);
        OrderResponse orderResponse = new OrderResponse(1L, 1L, COOKING.name(), LocalDateTime.now(),
                List.of(orderLineItemCreateResponse));

        when(orderService.create(any(OrderCreateRequest.class))).thenReturn(orderResponse);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }
}
