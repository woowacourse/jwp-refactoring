package kitchenpos.ui;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.request.ChangeOrderStatusRequest;
import kitchenpos.order.ui.request.CreateOrderRequest;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.response.CreateOrderResponse;
import kitchenpos.order.ui.response.OrderLineItemResponse;
import kitchenpos.order.ui.response.OrderResponse;

import static kitchenpos.fixture.MenuFixture.후라이드_단품;
import static kitchenpos.fixture.OrderFixture.COMPLETION_ORDER;
import static kitchenpos.fixture.OrderFixture.COOKING_ORDER;
import static kitchenpos.fixture.OrderLineItemFixture.후라이드_단품_둘;
import static kitchenpos.fixture.OrderTableFixture.단일_손님0_테이블1;
import static kitchenpos.fixture.OrderTableFixture.단일_손님2_테이블;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("TableRestController 단위 테스트")
class OrderRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("주문을 등록할 수 있다. - 해당 주문은 조리중(COOKING) 상태가 된다.")
    void create() throws Exception {
        // given
        CreateOrderRequest order = new CreateOrderRequest(
                단일_손님2_테이블.getId(),
                Collections.singletonList(new OrderLineItemRequest(후라이드_단품.getId(), 2))
        );
        CreateOrderResponse expected = new CreateOrderResponse(
                1L,
                단일_손님2_테이블.getId(),
                OrderStatus.COOKING,
                LocalDateTime.now(),
                Collections.singletonList(new OrderLineItemResponse(1L, 후라이드_단품.getId(), 2))
        );
        given(orderService.create(any(CreateOrderRequest.class))).willReturn(expected);

        // when
        ResultActions response = mockMvc.perform(post("/api/orders")
                .content(objectToJsonString(order))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/orders/" + expected.getId()))
                .andExpect(content().json(objectToJsonString(expected)));
    }

    @Test
    @DisplayName("메뉴 목록은 하나이상 있어야한다.")
    void createWrongOrderLineItemsEmpty() throws Exception {
        // given
        CreateOrderRequest order = new CreateOrderRequest(
                단일_손님2_테이블.getId(),
                Collections.singletonList(new OrderLineItemRequest(후라이드_단품.getId(), 2))
        );
        willThrow(new IllegalArgumentException("주문하려면 하나 이상의 메뉴가 필요합니다."))
                .given(orderService).create(any(CreateOrderRequest.class));

        // when
        ResultActions response = mockMvc.perform(post("/api/orders")
                .content(objectToJsonString(order))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("주문하려면 하나 이상의 메뉴가 필요합니다."));
    }

    @Test
    @DisplayName("메뉴 목록에 포함된 메뉴들은 모두 등록된 메뉴여야한다.")
    void createWrongOrderLineItemsNotRegister() throws Exception {
        // given
        CreateOrderRequest order = new CreateOrderRequest(
                단일_손님2_테이블.getId(),
                Collections.singletonList(new OrderLineItemRequest(10L, 2))
        );
        willThrow(new IllegalArgumentException("등록되지 않은 메뉴는 주문할 수 없습니다."))
                .given(orderService).create(any(CreateOrderRequest.class));

        // when
        ResultActions response = mockMvc.perform(post("/api/orders")
                .content(objectToJsonString(order))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("등록되지 않은 메뉴는 주문할 수 없습니다."));
    }

    @Test
    @DisplayName("주문하려는 테이블은 존재해야 한다.")
    void createWrongTableNotExist() throws Exception {
        CreateOrderRequest order = new CreateOrderRequest(
                10L,
                Collections.singletonList(new OrderLineItemRequest(후라이드_단품.getId(), 2))
        );
        willThrow(new IllegalArgumentException("존재하지 않는 테이블은 주문할 수 없습니다."))
                .given(orderService).create(any(CreateOrderRequest.class));

        // when
        ResultActions response = mockMvc.perform(post("/api/orders")
                .content(objectToJsonString(order))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("존재하지 않는 테이블은 주문할 수 없습니다."));
    }

    @Test
    @DisplayName("주문하려는 테이블은 비어있지 않아야한다.")
    void createWrongTableEmpty() throws Exception {
        CreateOrderRequest order = new CreateOrderRequest(
                단일_손님0_테이블1.getId(),
                Collections.singletonList(new OrderLineItemRequest(후라이드_단품.getId(), 2))
        );
        willThrow(new IllegalArgumentException("빈 테이블은 주문할 수 없습니다."))
                .given(orderService).create(any(CreateOrderRequest.class));

        // when
        ResultActions response = mockMvc.perform(post("/api/orders")
                .content(objectToJsonString(order))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("빈 테이블은 주문할 수 없습니다."));
    }

    @Test
    @DisplayName("전체 주문을 조회할 수 있다.")
    void list() throws Exception {
        // given
        List<OrderResponse> expected = Arrays.asList(
                OrderResponse.from(COOKING_ORDER),
                OrderResponse.from(COMPLETION_ORDER)
        );
        given(orderService.list()).willReturn(expected);

        // when
        ResultActions response = mockMvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().json(objectToJsonString(expected)));
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() throws Exception {
        // given
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("MEAL");
        OrderResponse expected = new OrderResponse(
                1L,
                OrderStatus.MEAL,
                LocalDateTime.now(),
                Collections.singletonList(OrderLineItemResponse.from(후라이드_단품_둘))
        );

        given(orderService.changeOrderStatus(anyLong(), any(ChangeOrderStatusRequest.class))).willReturn(expected);

        // when
        ResultActions response = mockMvc.perform(put("/api/orders/1/order-status")
                .content(objectToJsonString(request))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().json(objectToJsonString(expected)));
    }

    @Test
    @DisplayName("주문 상태를 변경하려면 주문은 존재해야 한다.")
    void changeOrderStatusWrongOrderNotExist() throws Exception {
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("MEAL");
        willThrow(new IllegalArgumentException("존재하지 않는 주문의 상태는 변경할 수 없습니다."))
                .given(orderService).changeOrderStatus(anyLong(), any(ChangeOrderStatusRequest.class));

        // when
        ResultActions response = mockMvc.perform(put("/api/orders/11/order-status")
                .content(objectToJsonString(request))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("존재하지 않는 주문의 상태는 변경할 수 없습니다."));
    }

    @Test
    @DisplayName("주문 상태를 변경하려면 주문 상태는 조리중(COOKING)이나 식사중(MEAL)이어야한다.")
    void changeOrderStatusWrongOrderStatus() throws Exception {
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("MEAL");
        willThrow(new IllegalArgumentException("계산 완료된 주문의 상태는 변경할 수 없습니다."))
                .given(orderService).changeOrderStatus(anyLong(), any(ChangeOrderStatusRequest.class));

        // when
        ResultActions response = mockMvc.perform(put("/api/orders/1/order-status")
                .content(objectToJsonString(request))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("계산 완료된 주문의 상태는 변경할 수 없습니다."));
    }
}
