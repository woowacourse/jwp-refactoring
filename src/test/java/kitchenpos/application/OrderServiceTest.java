package kitchenpos.application;

import static kitchenpos.support.OrderTableFixtures.ORDER_TABLE_NOT_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("OrderLineItems이 비어있는 경우 예외가 발생한다.")
    @Test
    void create_orderLineItemsEmpty_throwsException() {
        // given
        final OrderRequest orderRequest = new OrderRequest(9L, Collections.emptyList());

        // when
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderLineItem의 menuId가 존재하지 않는 id면 예외가 발생한다.")
    @Test
    void create_notExistMenu_throwsException() {
        // given
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(8L, 3);
        final OrderRequest orderRequest = new OrderRequest(9L, Arrays.asList(orderLineItemRequest));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderLineItem의 OrderTable이 empty이면 예외가 발생한다.")
    @Test
    void create_alreadyExistOrderTable_throwsException() {
        // given
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 3);
        final OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(orderLineItemRequest));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order를 등록한다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = orderTableDao.save(ORDER_TABLE_NOT_EMPTY.createWithIdNull());
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 3);
        final OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Arrays.asList(orderLineItemRequest));

        // when
        final Order savedOrder = orderService.create(orderRequest);

        // then
        assertThat(savedOrder.getId()).isNotNull();
    }

    @DisplayName("Order를 조회한다.")
    @Test
    void list() {
        // given
        final OrderTable orderTable = orderTableDao.save(ORDER_TABLE_NOT_EMPTY.createWithIdNull());
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 3);
        final OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Arrays.asList(orderLineItemRequest));

        final Order savedOrder = orderService.create(orderRequest);

        // when
        final List<Order> orders = orderService.list();

        // then
        assertThat(orders).size().isEqualTo(1);
    }

    @DisplayName("OrderId가 존재하지 않으면 예외가 발생한다.")
    @Test
    void changeOrderStatus_notExistOrder_throwsException() {
        // given
        final OrderTable orderTable = orderTableDao.save(ORDER_TABLE_NOT_EMPTY.createWithIdNull());
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 3);
        final Order order = new Order(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now());
        final OrderRequest request = new OrderRequest(OrderStatus.COOKING.name());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order의 상태가 Completion이면 예외가 발생한다.")
    @Test
    void changeOrderStatus_orderStatusCompletion_throwsException() {
        // given
        final OrderTable orderTable = orderTableDao.save(ORDER_TABLE_NOT_EMPTY.createWithIdNull());
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 3);
        final Order order = new Order(null, orderTable, OrderStatus.COMPLETION.name(), LocalDateTime.now(),
                Arrays.asList(orderLineItem));
        final Order savedOrder = orderDao.save(order);

        final OrderRequest request = new OrderRequest(OrderStatus.COOKING.name());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order의 상태를 Cooking에서 meal로 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        final OrderTable orderTable = orderTableDao.save(ORDER_TABLE_NOT_EMPTY.createWithIdNull());
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 3);
        final Order order = new Order(null, orderTable, OrderStatus.COOKING.name(), LocalDateTime.now(),
                Arrays.asList(orderLineItem));
        final Order savedOrder = orderDao.save(order);
        final OrderRequest request = new OrderRequest(OrderStatus.MEAL.name());

        // when
        final Order updatedOrder = orderService.changeOrderStatus(savedOrder.getId(), request);

        // then
        assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }
}
