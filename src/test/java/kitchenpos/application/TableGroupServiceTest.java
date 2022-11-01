package kitchenpos.application;

import static kitchenpos.fixture.MenuBuilder.aMenu;
import static kitchenpos.fixture.OrderTableFactory.createEmptyTable;
import static kitchenpos.fixture.OrderTableFactory.createOrderTable;
import static kitchenpos.fixture.ProductBuilder.aProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    TableGroupService sut;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    TableGroupRepository tableGroupRepository;

    @Autowired
    OrderRepository orderRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @DisplayName("주문 테이블이 없을 경우 단체 지정을 할 수 없다")
    void cannotCreateTableGroup_WhenNoOrderTable() {
        // given
        var request = new TableGroupRequest(Collections.emptyList());

        // when && then
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블의 수는 최소 2개 이상이어야 합니다");
    }

    @Test
    @DisplayName("주문 테이블이 두 개 이하일 경우 단체 지정을 할 수 없다")
    void cannotCreateTableGroup_WhenSizeLessThanTwo() {
        // given
        var orderTable = orderTableRepository.save(createEmptyTable());
        var request = new TableGroupRequest(List.of(new OrderTableRequest(orderTable.getId())));

        // when && then
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블의 수는 최소 2개 이상이어야 합니다");
    }

    @Test
    @DisplayName("입력된 주문 테이블 수와 조회된 주문 테이블 수가 일치하지 않으면 단체 지정을 할 수 없다")
    void cannotCreateTableGroup_WhenOrderTableSize_DoesNotMatch() {
        // given
        OrderTable orderTable = orderTableRepository.save(createEmptyTable());

        var request = new TableGroupRequest(
                List.of(new OrderTableRequest(0L), new OrderTableRequest(orderTable.getId())));

        // when && then
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 주문 테이블이 포함되어 있습니다");
    }

    @Test
    @DisplayName("비어있지 않은 주문 테이블이 하나라도 있는 경우 단체 지정을 할 수 없다")
    void cannotCreateTableGroup_WhenAnyOrderTable_IsNotEmpty() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(createEmptyTable());
        OrderTable orderTable2 = orderTableRepository.save(createOrderTable(1, false));

        var request = new TableGroupRequest(
                List.of(new OrderTableRequest(orderTable1.getId()), new OrderTableRequest(orderTable2.getId())));

        // when && then
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 주문 테이블입니다");
    }

    @Test
    @DisplayName("이미 단체로 지정된 주문 테이블이 하나라도 있으면 단체 지정을 할 수 없다")
    void cannotCreateTableGroup_WhenAnyOrderTable_IsAlreadyGrouped() {
        // given
        var orderTable1 = orderTableRepository.save(createEmptyTable());
        var orderTable2 = orderTableRepository.save(createEmptyTable());
        var orderTable3 = orderTableRepository.save(createEmptyTable());
        var tableGroup = tableGroupRepository.save(new TableGroup(List.of(orderTable1, orderTable2)));

        var request = new TableGroupRequest(
                List.of(new OrderTableRequest(orderTable1.getId()), new OrderTableRequest(orderTable3.getId()))
        );

        // when && then
        assertThatThrownBy(() -> sut.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 주문 테이블입니다");
    }

    @Test
    @DisplayName("주문 테이블 단체를 지정한다")
    void testCreateTableGroup() {
        // given
        var orderTable1 = orderTableRepository.save(createEmptyTable());
        var orderTable2 = orderTableRepository.save(createEmptyTable());
        var request = new TableGroupRequest(
                List.of(new OrderTableRequest(orderTable1.getId()), new OrderTableRequest(orderTable2.getId()))
        );

        // when
        var response = sut.create(request);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getCreatedDate()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThatTableGroupIdAndEmptyIsSet(response.getOrderTables(), response.getId());
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @DisplayName("테이블 단체에 속한 주문 테이블의 주문 상태가 MEAL 또는 COOKING이면 그룹 해제를 할 수 없다")
    void cannotUngroup_WhenIncludedOrderTables_MEAL_or_COOKING(OrderStatus orderStatus) {
        // given
        var orderTable1 = orderTableRepository.save(createOrderTable(1, false));
        var orderTable2 = orderTableRepository.save(createOrderTable(1, false));

        var menuGroupId = menuGroupRepository.save(new MenuGroup("후라이드 치킨")).getId();
        var product = productRepository.save(aProduct().build());
        var menu = menuRepository.save(aMenu(menuGroupId)
                .withMenuProducts(List.of(new MenuProduct(product, 1L)))
                .build());
        var order1 = orderRepository.save(new Order(orderTable1, List.of(new OrderLineItem(menu.getId(), 1L))));
        var order2 = orderRepository.save(new Order(orderTable2, List.of(new OrderLineItem(menu.getId(), 1L))));
        order1.changeStatus(orderStatus);
        order2.changeStatus(OrderStatus.COMPLETION);
        orderTable1.changeEmpty(true);
        orderTable2.changeEmpty(true);
        entityManager.flush();

        var tableGroup = tableGroupRepository.save(new TableGroup(List.of(orderTable1, orderTable2)));

        // when && then
        assertThatThrownBy(() -> sut.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 주문 상태입니다");
    }

    @Test
    @DisplayName("테이블 단체를 해제한다")
    void ungroup() {
        // given
        var orderTable1 = orderTableRepository.save(createOrderTable(1, false));
        var orderTable2 = orderTableRepository.save(createOrderTable(1, false));

        var menuGroupId = menuGroupRepository.save(new MenuGroup("후라이드 치킨")).getId();
        var product = productRepository.save(aProduct().build());
        var menu = menuRepository.save(aMenu(menuGroupId)
                .withMenuProducts(List.of(new MenuProduct(product, 1L)))
                .build());
        var order1 = orderRepository.save(new Order(orderTable1, List.of(new OrderLineItem(menu.getId(), 1L))));
        var order2 = orderRepository.save(new Order(orderTable2, List.of(new OrderLineItem(menu.getId(), 1L))));
        order1.changeStatus(OrderStatus.COMPLETION);
        order2.changeStatus(OrderStatus.COMPLETION);
        orderTable1.changeEmpty(true);
        orderTable2.changeEmpty(true);
        var tableGroup = tableGroupRepository.save(new TableGroup(List.of(orderTable1, orderTable2)));

        // when
        sut.ungroup(tableGroup.getId());

        // then
        var findTableGroup = tableGroupRepository.findById(tableGroup.getId()).get();
        assertThat(findTableGroup.getOrderTables()).isEmpty();
    }

    private void assertThatTableGroupIdAndEmptyIsSet(List<OrderTableResponse> orderTables, Long tableGroupId) {
        for (var orderTable : orderTables) {
            assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroupId);
            assertThat(orderTable.isEmpty()).isFalse();
        }
    }
}
