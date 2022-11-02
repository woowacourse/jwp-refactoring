package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.support.application.ServiceTestEnvironment;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.exception.OrderLineItemMenuException;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.support.fixture.MenuFixture;
import kitchenpos.support.fixture.MenuGroupFixture;
import kitchenpos.support.fixture.OrderFixture;
import kitchenpos.support.fixture.OrderLineItemFixture;
import kitchenpos.support.fixture.OrderTableFixture;
import kitchenpos.support.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTestEnvironment {

    @Autowired
    private OrderService orderService;


    @Test
    @DisplayName("주문을 등록할 수 있다.")
    void create() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 2);
        final OrderTable savedTable = serviceDependencies.save(orderTable);

        final Menu savedMenu1 = saveValidMenu();

        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedTable.getId(),
                Collections.singletonList(
                        new OrderLineItemCreateRequest(savedMenu1.getId(), 1L)
                ));

        // when
        final Order actual = orderService.create(orderCreateRequest);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(actual.getOrderedTime()).isNotNull(),
                () -> assertThat(actual.getOrderTable()).usingRecursiveComparison()
                        .ignoringFields("order").isEqualTo(savedTable),
                () -> assertThat(actual.getOrderLineItems()).hasSize(1)
        );
    }

    @Test
    @DisplayName("주문하려는 주문 테이블이 등록되어 있어야 한다.")
    void create_exceptionOrderTableNotExists() {
        // given
        final Menu savedMenu = saveValidMenu();

        OrderCreateRequest request = new OrderCreateRequest(-1L,
                Collections.singletonList(new OrderLineItemCreateRequest(savedMenu.getId(), 1L)));

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isExactlyInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    @DisplayName("주문 항목에 해당하는 메뉴들이 모두 등록되어 있어야 한다.")
    void create_exceptionNotCreatedMenu() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 2);
        final OrderTable savedTable = serviceDependencies.save(orderTable);
        saveValidMenu();
        final OrderCreateRequest request = new OrderCreateRequest(savedTable.getId(),
                Arrays.asList(new OrderLineItemCreateRequest(-1L, 1L),
                        new OrderLineItemCreateRequest(-1L, 1L)));

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isExactlyInstanceOf(OrderLineItemMenuException.class);
    }

    @Test
    @DisplayName("주문 항목이 중복되는 메뉴를 가지면 안된다.")
    void create_exceptionDuplicationMenu() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 2);
        final OrderTable savedTable = serviceDependencies.save(orderTable);
        final Menu savedMenu = saveValidMenu();
        final OrderCreateRequest request = new OrderCreateRequest(savedTable.getId(), Arrays.asList(
                new OrderLineItemCreateRequest(savedMenu.getId(), 1L),
                new OrderLineItemCreateRequest(savedMenu.getId(), 1L)));

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isExactlyInstanceOf(OrderLineItemMenuException.class);
    }

    @Test
    @DisplayName("등록된 주문을 조회할 수 있다.")
    void list() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 2);
        final OrderTable savedTable = serviceDependencies.save(orderTable);
        final Menu savedMenu = saveValidMenu();
        final OrderLineItem orderLineItem = OrderLineItemFixture.create(savedMenu.getId());
        final Order order = OrderFixture.create(savedTable, OrderStatus.COMPLETION, orderLineItem);
        final Order savedOrder = serviceDependencies.save(order);

        // when
        final List<Order> actual = orderService.list();

        // then
        assertThat(actual).usingRecursiveFieldByFieldElementComparator()
                .extracting("id")
                .contains(savedOrder.getId());
    }

    @Test
    @DisplayName("등록되지 않은 특정 주문의 상태를 변경할 수 없다.")
    void changeOrderStatus_exceptionNotExistsOrder() {
        // given
        final Long notExistsId = Long.MAX_VALUE;

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(notExistsId,
                new OrderStatusRequest(OrderStatus.COOKING.name())))
                .isExactlyInstanceOf(OrderNotFoundException.class);
    }

    private Menu saveValidMenu() {
        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 = serviceDependencies.save(product1);
        final Product savedProduct2 = serviceDependencies.save(product2);

        final MenuGroup menuGroup1 = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup1 = serviceDependencies.save(menuGroup1);

        final Menu menu = MenuFixture.createWithPrice(savedMenuGroup1.getId(), 2000L, savedProduct1, savedProduct2);
        return serviceDependencies.save(menu);
    }
}
