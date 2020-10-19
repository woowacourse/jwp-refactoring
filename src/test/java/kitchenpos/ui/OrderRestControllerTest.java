package kitchenpos.ui;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import kitchenpos.application.OrderService;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

class OrderRestControllerTest extends ControllerTest {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderTableDao orderTableDao;

    @DisplayName("create: 테이블, 주문 라인 목록과 함께 주문 추가 요청을 한다. 새 주문 생성 성공 후 201 응답을 반환한다.")
    @Test
    void create() throws Exception {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(3);

        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);
        final OrderTable savedTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        final String createOrderApiUrl = "/api/orders";
        final ResultActions resultActions = create(createOrderApiUrl, order);

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.orderTableId", notNullValue()))
                .andExpect(jsonPath("$.orderStatus", is("COOKING")))
                .andExpect(jsonPath("$.orderedTime", notNullValue()))
                .andExpect(jsonPath("$.orderLineItems", hasSize(1)));
    }

    @DisplayName("list: 전체 주문 목록 조회 요청시, 200 상태 코드와 함께, 전체 주문 내역을 반환한다.")
    @Test
    void list() throws Exception {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(3);

        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);
        final OrderTable savedTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        orderService.create(order);

        final String findOrdersApiUrl = "/api/orders";
        final ResultActions resultActions = findList(findOrdersApiUrl);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @DisplayName("changeOrderStatus: 요리 완료 상태가 아닌 경우, 주문 현재 진행 상태 변경 요청시 변경 후, 200 상태코드와, 변경한 주문 내용을 반환한다.")
    @Test
    void changeOrderStatus() throws Exception {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(3);

        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(5);
        final OrderTable savedTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderTableId(savedTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        final Order savedOrder = orderService.create(order);
        final Long savedOrderId = savedOrder.getId();

        final Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.MEAL.name());

        String updateOrderStatusApiUrl = "/api/orders/{orderId}/order-status";
        final ResultActions resultActions = updateByPathIdAndBody(updateOrderStatusApiUrl, savedOrderId, newOrder);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.orderTableId", notNullValue()))
                .andExpect(jsonPath("$.orderStatus", is("MEAL")))
                .andExpect(jsonPath("$.orderedTime", notNullValue()))
                .andExpect(jsonPath("$.orderLineItems", hasSize(1)));

    }
}