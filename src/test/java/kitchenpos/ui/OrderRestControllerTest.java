package kitchenpos.ui;

import static kitchenpos.domain.OrderStatus.COOKING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.ui.dto.OrderLineItemRequest;
import kitchenpos.ui.dto.OrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class OrderRestControllerTest extends RestControllerTest {

    @Test
    void 주문_생성에_성공한다() throws Exception {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L, 1L, 1L);
        OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(orderLineItemRequest));
        OrderLineItem expectedItem = new OrderLineItem(1L, 1L, 1);
        Order expected = new Order(1L, 1L, COOKING.name(), LocalDateTime.now(),
                Arrays.asList(expectedItem));

        when(orderService.create(any())).thenReturn(expected);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andDo(print());
    }
}
