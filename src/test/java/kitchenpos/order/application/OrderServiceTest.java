package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.exception.OrderLineItemMenuException;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.exception.OrderTableEmptyException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Price;
import kitchenpos.order.OrderPrice;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.support.application.ServiceTestEnvironment;
import kitchenpos.support.fixture.MenuFixture;
import kitchenpos.support.fixture.MenuGroupFixture;
import kitchenpos.support.fixture.OrderFixture;
import kitchenpos.support.fixture.OrderLineItemFixture;
import kitchenpos.support.fixture.OrderTableFixture;
import kitchenpos.support.fixture.ProductFixture;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.util.BigDecimalComparator;
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
                () -> assertThat(actual.getOrderTableId()).isEqualTo(savedTable.getId()),
                () -> assertThat(actual.getOrderLineItems()).hasSize(1)
        );
    }

    @Test
    @DisplayName("주문에 등록된 메뉴 정보가 메뉴를 수정해도 변경되지 않는다.")
    void create_menuUpdatedNotChangeOrderLineItem() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 2);
        final OrderTable savedTable = serviceDependencies.save(orderTable);

        final Menu savedMenu1 = saveValidMenu();

        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedTable.getId(),
                Collections.singletonList(new OrderLineItemCreateRequest(savedMenu1.getId(), 1L)));
        final Order order = orderService.create(orderCreateRequest);

        savedMenu1.setName("changedName");
        savedMenu1.setPrice(new Price(3000000L));
        serviceDependencies.save(savedMenu1);

        // when
        final List<OrderLineItem> actual = order.getOrderLineItems();

        // then
        assertAll(
                () -> assertThat(actual).extracting("name").containsOnly("name"),
                () -> assertThat(actual)
                        .extracting("price")
                        .usingComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                        .containsOnly(BigDecimal.valueOf(2000L))
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
    @DisplayName("주문하려는 주문 테이블이 비어있으면 예외가 발생한다.")
    void create_exceptionOrderTableEmpty() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(true, 2);
        final OrderTable savedTable = serviceDependencies.save(orderTable);
        final Menu savedMenu = saveValidMenu();

        OrderCreateRequest request = new OrderCreateRequest(savedTable.getId(),
                Collections.singletonList(new OrderLineItemCreateRequest(savedMenu.getId(), 1L)));

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isExactlyInstanceOf(OrderTableEmptyException.class);
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
        final OrderLineItem orderLineItem = OrderLineItemFixture.create(savedMenu.getName(),
                new OrderPrice(savedMenu.getPrice()));
        final Order order = OrderFixture.create(savedTable.getId(), OrderStatus.COMPLETION, orderLineItem);
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

        final Menu menu = MenuFixture.createWithPrice(savedMenuGroup1.getId(), 2000L,
                Arrays.asList(savedProduct1.getId(), savedProduct2.getId()),
                Arrays.asList(1000L, 1000L));
        return serviceDependencies.save(menu);
    }
}
