package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.CreateTableGroupDto;
import kitchenpos.config.IntegrationTest;
import kitchenpos.domain.exception.InvalidOrderTableSizeException;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.repository.MenuGroupRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.repository.OrderTableRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.repository.ProductRepository;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.repository.TableGroupRepository;
import kitchenpos.ui.dto.request.CreateOrderGroupOrderTableRequest;
import kitchenpos.ui.dto.request.CreateTableGroupRequest;
import org.junit.jupiter.api.Test;
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
        final CreateTableGroupDto actual = tableGroupService.create(request);

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
        final TableGroup persistTableGroup = tableGroupRepository.save(new TableGroup());

        // when
        tableGroupService.ungroup(persistTableGroup.getId());

        // then
        assertAll(
                () -> assertThat(persistOrderTable1.isEmpty()).isTrue(),
                () -> assertThat(persistOrderTable1.getTableGroupId()).isNull(),
                () -> assertThat(persistOrderTable2.isEmpty()).isTrue(),
                () -> assertThat(persistOrderTable2.getTableGroupId()).isNull()
        );
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
        final MenuProduct persistMenuProduct = new MenuProduct(
                persistProduct.getId(),
                persistProduct.price(),
                persistProduct.name(),
                1L
        );

        return menuRepository.save(Menu.of(
                "메뉴",
                BigDecimal.TEN,
                List.of(persistMenuProduct),
                persistMenuGroup.getId())
        );
    }

    private void persistOrderBy(
            final Menu persistMenu,
            final OrderStatus orderStatus,
            final OrderTable persistOrderTable1,
            final OrderTable persistOrderTable2
    ) {
        final OrderLineItem persistOrderLineItem1 = new OrderLineItem(persistMenu.getId(), 1L);
        final OrderLineItem persistOrderLineItem2 = new OrderLineItem(persistMenu.getId(), 1L);
        final Order order1 = new Order(
                persistOrderTable1.getId(),
                orderStatus,
                LocalDateTime.now().minusHours(3),
                List.of(persistOrderLineItem1)
        );
        final Order order2 = new Order(
                persistOrderTable2.getId(),
                orderStatus,
                LocalDateTime.now().minusHours(3),
                List.of(persistOrderLineItem2)
        );
        orderRepository.save(order1);
        orderRepository.save(order2);
    }
}
