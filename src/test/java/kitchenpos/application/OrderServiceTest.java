package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixtures.후라이드치킨;
import static kitchenpos.application.fixture.MenuGroupFixtures.한마리메뉴;
import static kitchenpos.application.fixture.MenuProductFixtures.generateMenuProduct;
import static kitchenpos.application.fixture.OrderFixtures.generateOrder;
import static kitchenpos.application.fixture.OrderLineItemFixtures.*;
import static kitchenpos.application.fixture.OrderTableFixtures.generateOrderTable;
import static kitchenpos.application.fixture.OrderTableFixtures.테이블_1번;
import static kitchenpos.application.fixture.ProductFixtures.후라이드;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class OrderServiceTest {

    private final MenuGroupService menuGroupService;
    private final ProductService productService;
    private final MenuService menuService;
    private final TableService tableService;
    private final OrderService orderService;

    @Autowired
    public OrderServiceTest(final MenuGroupService menuGroupService, final ProductService productService,
                            final MenuService menuService, final TableService tableService,
                            final OrderService orderService) {
        this.menuGroupService = menuGroupService;
        this.productService = productService;
        this.menuService = menuService;
        this.tableService = tableService;
        this.orderService = orderService;
    }

    @DisplayName("order를 생성한다.")
    @Test
    void order를_생성한다() {
        MenuGroup menuGroup = menuGroupService.create(한마리메뉴());
        Menu menu = 후라이드치킨(menuGroup.getId());
        Product product = productService.create(후라이드());
        menu.setMenuProducts(List.of(generateMenuProduct(product.getId(), 1)));
        menu = menuService.create(menu);

        OrderTable orderTable = tableService.create(테이블_1번());
        tableService.changeEmpty(orderTable.getId(), generateOrderTable(0, false));

        OrderLineItem orderLineItem = generateOrderLineItem(menu.getId(), 1);

        Order actual = orderService.create(generateOrder(orderTable.getId(), List.of(orderLineItem)));

        assertAll(() -> {
            assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId());
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            assertThat(actual.getOrderLineItems()).hasSize(1);
        });
    }

    @DisplayName("orderLineItems가 비어있는 경우 예외를 던진다.")
    @Test
    void orderLineItems가_비어있는_경우_예외를_던진다() {
        OrderTable orderTable = tableService.create(테이블_1번());
        tableService.changeEmpty(orderTable.getId(), generateOrderTable(0, false));

        Order order = generateOrder(orderTable.getId(), List.of());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("orderLineItems의 사이즈가 실제 menu에 포함된 개수가 일치하지 않는 경우 예외를 던진다.")
    @Test
    void orderLineItems의_사이즈가_실제_menu에_포함된_개수가_일치하지_않는_경우_예외를_던진다() {
        MenuGroup menuGroup = menuGroupService.create(한마리메뉴());
        Menu menu = 후라이드치킨(menuGroup.getId());
        Product product = productService.create(후라이드());
        menu.setMenuProducts(List.of(generateMenuProduct(product.getId(), 1)));
        menu = menuService.create(menu);

        OrderTable orderTable = tableService.create(테이블_1번());
        tableService.changeEmpty(orderTable.getId(), generateOrderTable(0, false));

        OrderLineItem orderLineItem = generateOrderLineItem(menu.getId(), 1);

        assertThatThrownBy(
                () -> orderService.create(generateOrder(orderTable.getId(), List.of(orderLineItem, orderLineItem))))
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @DisplayName("orderTable이 비어있는 경우 예외를 던진다.")
    @Test
    void orderTable이_비어있는_경우_예외를_던진다() {
        MenuGroup menuGroup = menuGroupService.create(한마리메뉴());
        Menu menu = 후라이드치킨(menuGroup.getId());
        Product product = productService.create(후라이드());
        menu.setMenuProducts(List.of(generateMenuProduct(product.getId(), 1)));
        menu = menuService.create(menu);

        OrderLineItem orderLineItem = generateOrderLineItem(menu.getId(), 1);

        assertThatThrownBy(() -> orderService.create(generateOrder(0L, List.of(orderLineItem))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("order list를 조회한다.")
    @Test
    void order_list를_조회한다() {
        MenuGroup menuGroup = menuGroupService.create(한마리메뉴());
        Menu menu = 후라이드치킨(menuGroup.getId());
        Product product = productService.create(후라이드());
        menu.setMenuProducts(List.of(generateMenuProduct(product.getId(), 1)));
        menu = menuService.create(menu);

        OrderLineItem orderLineItem = generateOrderLineItem(menu.getId(), 1);

        OrderTable 테이블_1번 = tableService.create(테이블_1번());
        tableService.changeEmpty(테이블_1번.getId(), generateOrderTable(0, false));

        OrderTable 테이블_2번 = tableService.create(테이블_1번());
        tableService.changeEmpty(테이블_2번.getId(), generateOrderTable(0, false));

        orderService.create(generateOrder(테이블_1번.getId(), List.of(orderLineItem)));
        orderService.create(generateOrder(테이블_2번.getId(), List.of(orderLineItem)));

        List<Order> actual = orderService.list();

        assertThat(actual).hasSize(2);
    }

    @DisplayName("order의 상태를 변경한다.")
    @Test
    void order의_상태를_변경한다() {
        MenuGroup menuGroup = menuGroupService.create(한마리메뉴());
        Menu menu = 후라이드치킨(menuGroup.getId());
        Product product = productService.create(후라이드());
        menu.setMenuProducts(List.of(generateMenuProduct(product.getId(), 1)));
        menu = menuService.create(menu);

        OrderTable orderTable = tableService.create(테이블_1번());
        tableService.changeEmpty(orderTable.getId(), generateOrderTable(0, false));

        OrderLineItem orderLineItem = generateOrderLineItem(menu.getId(), 1);
        Order order = orderService.create(generateOrder(orderTable.getId(), List.of(orderLineItem)));

        Order changedOrder = generateOrder(orderTable.getId(), OrderStatus.MEAL, List.of(orderLineItem));

        Order actual = orderService.changeOrderStatus(order.getId(), changedOrder);

        assertAll(() -> {
            assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId());
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
            assertThat(actual.getOrderLineItems()).hasSize(1);
        });
    }

    @DisplayName("order의 상태가 COMPLETION인 경우 예외를 던진다.")
    @Test
    void order의_상태가_COMPLETION인_경우_예외를_던진다() {
        MenuGroup menuGroup = menuGroupService.create(한마리메뉴());
        Menu menu = 후라이드치킨(menuGroup.getId());
        Product product = productService.create(후라이드());
        menu.setMenuProducts(List.of(generateMenuProduct(product.getId(), 1)));
        menu = menuService.create(menu);

        OrderTable orderTable = tableService.create(테이블_1번());
        tableService.changeEmpty(orderTable.getId(), generateOrderTable(0, false));

        OrderLineItem orderLineItem = generateOrderLineItem(menu.getId(), 1);
        Order order = orderService.create(generateOrder(orderTable.getId(), List.of(orderLineItem)));

        Order changedOrder = generateOrder(orderTable.getId(), OrderStatus.COMPLETION, List.of(orderLineItem));

        Order actual = orderService.changeOrderStatus(order.getId(), changedOrder);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), actual))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
