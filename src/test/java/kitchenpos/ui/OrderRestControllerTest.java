package kitchenpos.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import kitchenpos.application.OrderService;
import kitchenpos.domain.OrderLineItemCreateInfo;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusChangeRequest;

@WebMvcTest(OrderRestController.class)
class OrderRestControllerTest extends RestControllerTest {
    @MockBean
    private OrderService orderService;

    @DisplayName("주문 생성 요청을 수행한다.")
    @Test
    void create() throws Exception {
        List<OrderLineItemCreateInfo> orderLineItemCreateInfos = Arrays.asList(new OrderLineItemCreateInfo(1L, 1L),
            new OrderLineItemCreateInfo(2L, 2L));
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, orderLineItemCreateInfos);

        List<OrderLineItemResponse> orderLineItems = Arrays.asList(new OrderLineItemResponse(1L, 1L, 1L, 1L),
            new OrderLineItemResponse(2L, 1L, 2L, 2L));
        OrderResponse orderResponse = new OrderResponse(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(),
            orderLineItems);

        given(orderService.create(any(OrderCreateRequest.class))).willReturn(orderResponse);

        mockMvc.perform(post("/api/orders")
            .content(objectMapper.writeValueAsString(orderCreateRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/orders/" + orderResponse.getId()))
            .andDo(print());
    }

    @DisplayName("주문 전체 목록 조회 요청을 수행한다.")
    @Test
    void list() throws Exception {
        List<OrderLineItemResponse> orderLineItems = Arrays.asList(new OrderLineItemResponse(1L, 1L, 1L, 1L),
            new OrderLineItemResponse(2L, 1L, 2L, 2L));
        OrderResponse orderResponse = new OrderResponse(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(),
            orderLineItems);

        given(orderService.create(any(OrderCreateRequest.class))).willReturn(orderResponse);

        mockMvc.perform(get("/api/orders")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @DisplayName("주문 상태 변경 요청을 수행한다.")
    @Test
    void changeOrderStatus() throws Exception {
        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(OrderStatus.MEAL);

        List<OrderLineItemResponse> orderLineItems = Arrays.asList(new OrderLineItemResponse(1L, 1L, 1L, 1L),
            new OrderLineItemResponse(2L, 1L, 2L, 2L));
        OrderResponse orderResponse = new OrderResponse(1L, 1L, OrderStatus.MEAL, LocalDateTime.now(), orderLineItems);

        given(orderService.changeOrderStatus(anyLong(), any(OrderStatusChangeRequest.class))).willReturn(orderResponse);

        mockMvc.perform(put("/api/orders/{id}/order-status", 1L)
            .content(objectMapper.writeValueAsString(orderStatusChangeRequest))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }
}