package kitchenpos.ui;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class OrderRestControllerTest extends RestControllerTest {

    @Test
    void 주문_생성에_성공한다() throws Exception {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L, 1L);
        OrderRequest orderRequest = new OrderRequest(Arrays.asList(orderLineItemRequest));
        OrderLineItem expectedItem = new OrderLineItem(1L, 1L, 1);
        Order expected = new Order(1L, new OrderTable(5,true), COOKING.name(), LocalDateTime.now(), Arrays.asList(expectedItem));

        when(orderService.create(any())).thenReturn(expected);

        mockMvc.perform(post("/api/orders").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequest))).andExpect(status().isCreated())
                .andExpect(header().exists("Location")).andDo(print());
    }
}
