package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("주문을 생성한다")
    void create() {
        final OrderTable orderTable = tableService.create(new OrderTable());
        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        final OrderLineItem orderLineItem = generateOrderLineItem(1L, 1);
        orderLineItems.add(orderLineItem);
        order.setOrderLineItems(orderLineItems);

        final Order createOrder = orderService.create(order);

        assertThat(createOrder.getOrderTableId())
                .isEqualTo(orderTable.getId());
    }

    @Test
    @DisplayName("메뉴가 없는 주문을 생성하면 예외를 반환한다")
    void create_notHaveMenuException() {
        final OrderTable orderTable = tableService.create(new OrderTable());
        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴로 주문을 생성하면 예외를 반환한다")
    void create_notExistMenuException() {
        final OrderTable orderTable = tableService.create(new OrderTable());
        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        final OrderLineItem orderLineItem = generateOrderLineItem(999999999L, 1);
        orderLineItems.add(orderLineItem);
        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 전체를 조회한다")
    void list() {
        final OrderTable orderTable = tableService.create(new OrderTable());
        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        final OrderLineItem orderLineItem = generateOrderLineItem(1L, 1);
        orderLineItems.add(orderLineItem);
        order.setOrderLineItems(orderLineItems);

        orderService.create(order);

        assertThat(orderService.list())
                .hasSizeGreaterThan(0);
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void changeOrderStatus() {
        final OrderTable orderTable = tableService.create(new OrderTable());

        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        final OrderLineItem orderLineItem = generateOrderLineItem(1L, 1);
        orderLineItems.add(orderLineItem);
        order.setOrderLineItems(orderLineItems);
        final Order createOrder = orderService.create(order);

        final Order order2 = new Order();
        order2.setOrderStatus(OrderStatus.MEAL.name());

        final Order actual = orderService.changeOrderStatus(createOrder.getId(), order2);

        assertThat(actual.getOrderStatus())
                .isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("`계산 완료`인 주문 상태를 변경하면 예외를 반환한다")
    void changeOrderStatus_completionException() {
        final OrderTable orderTable = tableService.create(new OrderTable());

        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        final OrderLineItem orderLineItem = generateOrderLineItem(1L, 2);
        orderLineItems.add(orderLineItem);
        order.setOrderLineItems(orderLineItems);
        orderService.create(order);
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        final Order order2 = new Order();
        order2.setOrderStatus(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderLineItem generateOrderLineItem(final Long menuId, final int quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
