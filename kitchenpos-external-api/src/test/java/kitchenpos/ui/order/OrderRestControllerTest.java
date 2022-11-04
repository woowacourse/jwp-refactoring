package kitchenpos.ui.order;

import static kitchenpos.domain.common.OrderStatus.COOKING;
import static kitchenpos.domain.common.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dto.order.request.OrderCreateRequest;
import kitchenpos.dto.order.request.OrderLineItemCreateRequest;
import kitchenpos.dto.order.request.OrderStatusChangeRequest;
import kitchenpos.dto.order.response.OrderLineItemResponse;
import kitchenpos.dto.order.response.OrderResponse;
import kitchenpos.exception.badrequest.CompletedOrderCannotChangeException;
import kitchenpos.exception.badrequest.MenuNotExistsException;
import kitchenpos.exception.badrequest.OrderLineItemNotExistsException;
import kitchenpos.exception.badrequest.OrderNotExistsException;
import kitchenpos.exception.badrequest.OrderTableEmptyException;
import kitchenpos.exception.badrequest.OrderTableNotExistsException;
import kitchenpos.ui.product.RestControllerTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class OrderRestControllerTest extends RestControllerTest {

    @Test
    void 주문_생성에_성공한다() throws Exception {
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(1L, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(1L, List.of(orderLineItemRequest));
        OrderLineItemResponse expectedItem = new OrderLineItemResponse(1L, 1L, "메뉴 이름", BigDecimal.ZERO);
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

    @Test
    void 주문에_주문_항목이_존재하지_않으면_400() throws Exception {
        OrderCreateRequest orderRequest = new OrderCreateRequest(1L, List.of());

        when(orderService.create(any(OrderCreateRequest.class))).thenThrow(new OrderLineItemNotExistsException());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 주문_항목에_존재하지_않는_메뉴가_있으면_400() throws Exception {
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(1L, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(1L, List.of(orderLineItemRequest));

        when(orderService.create(any(OrderCreateRequest.class))).thenThrow(new MenuNotExistsException());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 주문_대상_테이블이_존재하지_않으면_400() throws Exception {
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(1L, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(1L, List.of(orderLineItemRequest));

        when(orderService.create(any(OrderCreateRequest.class))).thenThrow(new OrderTableNotExistsException());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 주문_대상_테이블이_빈_테이블이면_400() throws Exception {
        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(1L, 1);
        OrderCreateRequest orderRequest = new OrderCreateRequest(1L, List.of(orderLineItemRequest));

        when(orderService.create(any(OrderCreateRequest.class))).thenThrow(new OrderTableEmptyException());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 주문_테이블_목록을_조회할_수_있다() throws Exception {
        OrderLineItemResponse expectedItem = new OrderLineItemResponse(1L, 1L, "메뉴 이름", BigDecimal.ZERO);
        OrderResponse expected = new OrderResponse(1L, 1L, COOKING.name(), LocalDateTime.now(),
                List.of(expectedItem));

        when(orderService.list()).thenReturn(List.of(expected));

        MvcResult mvcResult = mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<OrderResponse> content = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(),
                new TypeReference<List<OrderResponse>>() {
                });

        Assertions.assertThat(content).hasSize(1)
                .extracting("id")
                .containsExactly(expected.getId());
    }

    @Test
    void 주문의_상태를_변경할_수_있다() throws Exception {
        OrderStatusChangeRequest request = new OrderStatusChangeRequest("MEAL");
        OrderLineItemResponse expectedItem = new OrderLineItemResponse(1L, 1L, "메뉴 이름", BigDecimal.ZERO);
        OrderResponse expected = new OrderResponse(1L, 1L, MEAL.name(), LocalDateTime.now(),
                List.of(expectedItem));

        when(orderService.changeOrderStatus(anyLong(), any(OrderStatusChangeRequest.class))).thenReturn(expected);

        MvcResult mvcResult = mockMvc.perform(put("/api/orders/1/order-status")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        OrderResponse content = objectMapper.readValue(mvcResult.getResponse().getContentAsByteArray(),
                OrderResponse.class);

        assertThat(content.getOrderStatus()).isEqualTo("MEAL");
    }

    @Test
    void 상태를_변경하려는_주문이_존재하지_않으면_400() throws Exception {
        OrderStatusChangeRequest request = new OrderStatusChangeRequest("MEAL");

        when(orderService.changeOrderStatus(anyLong(), any(OrderStatusChangeRequest.class)))
                .thenThrow(new OrderNotExistsException());

        mockMvc.perform(put("/api/orders/1/order-status")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void 상태를_변경하려는_주문이_이미_완료됐으면_400() throws Exception {
        OrderStatusChangeRequest request = new OrderStatusChangeRequest("MEAL");

        when(orderService.changeOrderStatus(anyLong(), any(OrderStatusChangeRequest.class)))
                .thenThrow(new CompletedOrderCannotChangeException());

        mockMvc.perform(put("/api/orders/1/order-status")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
