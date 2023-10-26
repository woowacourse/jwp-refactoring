package kitchenpos.application;

import static kitchenpos.Fixture.INVALID_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.Fixture;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.OrderChangeOrderStatusRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceIntegrationTest {
    private static final OrderTableChangeEmptyRequest ORDER_TABLE_STATUS_EMPTY
            = new OrderTableChangeEmptyRequest(true);
    private static final OrderTableChangeEmptyRequest ORDER_TABLE_STATUS_NOT_EMPTY
            = new OrderTableChangeEmptyRequest(false);
    private static final OrderTableChangeNumberOfGuestsRequest ORDER_TABLE_GUEST_POSITIVE
            = new OrderTableChangeNumberOfGuestsRequest(1);
    private static final OrderTableChangeNumberOfGuestsRequest ORDER_TABLE_GUEST_NEGATIVE
            = new OrderTableChangeNumberOfGuestsRequest(-1);
    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

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
        final OrderTableCreateRequest orderTable = Fixture.ORDER_TABLE_EMPTY;

        // when
        final OrderTableResponse result = tableService.create(orderTable);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isNotNull();
            softly.assertThat(result.getTableGroupId()).isNull();
        });
    }

    @Test
    void list() {
        // given
        final OrderTableResponse orderTable1 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final OrderTableResponse orderTable2 = tableService.create(Fixture.ORDER_TABLE_EMPTY);

        // when
        final List<OrderTableResponse> result = tableService.list();

        // then
        assertThat(result).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(orderTable1, orderTable2);
    }

    @Test
    void changeEmpty() {
        // given
        final OrderTableResponse saved = tableService.create(Fixture.ORDER_TABLE_EMPTY);

        // when
        final OrderTableResponse result = tableService.changeEmpty(saved.getId(), ORDER_TABLE_STATUS_NOT_EMPTY);

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
        final OrderTableResponse saved = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final OrderTableResponse ignored = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        tableGroupService.create(new TableGroupCreateRequest(List.of(saved.getId(), ignored.getId())));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(saved.getId(), ORDER_TABLE_STATUS_NOT_EMPTY))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmpty_tableStatusException(final String status) {
        // given
        final OrderTableResponse saved = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);
        final OrderResponse order = orderService.create(generateBasicOrderBy(saved));
        orderService.changeOrderStatus(order.getId(), new OrderChangeOrderStatusRequest(status));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(saved.getId(), ORDER_TABLE_STATUS_EMPTY))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderCreateRequest generateBasicOrderBy(final OrderTableResponse orderTable) {
        final MenuGroupResponse menuGroup = menuGroupService.create(Fixture.MENU_GROUP);
        final ProductResponse product = productService.create(Fixture.PRODUCT);
        final MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), 2);
        final MenuResponse menu = menuService.create(
                new MenuCreateRequest("Menu1", BigDecimal.valueOf(19000), menuGroup.getId(), List.of(menuProduct)));
        final OrderLineItemRequest orderLineItem = new OrderLineItemRequest(menu.getId(), 1);
        return new OrderCreateRequest(orderTable.getId(), List.of(orderLineItem));
    }

    @Test
    void changeNumberOfGuests() {
        // given
        final OrderTableResponse saved = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);

        // when
        final OrderTableResponse result = tableService.changeNumberOfGuests(saved.getId(), ORDER_TABLE_GUEST_POSITIVE);

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
        final OrderTableResponse saved = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(saved.getId(), ORDER_TABLE_GUEST_NEGATIVE))
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
        final OrderTableResponse saved = tableService.create(Fixture.ORDER_TABLE_EMPTY);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(saved.getId(), ORDER_TABLE_GUEST_POSITIVE))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
