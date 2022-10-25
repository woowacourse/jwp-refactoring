package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableGroupServiceTest {
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

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("테이블 그룹을 등록할 수 있다.")
    void create() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = tableService.create(orderTable1);
        final OrderTable savedTable2 = tableService.create(orderTable2);
        final OrderTable savedTable3 = tableService.create(orderTable3);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);

        // when
        final TableGroup actual = tableGroupService.create(tableGroup);

        // then
        assertThat(actual.getOrderTables())
                .usingElementComparatorOnFields("id")
                .containsExactly(savedTable1, savedTable2, savedTable3);
    }

    @Test
    @DisplayName("2개 미만의 테이블을 테이블 그룹으로 등록할 수 없다.")
    void create_ExceptionOrderTablesLowerThanTwo() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = tableService.create(orderTable1);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되지 않은 테이블을 테이블 그룹에 등록할 수 없다.")
    void create_ExceptionNotSavedOrderTable() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = tableService.create(orderTable1);
        final OrderTable savedTable2 = tableService.create(orderTable2);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2, orderTable3);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있지 않는 테이블을 테이블 그룹에 등록할 수 없다.")
    void create_ExceptionEmptyOrderTable() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(false, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = tableService.create(orderTable1);
        final OrderTable savedTable2 = tableService.create(orderTable2);
        final OrderTable savedTable3 = tableService.create(orderTable3);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 테이블 그룹에 속한 테이블을 테이블 그룹에 등록할 수 없다.")
    void create_ExceptionAlreadyCreatedTableGroup() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = tableService.create(orderTable1);
        final OrderTable savedTable2 = tableService.create(orderTable2);
        final OrderTable savedTable3 = tableService.create(orderTable3);

        final TableGroup tableGroup1 = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);
        final TableGroup tableGroup2 = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);

        tableGroupService.create(tableGroup1);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 테이블 그룹을 해제할 수 있다.")
    void ungroup() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = tableService.create(orderTable1);
        final OrderTable savedTable2 = tableService.create(orderTable2);
        final OrderTable savedTable3 = tableService.create(orderTable3);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when, then
        assertThatCode(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("해제하려는 그룹에 속한 주문 테이블이 조리나 식사 상태면 안된다.")
    void ungroup_exeptionOrderTableCookingOrMeal() {
        // given
        final OrderTable orderTable1 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable2 = OrderTableFixture.create(true, 1);
        final OrderTable orderTable3 = OrderTableFixture.create(true, 1);
        final OrderTable savedTable1 = tableService.create(orderTable1);
        final OrderTable savedTable2 = tableService.create(orderTable2);
        final OrderTable savedTable3 = tableService.create(orderTable3);

        final TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2, savedTable3);
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        final Product product1 = ProductFixture.createWithPrice(1000L);
        final Product product2 = ProductFixture.createWithPrice(1000L);
        final Product savedProduct1 = productService.create(product1);
        final Product savedProduct2 = productService.create(product2);

        final MenuGroup menuGroup = MenuGroupFixture.createDefaultWithoutId();
        final MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);
        final Menu menu = MenuFixture.createWithPrice(savedMenuGroup, 2000L, savedProduct1, savedProduct2);
        final Menu savedMenu = menuService.create(menu);

        final OrderLineItem orderLineItem1 = OrderLineItemFixture.create(savedMenu);
        final Order order = OrderFixture.create(savedTable1, OrderStatus.COMPLETION, orderLineItem1);
         orderService.create(order);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
