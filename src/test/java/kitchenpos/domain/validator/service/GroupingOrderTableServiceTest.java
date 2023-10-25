package kitchenpos.domain.validator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.config.IntegrationTest;
import kitchenpos.domain.exception.InvalidEmptyOrderTableException;
import kitchenpos.domain.exception.InvalidOrderTableSizeException;
import kitchenpos.domain.exception.InvalidOrderTableStatusException;
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
import kitchenpos.domain.tablegroup.service.GroupingTableService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GroupingOrderTableServiceTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    TableGroupRepository tableGroupRepository;

    @Autowired
    GroupingTableService groupingTableService;

    @Test
    void group_메서드는_지정한_orderTable을_group한다() {
        // given
        final OrderTable persistOrderTable1 = orderTableRepository.save(new OrderTable(0, true));
        final OrderTable persistOrderTable2 = orderTableRepository.save(new OrderTable(0, true));
        final TableGroup persistTableGroup = tableGroupRepository.save(new TableGroup());

        // when
        groupingTableService.group(
                List.of(persistOrderTable1.getId(), persistOrderTable2.getId()),
                persistTableGroup
        );

        // then
        assertAll(
                () -> assertThat(persistOrderTable1.getTableGroupId()).isEqualTo(persistTableGroup.getId()),
                () -> assertThat(persistOrderTable2.getTableGroupId()).isEqualTo(persistTableGroup.getId())
        );
    }

    @Test
    void group_메서드는_전달한_orderTable의_empty가_비어있지_않다면_예외가_발생한다() {
        // given
        final OrderTable persistOrderTable1 = orderTableRepository.save(new OrderTable(0, false));
        final OrderTable persistOrderTable2 = orderTableRepository.save(new OrderTable(0, false));
        final TableGroup persistTableGroup = tableGroupRepository.save(new TableGroup());
        final List<Long> invalidOrderTableIds = List.of(persistOrderTable1.getId(), persistOrderTable2.getId());

        // when & then
        assertThatThrownBy(() -> groupingTableService.group(invalidOrderTableIds, persistTableGroup))
                .isInstanceOf(InvalidEmptyOrderTableException.class);
    }

    @Test
    void group_메서드는_전달한_orderTable의_id_크기가_2이하면_예외가_발생한다() {
        // given
        final TableGroup persistTableGroup = tableGroupRepository.save(new TableGroup());

        // when & then
        assertThatThrownBy(() -> groupingTableService.group(Collections.emptyList(), persistTableGroup))
                .isInstanceOf(InvalidOrderTableSizeException.class);
    }

    @Test
    void ungroup_메서드는_지정한_tableGroup을_해제한다() {
        // given
        final Menu persistMenu = persistMenu();
        final OrderTable persistOrderTable1 = orderTableRepository.save(new OrderTable(0, true));
        final OrderTable persistOrderTable2 = orderTableRepository.save(new OrderTable(0, true));
        persistOrder(persistOrderTable1, persistMenu, OrderStatus.COMPLETION);
        persistOrder(persistOrderTable2, persistMenu, OrderStatus.COMPLETION);
        final TableGroup persistTableGroup = tableGroupRepository.save(new TableGroup());
        persistOrderTable1.group(persistTableGroup.getId());
        persistOrderTable2.group(persistTableGroup.getId());

        // when
        groupingTableService.ungroup(persistTableGroup);

        // then
        assertAll(
                () -> assertThat(persistOrderTable1.getTableGroupId()).isNull(),
                () -> assertThat(persistOrderTable1.isEmpty()).isTrue(),
                () -> assertThat(persistOrderTable2.getTableGroupId()).isNull(),
                () -> assertThat(persistOrderTable2.isEmpty()).isTrue()
        );
    }

    @Test
    void ungroup_메서드는_order의_상태가_COMPLETION이_아니라면_예외가_발생한다() {
        // given
        final Menu persistMenu = persistMenu();
        final OrderTable persistOrderTable1 = orderTableRepository.save(new OrderTable(0, true));
        final OrderTable persistOrderTable2 = orderTableRepository.save(new OrderTable(0, true));
        persistOrder(persistOrderTable1, persistMenu, OrderStatus.COOKING);
        persistOrder(persistOrderTable2, persistMenu, OrderStatus.COOKING);
        final TableGroup persistTableGroup = tableGroupRepository.save(new TableGroup());
        persistOrderTable1.group(persistTableGroup.getId());
        persistOrderTable2.group(persistTableGroup.getId());

        // when & then
        assertThatThrownBy(() -> groupingTableService.ungroup(persistTableGroup))
                .isInstanceOf(InvalidOrderTableStatusException.class);
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

    private Order persistOrder(
            final OrderTable persistOrderTable,
            final Menu persistMenu,
            final OrderStatus orderStatus
    ) {
        final OrderLineItem persistOrderLineItem = new OrderLineItem(persistMenu, 1L);

        return orderRepository.save(
                new Order(persistOrderTable.getId(), orderStatus, LocalDateTime.now(), List.of(persistOrderLineItem))
        );
    }
}
