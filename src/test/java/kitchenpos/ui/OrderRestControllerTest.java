package kitchenpos.ui;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class OrderRestControllerTest extends RestControllerTest {

    @Test
    void 주문_생성에_성공한다() throws Exception {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L, 1L);
        OrderRequest orderRequest = new OrderRequest(Arrays.asList(orderLineItemRequest));
        final OrderMenu 치킨_두마리 = new OrderMenu("치킨 두마리", BigDecimal.TEN);
        OrderLineItem expectedItem = new OrderLineItem(1L, 치킨_두마리, 1);
        OrderResponse expected = new OrderResponse(1L, 1L, COOKING.name(), LocalDateTime.now(), new ArrayList<>());

        when(orderService.create(any())).thenReturn(expected);

        mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequest))).andExpect(status().isCreated())
                .andExpect(header().exists("Location")).andDo(print());
    }
}
