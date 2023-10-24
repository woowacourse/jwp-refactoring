package kitchenpos.ui;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.application.response.OrderResponse;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.application.request.OrderLineItemsRequest;
import kitchenpos.application.request.OrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderService orderService;

    @Test
    @DisplayName("POST /api/orders")
    void createProduct() throws Exception {
        final List<OrderLineItemsRequest> orderLineItemsRequests = List.of(
                new OrderLineItemsRequest(1L, 4L),
                new OrderLineItemsRequest(2L, 7L)
        );
        final OrderRequest orderRequest = new OrderRequest(1L, orderLineItemsRequests);

        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 4L);
        final OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 2L, 7L);

        when(orderService.create(1L, orderLineItemsRequests))
                .thenReturn(
                        OrderResponse.from(new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(),
                                List.of(orderLineItem, orderLineItem2))
                ));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}
