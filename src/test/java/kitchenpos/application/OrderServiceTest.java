package kitchenpos.application;

import static kitchenpos.fixture.MenuFactory.menu;
import static kitchenpos.fixture.MenuGroupFactory.menuGroup;
import static kitchenpos.fixture.OrderFactory.order;
import static kitchenpos.fixture.OrderRequestFactory.orderRequestFrom;
import static kitchenpos.fixture.OrderTableFactory.notEmptyTable;
import static kitchenpos.fixture.ProductFactory.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderChangeStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends FakeSpringContext {

    private final OrderService orderService = new OrderService(
            menuDao, orderTables, orders);

    @DisplayName("주문 등록")
    @Test
    void create() {
        final var pizza = productDao.save(product("피자", 10_000));
        final var coke = productDao.save(product("콜라", 1_000));

        final var italian = menuGroupDao.save(menuGroup("양식"));

        final var pizzaMenu = menuDao.save(menu("피자파티", italian, List.of(pizza)));
        final var cokeMenu = menuDao.save(menu("콜라파티", italian, List.of(coke)));

        final var table = orderTableDao.save(notEmptyTable(2));

        final var order = order(table, pizzaMenu, cokeMenu);
        final var request = orderRequestFrom(order);

        final var result = orderService.create(request);
        assertAll(
                () -> assertThat(result.getOrderTableId()).isEqualTo(order.getOrderTableId()),
                () -> assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING)
        );
    }

    @DisplayName("주문 항목의 메뉴가 중복된다면 등록 시 예외 발생")
    @Test
    void create_duplicatedMenuInOrderLineItems_throwsException() {
        final var pizza = productDao.save(product("피자", 10_000));
        final var italian = menuGroupDao.save(menuGroup("양식"));
        final var pizzaMenu = menuDao.save(menu("피자파티", italian, List.of(pizza)));

        final var table = orderTableDao.save(notEmptyTable(2));

        final var order = order(table, pizzaMenu, pizzaMenu);
        final var request = orderRequestFrom(order);

        assertThatThrownBy(
                () -> orderService.create(request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        final var pizza = productDao.save(product("피자", 10_000));
        final var italian = menuGroupDao.save(menuGroup("양식"));
        final var pizzaMenu = menuDao.save(menu("피자파티", italian, List.of(pizza)));

        final var table = orderTableDao.save(notEmptyTable(2));

        final var order = createThenSaveOrderAndRelateds(pizzaMenu, table);

        final var request = new OrderChangeStatusRequest(OrderStatus.MEAL);
        final var result = orderService.changeOrderStatus(order.getId(), request);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(order.getId()),
                () -> assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL)
        );
    }

    @DisplayName("주문 상태가 COMPLETION이라면, 주문 상태 변경 시 예외 발생")
    @Test
    void changeOrderStatus_orderStatusIsCompletion_throwsException() {
        final var pizza = productDao.save(product("피자", 10_000));
        final var italian = menuGroupDao.save(menuGroup("양식"));
        final var pizzaMenu = menuDao.save(menu("피자파티", italian, List.of(pizza)));

        final var table = orderTableDao.save(notEmptyTable(2));

        final var order = orderDao.save(order(table, OrderStatus.COMPLETION, pizzaMenu));

        final var request = new OrderChangeStatusRequest(OrderStatus.MEAL);

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(order.getId(), request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private Order createThenSaveOrderAndRelateds(final Menu pizzaMenu, final OrderTable table) {
        final var order = orderDao.save(order(table, pizzaMenu));
        final var orderLineItems = order.getOrderLineItems();
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrderId(order.getId());
            orderLineItemDao.save(orderLineItem);

        }
        return order;
    }
}
