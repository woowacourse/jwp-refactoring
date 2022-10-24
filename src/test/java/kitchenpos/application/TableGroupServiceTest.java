package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class TableGroupServiceTest {

    private final TableGroupService tableGroupService;
    private final TableService tableService;
    private final OrderService orderService;
    private final ProductService productService;
    private final MenuService menuService;
    private final MenuGroupService menuGroupService;

    TableGroupServiceTest(TableGroupService tableGroupService, TableService tableService,
                          OrderService orderService, ProductService productService,
                          MenuService menuService, MenuGroupService menuGroupService) {
        this.tableGroupService = tableGroupService;
        this.tableService = tableService;
        this.orderService = orderService;
        this.productService = productService;
        this.menuService = menuService;
        this.menuGroupService = menuGroupService;
    }

    @Test
    void 테이블그룹을_생성한다() {
        TableGroup tableGroup = new TableGroup(null, List.of(빈테이블을_생성한다(), 빈테이블을_생성한다()));

        assertThat(tableGroupService.create(tableGroup)).isInstanceOf(TableGroup.class);
    }

    @Test
    void 테이블그룹을_생성할때_테이블이_없는_경우_예외를_발생시킨다() {
        TableGroup tableGroup = new TableGroup(null, null);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할때_테이블이_2개보다_적은_경우_예외를_발생시킨다() {
        TableGroup tableGroup = new TableGroup(null, List.of(빈테이블을_생성한다()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할때_테이블이_저장된_정보와_다를_경우_예외를_발생시킨다() {
        TableGroup tableGroup = new TableGroup(null, List.of(new OrderTable(-1L, null, 0, true), 빈테이블을_생성한다()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할때_테이블이_비어있지_않은_경우_예외를_발생시킨다() {
        OrderTable orderTable = 빈테이블을_생성한다();
        orderTable.setEmpty(false);
        tableService.changeEmpty(orderTable.getId(), orderTable);
        TableGroup tableGroup = new TableGroup(null, List.of(orderTable, 빈테이블을_생성한다()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_해제한다() {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(빈테이블을_생성한다(), 빈테이블을_생성한다()));
        TableGroup savedGroup = tableGroupService.create(tableGroup);

        Assertions.assertDoesNotThrow(() -> tableGroupService.ungroup(savedGroup.getId()));
    }

    @Test
    void 테이블그룹을_해제할때_조리중인_주문이_있는_경우_예외를_발생시킨다() {
        OrderTable orderTable = 빈테이블을_생성한다();
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable, 빈테이블을_생성한다()));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        Order order = new Order(orderTable.getId(), null, null,
                List.of(new OrderLineItem(1L, null, 메뉴를_저장한다().getId(), 1)));
        orderService.create(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_해제할때_식사중인_주문이_있는_경우_예외를_발생시킨다() {
        OrderTable orderTable = 빈테이블을_생성한다();
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable, 빈테이블을_생성한다()));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
        Order order = new Order(orderTable.getId(), null, null,
                List.of(new OrderLineItem(1L, null, 메뉴를_저장한다().getId(), 1)));
        Order savedOrder = orderService.create(order);
        savedOrder.setOrderStatus("MEAL");
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    OrderTable 빈테이블을_생성한다() {
        OrderTable orderTable = new OrderTable(null, 0, true);
        return tableService.create(orderTable);
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
