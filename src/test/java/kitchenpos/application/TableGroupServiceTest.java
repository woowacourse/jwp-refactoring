package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.config.ServiceTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.request.CreateOrderGroupOrderTableRequest;
import kitchenpos.ui.dto.request.CreateTableGroupRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest {

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    OrderDao orderDao;

    @Autowired
    TableGroupDao tableGroupDao;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    MenuDao menuDao;

    @Autowired
    TableGroupService tableGroupService;

    @Test
    void create_메서드는_tableGroup을_전달하면_tableGroup을_저장하고_반환한다() {
        // given
        final OrderTable persistOrderTable1 = orderTableDao.save(new OrderTable(0, true));
        final OrderTable persistOrderTable2 = orderTableDao.save(new OrderTable(0, true));
        final List<CreateOrderGroupOrderTableRequest> createOrderGroupOrderTableRequests = List.of(
                new CreateOrderGroupOrderTableRequest(persistOrderTable1.getId()),
                new CreateOrderGroupOrderTableRequest(persistOrderTable2.getId())
        );
        final CreateTableGroupRequest request = new CreateTableGroupRequest(createOrderGroupOrderTableRequests);

        // when
        final TableGroup actual = tableGroupService.create(request);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void create_메서드는_tableGroup이_비어있는_tableGroup을_전달하면_예외가_발생한다() {
        // given
        final CreateTableGroupRequest invalidRequest = new CreateTableGroupRequest(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_메서드는_tableGroup이_하나인_tableGroup을_전달하면_예외가_발생한다() {
        // given
        final OrderTable persistOrderTable = orderTableDao.save(new OrderTable(0, true));
        final List<CreateOrderGroupOrderTableRequest> createOrderGroupOrderTableRequests = List.of(
                new CreateOrderGroupOrderTableRequest(persistOrderTable.getId())
        );
        final CreateTableGroupRequest invalidRequest = new CreateTableGroupRequest(createOrderGroupOrderTableRequests);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_메서드는_tableGroup이_비어있지_않은_tableGroup을_전달하면_예외가_발생한다() {
        // given
        final OrderTable persistOrderTable1 = orderTableDao.save(new OrderTable(0, false));
        final OrderTable persistOrderTable2 = orderTableDao.save(new OrderTable(0, false));
        final List<CreateOrderGroupOrderTableRequest> createOrderGroupOrderTableRequests = List.of(
                new CreateOrderGroupOrderTableRequest(persistOrderTable1.getId()),
                new CreateOrderGroupOrderTableRequest(persistOrderTable2.getId())
        );
        final CreateTableGroupRequest invalidRequest = new CreateTableGroupRequest(createOrderGroupOrderTableRequests);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ungroup_메서드는_지정한_tableGroup의_id의_그룹을_해제한다() {
        // given
        final OrderTable persistOrderTable1 = orderTableDao.save(new OrderTable(0, true));
        final OrderTable persistOrderTable2 = orderTableDao.save(new OrderTable(0, true));
        final MenuGroup persistMenuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹"));
        final Product persistProduct = productDao.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct persistMenuProduct = new MenuProduct(persistProduct, 1);
        final Menu persistMenu = menuDao.save(Menu.of(
                "메뉴",
                BigDecimal.TEN,
                List.of(persistMenuProduct),
                persistMenuGroup)
        );
        final OrderLineItem persistOrderLineItem1 = new OrderLineItem(persistMenu, 1L);
        final OrderLineItem persistOrderLineItem2 = new OrderLineItem(persistMenu, 1L);
        final Order order1 = new Order(
                persistOrderTable1,
                OrderStatus.COMPLETION,
                LocalDateTime.now().minusHours(3),
                List.of(persistOrderLineItem1)
        );
        final Order order2 = new Order(
                persistOrderTable2,
                OrderStatus.COMPLETION,
                LocalDateTime.now().minusHours(3),
                List.of(persistOrderLineItem2)
        );
        orderDao.save(order1);
        orderDao.save(order2);
        final TableGroup tableGroup = new TableGroup(List.of(persistOrderTable1, persistOrderTable2));
        final TableGroup persistTableGroup = tableGroupDao.save(tableGroup);

        // when
        tableGroupService.ungroup(persistTableGroup.getId());

        // then
        final Optional<TableGroup> actual = tableGroupDao.findById(persistTableGroup.getId());

        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getOrderTables().get(0).getTableGroup()).isNull(),
                () -> assertThat(actual.get().getOrderTables().get(1).getTableGroup()).isNull()
        );
    }

    @ParameterizedTest(name = "orderStatus가 {0}일 때 예외가 발생한다.")
    @ValueSource(strings = {"MEAL", "COOKING"})
    void upgroup_메서드는_orderTable의_order의_상태가_COMPLETION이_아닌_tableGroup을_전달하면_예외가_발생한다(final String invalidOrderStatus) {
        // given
        final OrderTable persistOrderTable1 = orderTableDao.save(new OrderTable(0, true));
        final OrderTable persistOrderTable2 = orderTableDao.save(new OrderTable(0, true));
        final MenuGroup persistMenuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹"));
        final Product persistProduct = productDao.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct persistMenuProduct = new MenuProduct(persistProduct, 1);
        final Menu persistMenu = menuDao.save(Menu.of(
                "메뉴",
                BigDecimal.TEN,
                List.of(persistMenuProduct),
                persistMenuGroup)
        );
        final OrderStatus orderStatus = OrderStatus.valueOf(invalidOrderStatus);
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
        orderDao.save(order1);
        orderDao.save(order2);
        final TableGroup persistTableGroup = tableGroupDao.save(
                new TableGroup(List.of(persistOrderTable1, persistOrderTable2))
        );
        persistOrderTable1.group(persistTableGroup);
        persistOrderTable2.group(persistTableGroup);
        orderTableDao.save(persistOrderTable1);
        orderTableDao.save(persistOrderTable2);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(persistTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
