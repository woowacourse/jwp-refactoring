package kitchenpos.application;

import static kitchenpos.Fixture.INVALID_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.Fixture;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceIntegrationTest {
    private static final long ANY_VALID_ID = 1L;
    private static final OrderTable ORDER_TABLE_STATUS_EMPTY = new OrderTable(true);
    private static final OrderTable ORDER_TABLE_STATUS_NOT_EMPTY = new OrderTable(false);
    private static final OrderTable ORDER_TABLE_GUEST_POSITIVE = new OrderTable(1);
    private static final OrderTable ORDER_TABLE_GUEST_NEGATIVE = new OrderTable(-1);
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

    @Test
    void create() {
        // given
        final OrderTable orderTable = Fixture.ORDER_TABLE_EMPTY;

        // when
        final OrderTable result = tableService.create(orderTable);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isNotNull();
            softly.assertThat(result.getTableGroupId()).isNull();
        });
    }

    @Test
    void list() {
        // given
        final OrderTable orderTable1 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final OrderTable orderTable2 = tableService.create(Fixture.ORDER_TABLE_EMPTY);

        // when
        final List<OrderTable> result = tableService.list();

        // then
        assertThat(result).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(orderTable1, orderTable2);
    }

    @Test
    void changeEmpty() {
        // given
        final OrderTable saved = tableService.create(Fixture.ORDER_TABLE_EMPTY);

        // when
        final OrderTable result = tableService.changeEmpty(saved.getId(), ORDER_TABLE_STATUS_NOT_EMPTY);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.isEmpty()).isFalse();
            softly.assertThat(result).usingRecursiveComparison()
                    .ignoringFields("empty")
                    .isEqualTo(saved);
        });
    }

    @Test
    void changeEmpty_tableNullException() {
        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(INVALID_ID, ORDER_TABLE_STATUS_NOT_EMPTY))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeEmpty_tableGroupException() {
        // given
        final OrderTable saved = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        saved.setTableGroupId(ANY_VALID_ID);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(saved.getTableGroupId(), ORDER_TABLE_STATUS_NOT_EMPTY))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmpty_tableStatusException(final String status) {
        // given
        final OrderTable saved = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);
        final Order order = orderService.create(generateBasicOrderBy(saved));
        orderService.changeOrderStatus(order.getId(), new Order(status));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(saved.getTableGroupId(), ORDER_TABLE_STATUS_EMPTY))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Order generateBasicOrderBy(final OrderTable orderTable) {
        final MenuGroup menuGroup = menuGroupService.create(Fixture.MENU_GROUP);
        final Product product = productService.create(Fixture.PRODUCT);
        final MenuProduct menuProduct = new MenuProduct(product.getId(), 2);
        final Menu menu = menuService.create(
                new Menu("Menu1", BigDecimal.valueOf(19000), menuGroup.getId(), List.of(menuProduct)));
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 1);
        return new Order(orderTable.getId(), List.of(orderLineItem));
    }

    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable saved = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);

        // when
        final OrderTable result = tableService.changeNumberOfGuests(saved.getId(), ORDER_TABLE_GUEST_POSITIVE);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getNumberOfGuests()).isEqualTo(1);
            softly.assertThat(result).usingRecursiveComparison()
                    .ignoringFields("numberOfGuests")
                    .isEqualTo(saved);
        });
    }

    @Test
    void changeNumberOfGuests_numberException() {
        // given
        final OrderTable saved = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(saved.getTableGroupId(), ORDER_TABLE_GUEST_NEGATIVE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_tableNullException() {
        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(INVALID_ID, ORDER_TABLE_GUEST_POSITIVE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_tableEmptyException() {
        // given
        final OrderTable saved = tableService.create(Fixture.ORDER_TABLE_EMPTY);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(saved.getTableGroupId(), ORDER_TABLE_GUEST_POSITIVE))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
