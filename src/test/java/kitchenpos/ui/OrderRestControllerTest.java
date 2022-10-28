package kitchenpos.ui;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.application.OrderService;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderRequest;
import kitchenpos.application.dto.request.OrderChangeRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.OrderStatus;

@ApiTest
@DisplayName("Order API 테스트")
class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @DisplayName("주문을 생성한다")
    @Test
    void create() throws Exception {
        final long orderTableId = 1L;

        final OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 1);
        final OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 1);
        final OrderRequest request = new OrderRequest(orderTableId, List.of(orderLineItemRequest1, orderLineItemRequest2));
        final String body = objectMapper.writeValueAsString(request);

        final OrderResponse response = new OrderResponse(1L, orderTableId, OrderStatus.COOKING.name(), LocalDateTime.now(), List.of());
        BDDMockito.given(orderService.create(any()))
                .willReturn(response);

        mockMvc.perform(post("/api/orders")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", containsString("/api/orders/" + response.getId())))
        ;
    }

    @DisplayName("주문 목록을 조회한다")
    @Test
    void list() throws Exception {
        final OrderResponse response = new OrderResponse(
            1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.emptyList()
        );

        BDDMockito.given(orderService.list())
                .willReturn(List.of(response));

        mockMvc.perform(get("/api/orders"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            ;
    }

    @DisplayName("주문의 상태를 변경한다")
    @Test
    void changeOrderStatus() throws Exception {
        final long orderId = 1L;

        final OrderChangeRequest request = new OrderChangeRequest(OrderStatus.MEAL.name());
        final String body = objectMapper.writeValueAsString(request);

        final OrderResponse response = new OrderResponse(
            orderId, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), null
        );
        BDDMockito.given(orderService.changeOrderStatus(eq(orderId), any()))
                .willReturn(response);

        mockMvc.perform(put("/api/orders/" + orderId + "/order-status")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("orderStatus", containsString(response.getOrderStatus())))
        ;
    }
}
