package kitchenpos.application;

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

import java.util.List;

import static kitchenpos.fixture.MenuFixtures.후라이드치킨;
import static kitchenpos.fixture.MenuGroupFixtures.한마리메뉴;
import static kitchenpos.fixture.MenuProductFixtures.createMenuProduct;
import static kitchenpos.fixture.OrderFixtures.createOrder;
import static kitchenpos.fixture.OrderLineItemFixtures.createOrderLineItem;
import static kitchenpos.fixture.OrderTableFixtures.createOrderTable;
import static kitchenpos.fixture.OrderTableFixtures.테이블_1번;
import static kitchenpos.fixture.OrderTableFixtures.테이블_2번;
import static kitchenpos.fixture.ProductFixtures.후라이드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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
    void createOrderSuccess() {
        // 메뉴 설정
        Product 후라이드 = productService.create(후라이드());
        MenuGroup 한마리메뉴 = menuGroupService.create(한마리메뉴());
        Menu 메뉴_후라이드치킨 = 후라이드치킨(한마리메뉴.getId());
        메뉴_후라이드치킨.setMenuProducts(List.of(createMenuProduct(후라이드.getId(), 1)));
        메뉴_후라이드치킨 = menuService.create(메뉴_후라이드치킨);

        // 1번테이블, 0명
        OrderTable orderTable = tableService.create(테이블_1번());
        tableService.changeEmpty(orderTable.getId(), createOrderTable(0, false));

        // 주문: 1번 테이블 / 후라이드 치킨 1개
        OrderLineItem orderLineItem = createOrderLineItem(메뉴_후라이드치킨.getId(), 1);
        Order actual = orderService.create(createOrder(orderTable.getId(), List.of(orderLineItem)));

        assertAll(
                () -> assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId()),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(actual.getOrderLineItems()).hasSize(1)
        );
    }

    @DisplayName("orderLineItems가 비어있는 경우 예외를 던진다.")
    @Test
    void createOrderByorderLineItemsIsEmpty() {
        OrderTable orderTable = tableService.create(테이블_1번());
        Order order = createOrder(orderTable.getId(), List.of());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("orderLineItems의 사이즈와 menu의 개수가 다르면 예외를 던진다.")
    @Test
    void createOrderByNotEqualSize() {
        // 메뉴 설정
        Product 후라이드 = productService.create(후라이드());
        MenuGroup 한마리메뉴 = menuGroupService.create(한마리메뉴());
        Menu 메뉴_후라이드치킨 = 후라이드치킨(한마리메뉴.getId());
        메뉴_후라이드치킨.setMenuProducts(List.of(createMenuProduct(후라이드.getId(), 1)));
        메뉴_후라이드치킨 = menuService.create(메뉴_후라이드치킨);

        // 1번테이블, 0명
        OrderTable orderTable = tableService.create(테이블_1번());
        tableService.changeEmpty(orderTable.getId(), createOrderTable(0, false));

        // 주문할 메뉴
        OrderLineItem orderLineItem = createOrderLineItem(메뉴_후라이드치킨.getId(), 1);

        // 다른 개수
        assertThatThrownBy(() -> orderService.create(createOrder(
                orderTable.getId(),
                List.of(orderLineItem, orderLineItem)
        )))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("orderTable이 비어있는 경우 예외를 던진다.")
    @Test
    void createOrderByOrderTableIsEmpty() {
        // 메뉴 설정
        Product 후라이드 = productService.create(후라이드());
        MenuGroup 한마리메뉴 = menuGroupService.create(한마리메뉴());
        Menu 메뉴_후라이드치킨 = 후라이드치킨(한마리메뉴.getId());
        메뉴_후라이드치킨.setMenuProducts(List.of(createMenuProduct(후라이드.getId(), 1)));
        메뉴_후라이드치킨 = menuService.create(메뉴_후라이드치킨);

        // 주문할 메뉴
        OrderLineItem orderLineItem = createOrderLineItem(메뉴_후라이드치킨.getId(), 1);

        // 없는 테이블이 주문
        assertThatThrownBy(() -> orderService.create(createOrder(0L, List.of(orderLineItem))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("order 전체를 조회한다.")
    @Test
    void findAllOrder() {
        // 메뉴 설정
        Product 후라이드 = productService.create(후라이드());
        MenuGroup 한마리메뉴 = menuGroupService.create(한마리메뉴());
        Menu 메뉴_후라이드치킨 = 후라이드치킨(한마리메뉴.getId());
        메뉴_후라이드치킨.setMenuProducts(List.of(createMenuProduct(후라이드.getId(), 2)));
        메뉴_후라이드치킨 = menuService.create(메뉴_후라이드치킨);

        // 테이블 설정
        OrderTable 테이블_1번 = tableService.create(테이블_1번());
        tableService.changeEmpty(테이블_1번.getId(), createOrderTable(0, false));
        OrderTable 테이블_2번 = tableService.create(테이블_2번());
        tableService.changeEmpty(테이블_2번.getId(), createOrderTable(0, false));

        // 주문할 메뉴
        OrderLineItem orderLineItem = createOrderLineItem(메뉴_후라이드치킨.getId(), 1);

        // 주문
        orderService.create(createOrder(테이블_1번.getId(), List.of(orderLineItem)));
        orderService.create(createOrder(테이블_2번.getId(), List.of(orderLineItem)));

        List<Order> orders = orderService.list();
        assertThat(orders).hasSize(2);
    }

    @DisplayName("order의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // 메뉴 설정
        Product 후라이드 = productService.create(후라이드());
        MenuGroup 한마리메뉴 = menuGroupService.create(한마리메뉴());
        Menu 메뉴_후라이드치킨 = 후라이드치킨(한마리메뉴.getId());
        메뉴_후라이드치킨.setMenuProducts(List.of(createMenuProduct(후라이드.getId(), 2)));
        메뉴_후라이드치킨 = menuService.create(메뉴_후라이드치킨);

        // 테이블 설정
        OrderTable orderTable = tableService.create(테이블_1번());
        tableService.changeEmpty(orderTable.getId(), createOrderTable(0, false));

        // 주문
        OrderLineItem orderLineItem = createOrderLineItem(메뉴_후라이드치킨.getId(), 1);
        Order order = orderService.create(createOrder(orderTable.getId(), List.of(orderLineItem)));

        // 상태를 식사로 변경
        Order changedOrder = createOrder(orderTable.getId(), OrderStatus.MEAL, List.of(orderLineItem));
        Order actual = orderService.changeOrderStatus(order.getId(), changedOrder);

        assertAll(
                () -> assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId()),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name()),
                () -> assertThat(actual.getOrderLineItems()).hasSize(1)
        );
    }

    @DisplayName("order의 상태가 COMPLETION인 경우 예외를 던진다.")
    @Test
    void changeOrderStatusToCompletion() {
        // 메뉴 설정
        Product 후라이드 = productService.create(후라이드());
        MenuGroup 한마리메뉴 = menuGroupService.create(한마리메뉴());
        Menu 메뉴_후라이드치킨 = 후라이드치킨(한마리메뉴.getId());
        메뉴_후라이드치킨.setMenuProducts(List.of(createMenuProduct(후라이드.getId(), 2)));
        메뉴_후라이드치킨 = menuService.create(메뉴_후라이드치킨);

        // 테이블 설정
        OrderTable orderTable = tableService.create(테이블_1번());
        tableService.changeEmpty(orderTable.getId(), createOrderTable(0, false));

        // 주문
        OrderLineItem orderLineItem = createOrderLineItem(메뉴_후라이드치킨.getId(), 1);
        Order order = orderService.create(createOrder(orderTable.getId(), List.of(orderLineItem)));

        // 계산완료로 변경
        Order changedOrder = createOrder(orderTable.getId(), OrderStatus.COMPLETION, List.of(orderLineItem));
        Order actual = orderService.changeOrderStatus(order.getId(), changedOrder);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), actual))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
