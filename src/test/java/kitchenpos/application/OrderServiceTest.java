package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class OrderServiceTest {

    private final OrderDao orderDao;
    private final OrderService orderService;
    private final TableService tableService;
    private final ProductService productService;
    private final MenuService menuService;
    private final MenuGroupService menuGroupService;

    OrderServiceTest(OrderDao orderDao, OrderService orderService, TableService tableService,
                     ProductService productService, MenuService menuService,
                     MenuGroupService menuGroupService) {
        this.orderDao = orderDao;
        this.orderService = orderService;
        this.tableService = tableService;
        this.productService = productService;
        this.menuService = menuService;
        this.menuGroupService = menuGroupService;
    }

    @Test
    void 주문을_생성한다() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        OrderTable savedTable = tableService.create(orderTable);
        Order order = new Order(savedTable.getId(), null, null,
                List.of(new OrderLineItem(1L, null, 메뉴를_저장한다().getId(), 1)));

        Order actual = orderService.create(order);
        assertThat(actual).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 주문을_생성할때_주문정보가_없는_경우_예외를_발생시킨다() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        OrderTable savedTable = tableService.create(orderTable);
        Order order = new Order(savedTable.getId(), null, null, null);

        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할때_주문정보와_저장된_메뉴와_다를_경우_예외를_발생시킨다() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        OrderTable savedTable = tableService.create(orderTable);
        Order order = new Order(savedTable.getId(), null, null, List.of(new OrderLineItem(1L, null, -1L, 1)));

        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할때_테이블이_존재하지_않는_경우_예외를_발생시킨다() {
        Order order = new Order(-1L, null, null, List.of(new OrderLineItem(1L, null, 메뉴를_저장한다().getId(), 1)));

        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블이_빈_경우_예외를_발생시킨다() {
        OrderTable orderTable = new OrderTable(null, 0, true);
        OrderTable savedTable = tableService.create(orderTable);
        Order order = new Order(savedTable.getId(), null, null,
                List.of(new OrderLineItem(1L, null, 메뉴를_저장한다().getId(), 1)));

        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_조회한다() {
        orderDao.save(new Order());

        List<Order> orders = orderService.list();

        assertThat(orders).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void 주문의_상태를_조리에서_식사로_바꾼다() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        OrderTable savedTable = tableService.create(orderTable);
        Order order = new Order(savedTable.getId(), null, null,
                List.of(new OrderLineItem(1L, null, 메뉴를_저장한다().getId(), 1)));
        Order savedOrder = orderService.create(order);

        savedOrder.setOrderStatus("MEAL");
        Order result = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 주문의_상태를_바꿀때_주문이_존재하지_않는_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문의_상태를_바꿀때_주문의_상태가_계산완료인_경우_예외를_발생시킨다() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        OrderTable savedTable = tableService.create(orderTable);
        Order order = new Order(savedTable.getId(), null, null,
                List.of(new OrderLineItem(1L, null, 메뉴를_저장한다().getId(), 1)));
        Order savedOrder = orderService.create(order);

        savedOrder.setOrderStatus("COMPLETION");
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    Menu 메뉴를_저장한다() {
        Product jwt_후라이드 = productService.create(new Product("JWT 후라이드", new BigDecimal(100_000)));
        Product jwt_양념 = productService.create(new Product("JWT 양념", new BigDecimal(100_000)));
        MenuProduct 후라이드 = new MenuProduct(1L, null, jwt_후라이드.getId(), 1);
        MenuProduct 양념 = new MenuProduct(2L, null, jwt_양념.getId(), 1);
        MenuGroup menuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        Menu menu = new Menu("반반치킨", new BigDecimal(200_000), menuGroup.getId(), List.of(후라이드, 양념));

        return menuService.create(menu);
    }
}
