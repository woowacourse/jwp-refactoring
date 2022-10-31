package kitchenpos.order.ui;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderedMenu;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.support.ControllerTest;

class OrderRestControllerTest extends ControllerTest {

    @DisplayName("주문을 생성한다.")
    @Test
    void create() throws Exception {
        // given
        OrderResponse orderResponse = new OrderResponse(
            1L,
            1L,
            OrderStatus.COOKING.name(),
            LocalDateTime.now(),
            List.of(
                new OrderLineItemResponse(
                    1L, 1L, new OrderedMenu("치킨", BigDecimal.valueOf(20000)), 2L
                ),
                new OrderLineItemResponse(
                    2L, 1L, new OrderedMenu("콜라", BigDecimal.valueOf(1500)), 1L
                )
            )
        );

        given(orderService.create(any(OrderRequest.class)))
            .willReturn(orderResponse);

        // when
        ResultActions result = mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(
                new OrderRequest(
                    1L,
                    List.of(
                        new OrderLineItemRequest(1L, 2),
                        new OrderLineItemRequest(2L, 1)
                    )
                ))
            ));

        // then
        result.andExpect(status().isCreated())
            .andExpect(header().string("location", "/api/orders/1"))
            .andExpect(content().json(toJson(orderResponse)));
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() throws Exception {
        // given
        List<OrderResponse> orderResponses = List.of(
            new OrderResponse(
                1L,
                1L,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                List.of(
                    new OrderLineItemResponse(
                        1L, 1L, new OrderedMenu("치킨", BigDecimal.valueOf(20000)), 2L
                    ),
                    new OrderLineItemResponse(
                        2L, 1L, new OrderedMenu("콜라", BigDecimal.valueOf(1500)), 1L
                    )
                )
            ));

        given(orderService.list())
            .willReturn(orderResponses);

        // when
        ResultActions result = mockMvc.perform(get("/api/orders"));

        // then
        result.andExpect(status().isOk())
            .andExpect(content().json(toJson(orderResponses)));
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        // given
        OrderResponse orderResponse = new OrderResponse(
            1L,
            1L,
            "MEAL",
            LocalDateTime.now(),
            new ArrayList<>()
        );
        given(orderService.changeOrderStatus(1L, OrderStatus.MEAL))
            .willReturn(orderResponse);

        // when
        ResultActions result = mockMvc.perform(put("/api/orders/1/order-status")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(new OrderStatusRequest("MEAL"))));

        // then
        result.andExpect(status().isOk())
            .andExpect(content().json(toJson(orderResponse)));
    }
}
