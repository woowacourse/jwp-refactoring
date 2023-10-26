package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.Fixture;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.OrderChangeOrderStatusRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.dto.response.TableGroupResponse;
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
    private OrderTableRepository orderTableRepository;

    @Test
    void create() {
        // given
        final OrderTableResponse orderTable1 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final OrderTableResponse orderTable2 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final TableGroupCreateRequest tableGroup = new TableGroupCreateRequest(
                List.of(orderTable1.getId(), orderTable2.getId()));

        // when
        final TableGroupResponse result = tableGroupService.create(tableGroup);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isNotNull();
            softly.assertThat(result.getCreatedDate()).isNotNull();
        });
    }

    @Test
    void create_singleTableException() {
        // given
        final OrderTableResponse orderTable = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final TableGroupCreateRequest tableGroup = new TableGroupCreateRequest(List.of(orderTable.getId()));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_tableNullException() {
        // given
        final OrderTableResponse orderTable1 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final TableGroupCreateRequest tableGroup = new TableGroupCreateRequest(
                List.of(orderTable1.getId(), Fixture.INVALID_ID));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_tableDuplicateException() {
        // given
        final OrderTableResponse orderTable = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final TableGroupCreateRequest tableGroup = new TableGroupCreateRequest(
                List.of(orderTable.getId(), orderTable.getId()));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_tableNotEmptyException() {
        // given
        final OrderTableResponse orderTable1 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final OrderTableResponse orderTable2 = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);
        final TableGroupCreateRequest tableGroup = new TableGroupCreateRequest(
                List.of(orderTable1.getId(), orderTable2.getId()));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_tableHasGroupException() {
        // given
        final OrderTableResponse orderTable1 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final OrderTableResponse orderTable2 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        tableGroupService.create(new TableGroupCreateRequest(List.of(orderTable1.getId(), orderTable2.getId())));

        final OrderTableResponse orderTable3 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final TableGroupCreateRequest tableGroup = new TableGroupCreateRequest(
                List.of(orderTable1.getId(), orderTable3.getId()));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ungroup() {
        // given
        final OrderTableResponse orderTable1 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final OrderTableResponse orderTable2 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final TableGroupResponse tableGroup = tableGroupService.create(
                new TableGroupCreateRequest(List.of(orderTable1.getId(), orderTable2.getId())));

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());
        assertSoftly(softly -> {
            softly.assertThat(orderTables).isEmpty();
            final OrderTable savedOrderTable1 = orderTableRepository.findById(orderTable1.getId()).get();
            softly.assertThat(savedOrderTable1.getTableGroup()).isNull();
            softly.assertThat(savedOrderTable1.isEmpty()).isFalse();
            final OrderTable savedOrderTable2 = orderTableRepository.findById(orderTable2.getId()).get();
            softly.assertThat(savedOrderTable2.getTableGroup()).isNull();
            softly.assertThat(savedOrderTable2.isEmpty()).isFalse();
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void ungroup_tableStatusException(final String status) {
        // given
        final OrderTableResponse orderTable1 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final OrderTableResponse orderTable2 = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final TableGroupResponse tableGroup = tableGroupService.create(
                new TableGroupCreateRequest(List.of(orderTable1.getId(), orderTable2.getId())));

        final OrderResponse order = orderService.create(generateBasicOrderBy(orderTable1));
        orderService.changeOrderStatus(order.getId(), new OrderChangeOrderStatusRequest(status));

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
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
}
