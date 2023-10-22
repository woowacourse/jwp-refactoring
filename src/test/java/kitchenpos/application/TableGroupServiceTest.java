package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.Fixture;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceIntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void create() {
        // given
        final OrderTable orderTable1 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final OrderTable orderTable2 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));

        // when
        final TableGroup result = tableGroupService.create(tableGroup);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isNotNull();
            softly.assertThat(result.getCreatedDate()).isNotNull();
        });
    }

    @Test
    void create_singleTableException() {
        // given
        final OrderTable orderTable = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final TableGroup tableGroup = new TableGroup(List.of(orderTable));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_tableNullException() {
        // given
        final OrderTable orderTable1 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final OrderTable orderTable2 = Fixture.ORDER_TABLE_EMPTY;
        final TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_tableDuplicateException() {
        // given
        final OrderTable orderTable = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final TableGroup tableGroup = new TableGroup(List.of(orderTable, orderTable));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_tableNotEmptyException() {
        // given
        final OrderTable orderTable1 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final OrderTable orderTable2 = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);
        final TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_tableHasGroupException() {
        // given
        final OrderTable orderTable1 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final OrderTable orderTable2 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        tableGroupService.create(new TableGroup(List.of(orderTable1, orderTable2)));

        final OrderTable orderTable3 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable3));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ungroup() {
        // given
        final OrderTable orderTable1 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final OrderTable orderTable2 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final TableGroup tableGroup = tableGroupService.create(new TableGroup(List.of(orderTable1, orderTable2)));

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        final List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroup.getId());
        assertSoftly(softly -> {
            softly.assertThat(orderTables).isEmpty();
            final OrderTable savedOrderTable1 = orderTableDao.findById(orderTable1.getId()).get();
            softly.assertThat(savedOrderTable1.getTableGroup()).isNull();
            softly.assertThat(savedOrderTable1.isEmpty()).isFalse();
            final OrderTable savedOrderTable2 = orderTableDao.findById(orderTable2.getId()).get();
            softly.assertThat(savedOrderTable2.getTableGroup()).isNull();
            softly.assertThat(savedOrderTable2.isEmpty()).isFalse();
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void ungroup_tableStatusException(final String status) {
        // given
        final OrderTable orderTable1 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final OrderTable orderTable2 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final TableGroup tableGroup = tableGroupService.create(new TableGroup(List.of(orderTable1, orderTable2)));

        final Order order = orderService.create(generateBasicOrderBy(orderTable1));
        orderService.changeOrderStatus(order.getId(), new Order(OrderStatus.get(status)));

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Order generateBasicOrderBy(final OrderTable orderTable) {
        final MenuGroup menuGroup = menuGroupService.create(Fixture.MENU_GROUP);
        final Product product = productService.create(Fixture.PRODUCT);
        final MenuProduct menuProduct = new MenuProduct(product, 2);
        final Menu menu = menuService.create(
                new Menu("Menu1", BigDecimal.valueOf(19000), menuGroup, List.of(menuProduct)));
        final OrderLineItem orderLineItem = new OrderLineItem(menu, 1);
        return new Order(orderTable, List.of(orderLineItem));
    }
}
