package kitchenpos.ui;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.builder.OrderBuilder;
import kitchenpos.builder.OrderLineItemBuilder;
import kitchenpos.ui.dto.order.OrderCreateRequest;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderRestControllerTest extends BaseWebMvcTest {

    OrderLineItem orderLineItem1;
    OrderLineItem orderLineItem2;
    OrderLineItem orderLineItem3;
    Order order1;
    Order order2;

    @BeforeEach
    void setUp() {
        orderLineItem1 = new OrderLineItemBuilder()
                .seq(1L)
                .order(null)
                .menu(new Menu(1L))
                .quantity(1L)
                .build();

        order1 = new OrderBuilder()
                .id(1L)
                .orderTableId(1L)
                .orderStatus("COOKING")
                .orderedTime(LocalDateTime.now())
                .orderLineItems(Arrays.asList(orderLineItem1))
                .build();
        orderLineItem1.connectOrder(order1);

        orderLineItem2 = new OrderLineItemBuilder()
                .seq(2L)
                .order(null)
                .menu(new Menu(1L))
                .quantity(1L)
                .build();

        orderLineItem3 = new OrderLineItemBuilder()
                .seq(3L)
                .order(null)
                .menu(new Menu(2L))
                .quantity(3L)
                .build();

        order2 = new OrderBuilder()
                .id(2L)
                .orderTableId(2L)
                .orderStatus("COOKING")
                .orderedTime(LocalDateTime.now())
                .orderLineItems(Arrays.asList(orderLineItem2, orderLineItem3))
                .build();
        orderLineItem2.connectOrder(order2);
        orderLineItem3.connectOrder(order2);
    }

    @DisplayName("POST /api/orders -> 주문을 추가한다.")
    @Test
    void create() throws Exception {

        // given
        given(orderService.create(any(Order.class)))
                .willReturn(order1);

        OrderLineItemRequest requestOrderLineItem = new OrderLineItemRequest(1L, 1L);
        OrderCreateRequest requestOrder = new OrderCreateRequest(1L, Arrays.asList(requestOrderLineItem));
        String content = parseJson(requestOrder);

        // when
        ResultActions actions = mvc.perform(postRequest("/api/orders", content));

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/orders/1"))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.orderTableId", is(1)))
                .andExpect(jsonPath("$.orderStatus", is("COOKING")))
                .andExpect(jsonPath("$.orderedTime", is(order1.getOrderedTime().toString())))
                .andExpect(jsonPath("$.orderLineItems[0].seq", is(1)))
                .andExpect(jsonPath("$.orderLineItems[0].orderId", is(1)))
                .andExpect(jsonPath("$.orderLineItems[0].menuId", is(1)))
                .andExpect(jsonPath("$.orderLineItems[0].quantity", is(1)))
                .andDo(print());
    }

    @DisplayName("GET /api/orders -> 주문 전체를 조회한다.")
    @Test
    void list() throws Exception {

        // given
        given(orderService.list())
                .willReturn(Arrays.asList(order1, order2));

        // when
        ResultActions actions = mvc.perform(getRequest("/api/orders"));

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].orderTableId", is(1)))
                .andExpect(jsonPath("$[0].orderStatus", is("COOKING")))
                .andExpect(jsonPath("$[0].orderLineItems[0].seq", is(1)))
                .andExpect(jsonPath("$[0].orderLineItems[0].orderId", is(1)))
                .andExpect(jsonPath("$[0].orderLineItems[0].menuId", is(1)))
                .andExpect(jsonPath("$[0].orderLineItems[0].quantity", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].orderTableId", is(2)))
                .andExpect(jsonPath("$[1].orderStatus", is("COOKING")))
                .andExpect(jsonPath("$[1].orderLineItems[0].seq", is(2)))
                .andExpect(jsonPath("$[1].orderLineItems[0].orderId", is(2)))
                .andExpect(jsonPath("$[1].orderLineItems[0].menuId", is(1)))
                .andExpect(jsonPath("$[1].orderLineItems[0].quantity", is(1)))
                .andExpect(jsonPath("$[1].orderLineItems[1].seq", is(3)))
                .andExpect(jsonPath("$[1].orderLineItems[1].orderId", is(2)))
                .andExpect(jsonPath("$[1].orderLineItems[1].menuId", is(2)))
                .andExpect(jsonPath("$[1].orderLineItems[1].quantity", is(3)))
                .andDo(print());
    }

    @DisplayName("PUT /api/orders/{orderId}/order-status -> 주문의 상태를 식사중 또는 완료(식사 끝)으로 변경한다.")
    @Test
    void changeOrderStatus() throws Exception {
        // given
        Order changedOrder = new OrderBuilder()
                .id(1L)
                .orderTableId(1L)
                .orderStatus("MEAL")
                .orderedTime(LocalDateTime.now())
                .orderLineItems(Arrays.asList(orderLineItem1))
                .build();
        given(orderService.changeOrderStatus(any(Long.class), any(Order.class)))
                .willReturn(changedOrder);

        Order requestOrder = new OrderBuilder()
                .id(null)
                .orderTableId(null)
                .orderStatus("MEAL")
                .orderedTime(null)
                .orderLineItems(null)
                .build();
        String content = parseJson(requestOrder);
        Long requestOrderId = 1L;

        // when
        ResultActions actions = mvc.perform(putRequest("/api/orders/1/order-status", content, requestOrderId))
                .andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus", is("MEAL")))
                .andDo(print());
    }
}