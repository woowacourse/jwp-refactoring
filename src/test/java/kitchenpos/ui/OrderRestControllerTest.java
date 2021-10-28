package kitchenpos.ui;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

import static kitchenpos.fixture.MenuFixture.양념_단품;
import static kitchenpos.fixture.MenuFixture.후라이드_단품;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("TableRestController 단위 테스트")
class OrderRestControllerTest extends ControllerTest {

    private OrderLineItem 후라이드치킨_2마리;
    private OrderLineItem 양념치킨_1마리;
    private OrderLineItem 후라이드치킨_2마리_주문1;
    private OrderLineItem 양념치킨_1마리_주문1;
    private OrderLineItem 양념치킨_1마리_주문2;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        // given
        후라이드치킨_2마리 = new OrderLineItem(후라이드_단품, 2);
        양념치킨_1마리 = new OrderLineItem(양념_단품, 1);

        Order 주문1 = new Order(1L);
        Order 주문2 = new Order(2L);

        후라이드치킨_2마리_주문1 = new OrderLineItem(1L, 주문1, 후라이드_단품, 2);
        양념치킨_1마리_주문1 = new OrderLineItem(2L, 주문1, 양념_단품, 1);
        양념치킨_1마리_주문2 = new OrderLineItem(3L, 주문2, 양념_단품, 1);
    }

    @Test
    @DisplayName("주문을 등록할 수 있다. - 해당 주문은 조리중(COOKING) 상태가 된다.")
    void create() throws Exception {
        // given
        Order order = new Order(1L, Arrays.asList(후라이드치킨_2마리, 양념치킨_1마리));
        Order expected = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), Arrays.asList(후라이드치킨_2마리_주문1, 양념치킨_1마리_주문1));
        given(orderService.create(any(Order.class))).willReturn(expected);

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
        Order order = new Order(1L, Collections.emptyList());
        willThrow(new IllegalArgumentException("주문하려면 하나 이상의 메뉴가 필요합니다."))
                .given(orderService).create(any(Order.class));

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
        OrderLineItem 간장치킨_1마리 = new OrderLineItem(new Menu(), 1);
        Order order = new Order(1L, Arrays.asList(후라이드치킨_2마리, 간장치킨_1마리));
        willThrow(new IllegalArgumentException("등록되지 않은 메뉴는 주문할 수 없습니다."))
                .given(orderService).create(any(Order.class));

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
        Order order = new Order(10L, Arrays.asList(후라이드치킨_2마리, 양념치킨_1마리));
        willThrow(new IllegalArgumentException("존재하지 않는 테이블은 주문할 수 없습니다."))
                .given(orderService).create(any(Order.class));

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
        Order order = new Order(1L, Arrays.asList(후라이드치킨_2마리, 양념치킨_1마리));
        willThrow(new IllegalArgumentException("빈 테이블은 주문할 수 없습니다."))
                .given(orderService).create(any(Order.class));

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
        Order order1 = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), Arrays.asList(후라이드치킨_2마리_주문1, 양념치킨_1마리_주문1));
        Order order2 = new Order(2L, 2L, OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.singletonList(양념치킨_1마리_주문2));
        List<Order> expected = Arrays.asList(order1, order2);
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
        Order changeStatusOrder = new Order(null, null, OrderStatus.COMPLETION.name(), null, null);
        Order expected = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Arrays.asList(후라이드치킨_2마리_주문1, 양념치킨_1마리_주문1));
        given(orderService.changeOrderStatus(anyLong(), any(Order.class))).willReturn(expected);

        // when
        ResultActions response = mockMvc.perform(put("/api/orders/1/order-status")
                .content(objectToJsonString(changeStatusOrder))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().json(objectToJsonString(expected)));
    }

    @Test
    @DisplayName("주문 상태를 변경하려면 주문은 존재해야 한다.")
    void changeOrderStatusWrongOrderNotExist() throws Exception {
        Order changeStatusOrder = new Order(null, null, OrderStatus.COMPLETION.name(), null, null);
        willThrow(new IllegalArgumentException("존재하지 않는 주문의 상태는 변경할 수 없습니다."))
                .given(orderService).changeOrderStatus(anyLong(), any(Order.class));

        // when
        ResultActions response = mockMvc.perform(put("/api/orders/11/order-status")
                .content(objectToJsonString(changeStatusOrder))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("존재하지 않는 주문의 상태는 변경할 수 없습니다."));
    }

    @Test
    @DisplayName("주문 상태를 변경하려면 주문 상태는 조리중(COOKING)이나 식사중(MEAL)이어야한다.")
    void changeOrderStatusWrongOrderStatus() throws Exception {
        Order changeStatusOrder = new Order(null, null, OrderStatus.MEAL.name(), null, null);
        willThrow(new IllegalArgumentException("계산 완료된 주문의 상태는 변경할 수 없습니다."))
                .given(orderService).changeOrderStatus(anyLong(), any(Order.class));

        // when
        ResultActions response = mockMvc.perform(put("/api/orders/1/order-status")
                .content(objectToJsonString(changeStatusOrder))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException))
                .andExpect(jsonPath("$.message").value("계산 완료된 주문의 상태는 변경할 수 없습니다."));
    }
}
