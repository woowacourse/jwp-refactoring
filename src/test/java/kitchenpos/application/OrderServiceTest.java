package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private ProductService productService;


    @Test
    @DisplayName("주문을 등록할 수 있다.")
    void create() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 2);
        final OrderTable savedTable = tableService.create(orderTable);

        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 = productService.create(product1);
        final Product savedProduct2 = productService.create(product2);

        final MenuGroup menuGroup1 = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup1 = menuGroupService.create(menuGroup1);

        final Menu menu1 = MenuFixture.createWithPrice(savedMenuGroup1, 2000L, savedProduct1, savedProduct2);
        final Menu savedMenu1 = menuService.create(menu1);

        final OrderLineItem orderLineItem1 = OrderLineItemFixture.create(savedMenu1);
        final Order order = OrderFixture.create(savedTable, OrderStatus.COMPLETION, orderLineItem1);

        // when
        final Order actual = orderService.create(order);

        // then
        assertAll(
                () -> assertThat(actual)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "orderLineItems")
                        .isEqualTo(order),
                () -> assertThat(actual.getOrderLineItems())
                        .usingElementComparatorIgnoringFields("seq")
                        .containsExactly(orderLineItem1)
        );
    }

    @Test
    @DisplayName("주문하려는 주문 테이블이 등록되어 있어야 한다.")
    void create_exceptionOrderTableNotExists() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 2);
        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 = productService.create(product1);
        final Product savedProduct2 = productService.create(product2);

        final MenuGroup menuGroup1 = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup1 = menuGroupService.create(menuGroup1);

        final Menu menu1 = MenuFixture.createWithPrice(savedMenuGroup1, 2000L, savedProduct1, savedProduct2);
        final Menu savedMenu1 = menuService.create(menu1);

        final OrderLineItem orderLineItem1 = OrderLineItemFixture.create(savedMenu1);
        final Order order = OrderFixture.create(orderTable, OrderStatus.COMPLETION, orderLineItem1);

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문하려는 주문 테이블이 비어있으면 안된다.")
    void create_exceptionOrderTableIsEmpty() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(true, 2);
        final OrderTable savedTable = tableService.create(orderTable);

        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 = productService.create(product1);
        final Product savedProduct2 = productService.create(product2);

        final MenuGroup menuGroup1 = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup1 = menuGroupService.create(menuGroup1);

        final Menu menu = MenuFixture.createWithPrice(savedMenuGroup1, 2000L, savedProduct1, savedProduct2);
        final Menu savedMenu = menuService.create(menu);
        final OrderLineItem orderLineItem = OrderLineItemFixture.create(savedMenu);
        final Order order = OrderFixture.create(savedTable, OrderStatus.COMPLETION, orderLineItem);

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목이 0개면 안된다.")
    void create_exceptionOrderLIneItemZero() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 2);
        final OrderTable savedTable = tableService.create(orderTable);

        final Order order = OrderFixture.create(savedTable, OrderStatus.COMPLETION);

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목에 해당하는 메뉴들이 모두 등록되어 있어야 한다.")
    void create_exceptionNotCreatedMenu() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 2);
        final OrderTable savedTable = tableService.create(orderTable);

        final Order order = OrderFixture.create(savedTable, OrderStatus.COMPLETION);

        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 = productService.create(product1);
        final Product savedProduct2 = productService.create(product2);

        final MenuGroup menuGroup1 = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup1 = menuGroupService.create(menuGroup1);

        final Menu menu = MenuFixture.createWithPrice(savedMenuGroup1, 2000L, savedProduct1, savedProduct2);

        final OrderLineItem orderLineItem = OrderLineItemFixture.create(menu);
        OrderFixture.create(savedTable, OrderStatus.COMPLETION, orderLineItem);

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목이 중복되는 메뉴를 가지면 안된다.")
    void create_exceptionDuplicationMenu() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 2);
        final OrderTable savedTable = tableService.create(orderTable);

        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 = productService.create(product1);
        final Product savedProduct2 = productService.create(product2);

        final MenuGroup menuGroup1 = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup1 = menuGroupService.create(menuGroup1);

        final Menu menu = MenuFixture.createWithPrice(savedMenuGroup1, 2000L, savedProduct1, savedProduct2);
        final Menu savedMenu = menuService.create(menu);

        final OrderLineItem orderLineItem1 = OrderLineItemFixture.create(savedMenu);
        final OrderLineItem orderLineItem2 = OrderLineItemFixture.create(savedMenu);

        final Order order = OrderFixture.create(savedTable, OrderStatus.COMPLETION, orderLineItem1, orderLineItem2);

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 2);
        final OrderTable savedTable = tableService.create(orderTable);

        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 = productService.create(product1);
        final Product savedProduct2 = productService.create(product2);

        final MenuGroup menuGroup1 = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup1 = menuGroupService.create(menuGroup1);

        final Menu menu1 = MenuFixture.createWithPrice(savedMenuGroup1, 2000L, savedProduct1, savedProduct2);
        final Menu savedMenu1 = menuService.create(menu1);

        final OrderLineItem orderLineItem1 = OrderLineItemFixture.create(savedMenu1);
        final Order order = OrderFixture.create(savedTable, OrderStatus.COMPLETION, orderLineItem1);

        final Order savedOrder = orderService.create(order);

        // when
        final List<Order> actual = orderService.list();

        // then
        assertThat(actual).usingRecursiveFieldByFieldElementComparator()
                .contains(savedOrder);
    }

    @Test
    @DisplayName("특정 주문의 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 2);
        final OrderTable savedTable = tableService.create(orderTable);

        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 = productService.create(product1);
        final Product savedProduct2 = productService.create(product2);

        final MenuGroup menuGroup1 = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup1 = menuGroupService.create(menuGroup1);

        final Menu menu1 = MenuFixture.createWithPrice(savedMenuGroup1, 2000L, savedProduct1, savedProduct2);
        final Menu savedMenu1 = menuService.create(menu1);

        final OrderLineItem orderLineItem1 = OrderLineItemFixture.create(savedMenu1);
        final Order order = OrderFixture.create(savedTable, OrderStatus.COMPLETION, orderLineItem1);

        final Order savedOrder = orderService.create(order);

        // when
        final Order actual = orderService.changeOrderStatus(savedOrder.getId(),
                OrderFixture.create(null, OrderStatus.COOKING));

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("특정 주문의 상태가 완료 상태면 변경할 수 없다.")
    void changeOrderStatus_exceptionChangeToCompletion() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 2);
        final OrderTable savedTable = tableService.create(orderTable);

        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 = productService.create(product1);
        final Product savedProduct2 = productService.create(product2);

        final MenuGroup menuGroup1 = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup1 = menuGroupService.create(menuGroup1);

        final Menu menu1 = MenuFixture.createWithPrice(savedMenuGroup1, 2000L, savedProduct1, savedProduct2);
        final Menu savedMenu1 = menuService.create(menu1);

        final OrderLineItem orderLineItem1 = OrderLineItemFixture.create(savedMenu1);
        final Order order = OrderFixture.create(savedTable, OrderStatus.COOKING, orderLineItem1);

        final Order savedOrder = orderService.create(order);
        orderService.changeOrderStatus(savedOrder.getId(),
                OrderFixture.create(null, OrderStatus.COMPLETION));

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(),
                OrderFixture.create(null, OrderStatus.COOKING)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되지 않은 특정 주문의 상태를 변경할 수 없다.")
    void changeOrderStatus_exceptionNotExistsOrder() {
        // given
        final Long notExistsId = Long.MAX_VALUE;

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(notExistsId,
                OrderFixture.create(null, OrderStatus.COOKING)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }


}
