package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class TableServiceTest {

    private final TableService tableService;
    private final OrderService orderService;
    private final ProductService productService;
    private final MenuService menuService;
    private final MenuGroupService menuGroupService;

    TableServiceTest(TableService tableService, OrderService orderService,
                     ProductService productService, MenuService menuService,
                     MenuGroupService menuGroupService) {
        this.tableService = tableService;
        this.orderService = orderService;
        this.productService = productService;
        this.menuService = menuService;
        this.menuGroupService = menuGroupService;
    }

    @Test
    void 테이블을_생성한다() {
        OrderTable orderTable = new OrderTable(null, 0, true);

        assertThat(tableService.create(orderTable)).isInstanceOf(OrderTable.class);
    }

    @Test
    void 테이블을_모두_조회한다() {
        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(8);
    }

    @Test
    void 테이블을_비운다() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        OrderTable savedTable = tableService.create(orderTable);

        OrderTable result = tableService.changeEmpty(savedTable.getId(), new OrderTable(null, 0, true));

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void 테이블을_비울때_테이블이_존재하지_않을_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> tableService.changeEmpty(-1L, null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블을_비울때_조리중인_주문이_있는_경우_예외를_발생시킨다() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        OrderTable savedTable = tableService.create(orderTable);
        Order order = new Order(savedTable.getId(), null, null,
                List.of(new OrderLineItem(1L, null, 메뉴를_저장한다().getId(), 1)));
        orderService.create(order);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), new OrderTable(1L, 0, true)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블을_비울때_식사중인_주문이_있는_경우_예외를_발생시킨다() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        OrderTable savedTable = tableService.create(orderTable);
        Order order = new Order(savedTable.getId(), null, null,
                List.of(new OrderLineItem(1L, null, 메뉴를_저장한다().getId(), 1)));
        Order savedOrder = orderService.create(order);
        savedOrder.setOrderStatus("MEAL");
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), new OrderTable(1L, 0, true)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_손님수를_변경한다() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        OrderTable savedTable = tableService.create(orderTable);

        orderTable.setNumberOfGuests(1);
        OrderTable result = tableService.changeNumberOfGuests(savedTable.getId(), orderTable);

        assertThat(result.getNumberOfGuests()).isEqualTo(1);
    }

    @Test
    void 테이블의_손님수를_변경할때_수정할_손님수가_0보다_작을때_예외를_발생시킨다() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        OrderTable savedTable = tableService.create(orderTable);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
                savedTable.getId(), new OrderTable(savedTable.getId(), -1, false)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_손님수를_변경할때_테이블이_존재하지_않는_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, new OrderTable(null, 1, false)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_손님수를_변경할때_이미_비어있는_경우_예외를_발생시킨다() {
        OrderTable orderTable = new OrderTable(null, 0, true);
        OrderTable savedTable = tableService.create(orderTable);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), new OrderTable(null, 1, true)))
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
