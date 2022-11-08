package kitchenpos.presentation;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.request.OrderRequest;
import kitchenpos.order.dto.request.OrderStatusRequest;
import kitchenpos.order.dto.response.OrderLineItemResponse;
import kitchenpos.order.dto.response.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

public class OrderControllerTest extends ControllerTest {

    @Test
    @DisplayName("주문을 생성한다.")
    void create() throws Exception {
        // given
        final String request = objectMapper.writeValueAsString(
                new OrderRequest(1L, List.of(new OrderLineItemRequest(1L, 1L)))
        );
        given(orderService.create(any()))
                .willReturn(new OrderResponse(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(),
                        List.of(new OrderLineItemResponse(1L, 1L, 1L, "콜라", BigDecimal.valueOf(1_000L), 1L))));

        // when
        final ResultActions perform = mockMvc.perform(
                        post("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request))
                .andDo(print());

        // then
        perform.andExpect(status().isCreated())
                .andExpect(jsonPath("id", notNullValue()));
    }

    @Test
    @DisplayName("주문을 조회한다.")
    void list() throws Exception {
        // given
        given(orderService.list())
                .willReturn(
                        List.of(
                                new OrderResponse(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(),
                                        List.of(new OrderLineItemResponse(1L, 1L, 1L, "콜라", BigDecimal.valueOf(1_000L),
                                                1L))),
                                new OrderResponse(1L, 2L, OrderStatus.COOKING, LocalDateTime.now(),
                                        List.of(new OrderLineItemResponse(2L, 2L, 3L, "사이다", BigDecimal.valueOf(2_000L),
                                                3L)))));

        // when
        final ResultActions perform = mockMvc.perform(get("/api/orders"))
                .andDo(print());

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus() throws Exception {
        // given
        final String request = objectMapper.writeValueAsString(
                new OrderStatusRequest(OrderStatus.COMPLETION)
        );

        given(orderService.changeOrderStatus(any(), any()))
                .willReturn(new OrderResponse(1L, 1L, OrderStatus.COOKING, LocalDateTime.now(),
                        List.of(new OrderLineItemResponse(1L, 1L, 1L, "콜라", BigDecimal.valueOf(1_000L), 1L))));

        // when
        final ResultActions perform = mockMvc.perform(put("/api/orders/{orderId}/order-status", 1)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        perform.andExpect(status().isOk());
    }
}
