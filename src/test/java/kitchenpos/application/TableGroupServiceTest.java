package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.config.IntegrationTest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.exception.InvalidOrderStatusCompletionException;
import kitchenpos.domain.exception.InvalidOrderTableSizeException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.request.CreateOrderGroupOrderTableRequest;
import kitchenpos.ui.dto.request.CreateTableGroupRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest {

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    TableGroupRepository tableGroupRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    TableGroupService tableGroupService;

    @Test
    void create_메서드는_tableGroup을_전달하면_tableGroup을_저장하고_반환한다() {
        // given
        final OrderTable persistOrderTable1 = orderTableRepository.save(new OrderTable(0, true));
        final OrderTable persistOrderTable2 = orderTableRepository.save(new OrderTable(0, true));
        final CreateTableGroupRequest request = convertCreateTableGroupRequest(
                List.of(persistOrderTable1, persistOrderTable2)
        );

        // when
        final TableGroup actual = tableGroupService.create(request);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void create_메서드는_tableGroup이_비어있는_tableGroup을_전달하면_예외가_발생한다() {
        // given
        final CreateTableGroupRequest invalidRequest = convertCreateTableGroupRequest(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidRequest))
                .isInstanceOf(InvalidOrderTableSizeException.class);
    }

    @Test
    void create_메서드는_tableGroup이_하나인_tableGroup을_전달하면_예외가_발생한다() {
        // given
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, true));
        final CreateTableGroupRequest invalidRequest = convertCreateTableGroupRequest(List.of(persistOrderTable));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidRequest))
                .isInstanceOf(InvalidOrderTableSizeException.class);
    }

    @Test
    void create_메서드는_tableGroup이_비어있지_않은_tableGroup을_전달하면_예외가_발생한다() {
        // given
        final OrderTable persistOrderTable1 = orderTableRepository.save(new OrderTable(0, false));
        final OrderTable persistOrderTable2 = orderTableRepository.save(new OrderTable(0, false));
        final CreateTableGroupRequest invalidRequest = convertCreateTableGroupRequest(
                List.of(persistOrderTable1, persistOrderTable2)
        );

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ungroup_메서드는_지정한_tableGroup의_id의_그룹을_해제한다() {
        // given
        final OrderTable persistOrderTable1 = orderTableRepository.save(new OrderTable(0, true));
        final OrderTable persistOrderTable2 = orderTableRepository.save(new OrderTable(0, true));
        final Menu persistMenu = persistMenu();
        persistOrderBy(persistMenu,  OrderStatus.COMPLETION, persistOrderTable1, persistOrderTable2);
        final TableGroup persistTableGroup = persistTableGroup(persistOrderTable1, persistOrderTable2);

        // when
        tableGroupService.ungroup(persistTableGroup.getId());

        // then
        assertAll(
                () -> assertThat(persistOrderTable1.getTableGroup()).isNull(),
                () -> assertThat(persistOrderTable2.getTableGroup()).isNull()
        );
    }

    @ParameterizedTest(name = "orderStatus가 {0}일 때 예외가 발생한다.")
    @ValueSource(strings = {"MEAL", "COOKING"})
    void upgroup_메서드는_orderTable의_order의_상태가_COMPLETION이_아닌_tableGroup을_전달하면_예외가_발생한다(final String invalidOrderStatus) {
        // given
        final OrderTable persistOrderTable1 = orderTableRepository.save(new OrderTable(0, true));
        final OrderTable persistOrderTable2 = orderTableRepository.save(new OrderTable(0, true));
        final Menu persistMenu = persistMenu();
        final OrderStatus orderStatus = OrderStatus.valueOf(invalidOrderStatus);
        persistOrderBy(persistMenu, orderStatus, persistOrderTable1, persistOrderTable2);
        final TableGroup persistTableGroup = persistTableGroup(persistOrderTable1, persistOrderTable2);
        persistOrderTables(persistOrderTable1, persistOrderTable2, persistTableGroup);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(persistTableGroup.getId()))
                .isInstanceOf(InvalidOrderStatusCompletionException.class);
    }

    private CreateTableGroupRequest convertCreateTableGroupRequest(final List<OrderTable> orderTables) {
        final List<CreateOrderGroupOrderTableRequest> createOrderGroupOrderTableRequests = new ArrayList<>();

        for (final OrderTable orderTable : orderTables) {
            createOrderGroupOrderTableRequests.add(new CreateOrderGroupOrderTableRequest(orderTable.getId()));
        }

        return new CreateTableGroupRequest(createOrderGroupOrderTableRequests);
    }

    private Menu persistMenu() {
        final MenuGroup persistMenuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Product persistProduct = productRepository.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct persistMenuProduct = new MenuProduct(persistProduct, 1);

        return menuRepository.save(Menu.of(
                "메뉴",
                BigDecimal.TEN,
                List.of(persistMenuProduct),
                persistMenuGroup)
        );
    }

    private void persistOrderBy(
            final Menu persistMenu,
            final OrderStatus orderStatus,
            final OrderTable persistOrderTable1,
            final OrderTable persistOrderTable2
    ) {
        final OrderLineItem persistOrderLineItem1 = new OrderLineItem(persistMenu, 1L);
        final OrderLineItem persistOrderLineItem2 = new OrderLineItem(persistMenu, 1L);
        final Order order1 = new Order(
                persistOrderTable1,
                orderStatus,
                LocalDateTime.now().minusHours(3),
                List.of(persistOrderLineItem1)
        );
        final Order order2 = new Order(
                persistOrderTable2,
                orderStatus,
                LocalDateTime.now().minusHours(3),
                List.of(persistOrderLineItem2)
        );
        orderRepository.save(order1);
        orderRepository.save(order2);
    }

    private TableGroup persistTableGroup(final OrderTable persistOrderTable1, final OrderTable persistOrderTable2) {
        return tableGroupRepository.save(
                new TableGroup(List.of(persistOrderTable1, persistOrderTable2))
        );
    }

    private void persistOrderTables(
            final OrderTable persistOrderTable1,
            final OrderTable persistOrderTable2,
            final TableGroup tableGroup
    ) {
        if (tableGroup != null) {
            persistOrderTable1.group(tableGroup);
            persistOrderTable2.group(tableGroup);
        }

        orderTableRepository.save(persistOrderTable1);
        orderTableRepository.save(persistOrderTable2);
    }
}
