package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(value = "classpath:data/truncate.sql")
class TableServiceTest {

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
        final OrderTable orderTable = new OrderTable(0, true);

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
        tableService.create(new OrderTable(0, true));
        tableService.create(new OrderTable(0, true));

        // when
        final List<OrderTable> result = tableService.list();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void changeEmpty() {
        // given
        final OrderTable saved = tableService.create(new OrderTable(0, true));

        // when
        final OrderTable changed = new OrderTable(false);
        final OrderTable result = tableService.changeEmpty(saved.getId(), changed);

        // then
        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    void changeEmpty_tableNullException() {
        // when & then
        final OrderTable changed = new OrderTable(false);
        assertThatThrownBy(() -> tableService.changeEmpty(-1L, changed))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeEmpty_tableGroupException() {
        // given
        final OrderTable saved = tableService.create(new OrderTable(0, true));
        saved.setTableGroupId(1L);

        // when & then
        final OrderTable changed = new OrderTable(false);
        assertThatThrownBy(() -> tableService.changeEmpty(saved.getTableGroupId(), changed))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmpty_tableStatusException(final String status) {
        // given
        final MenuGroup menuGroup = menuGroupService.create(new MenuGroup("Group1"));
        final Product product = productService.create(new Product("Product1", BigDecimal.valueOf(10000)));
        final MenuProduct menuProduct = new MenuProduct(product.getId(), 2);
        final Menu menu = menuService.create(new Menu("후라이드+후라이드",
                BigDecimal.valueOf(19000),
                menuGroup.getId(),
                List.of(menuProduct)));
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 1);

        final OrderTable saved = tableService.create(new OrderTable(0, false));
        final Order order = orderService.create(new Order(saved.getId(), List.of(orderLineItem)));
        orderService.changeOrderStatus(order.getId(), new Order(status));

        // when & then
        final OrderTable changed = new OrderTable(true);
        assertThatThrownBy(() -> tableService.changeEmpty(saved.getTableGroupId(), changed))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTable saved = tableService.create(new OrderTable(0, false));

        // when
        final OrderTable changed = new OrderTable(1);
        final OrderTable result = tableService.changeNumberOfGuests(saved.getId(), changed);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(1);
    }

    @Test
    void changeNumberOfGuests_numberException() {
        // given
        final OrderTable saved = tableService.create(new OrderTable(0, false));

        // when & then
        final OrderTable changed = new OrderTable(-1);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(saved.getTableGroupId(), changed))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_tableNullException() {
        // when & then
        final OrderTable changed = new OrderTable(1);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, changed))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuests_tableEmptyException() {
        // given
        final OrderTable saved = tableService.create(new OrderTable(0, true));

        // when & then
        final OrderTable changed = new OrderTable(1);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(saved.getTableGroupId(), changed))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
