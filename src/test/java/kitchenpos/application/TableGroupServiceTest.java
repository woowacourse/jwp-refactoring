package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFactory.createEmptyTable;
import static kitchenpos.fixture.OrderTableFactory.createOrderTable;
import static kitchenpos.fixture.TableGroupFactory.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
    OrderTableDao orderTableDao;

    @Autowired
    TableGroupDao tableGroupDao;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    TableGroupService sut;

    @Test
    @DisplayName("주문 테이블이 없을 경우 단체 지정을 할 수 없다")
    void cannotCreateTableGroup_WhenNoOrderTable() {
        // given
        TableGroup tableGroup = createTableGroup(Collections.emptyList());

        // when && then
        assertThatThrownBy(() -> sut.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블의 수는 최소 2개 이상이어야 합니다");
    }

    @Test
    @DisplayName("주문 테이블이 두 개 이하일 경우 단체 지정을 할 수 없다")
    void cannotCreateTableGroup_WhenSizeLessThanTwo() {
        // given
        OrderTable orderTable = orderTableDao.save(createEmptyTable());
        TableGroup tableGroup = createTableGroup(List.of(orderTable));

        // when && then
        assertThatThrownBy(() -> sut.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블의 수는 최소 2개 이상이어야 합니다");
    }

    @Test
    @DisplayName("입력된 주문 테이블 수와 조회된 주문 테이블 수가 일치하지 않으면 단체 지정을 할 수 없다")
    void cannotCreateTableGroup_WhenOrderTableSize_DoesNotMatch() {
        // given
        OrderTable notSavedOrderTable = createEmptyTable();
        OrderTable orderTable = orderTableDao.save(createEmptyTable());

        TableGroup tableGroup = createTableGroup(List.of(orderTable, notSavedOrderTable));

        // when && then
        assertThatThrownBy(() -> sut.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 테이블 개수가 일치하지 않습니다");
    }

    @Test
    @DisplayName("비어있지 않은 주문 테이블이 하나라도 있는 경우 단체 지정을 할 수 없다")
    void cannotCreateTableGroup_WhenAnyOrderTable_IsNotEmpty() {
        // given
        OrderTable orderTable1 = orderTableDao.save(createEmptyTable());
        OrderTable orderTable2 = orderTableDao.save(createOrderTable(1, false));

        TableGroup tableGroup = createTableGroup(List.of(orderTable1, orderTable2));

        // when && then
        assertThatThrownBy(() -> sut.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 주문 테이블입니다");
    }

    @Test
    @DisplayName("이미 단체로 지정된 주문 테이블이 하나라도 있으면 단체 지정을 할 수 없다")
    void cannotCreateTableGroup_WhenAnyOrderTable_IsAlreadyGrouped() {
        // given
        TableGroup defaultTableGroup = sut.create(newTableGroup());
        OrderTable alreadyGrouped = defaultTableGroup.getOrderTables().get(0);
        OrderTable orderTable = orderTableDao.save(createEmptyTable());

        TableGroup tableGroup = createTableGroup(List.of(alreadyGrouped, orderTable));

        // when && then
        assertThatThrownBy(() -> sut.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 주문 테이블입니다");
    }

    @Test
    @DisplayName("주문 테이블 단체를 지정한다")
    void testCreateTableGroup() {
        // given
        OrderTable orderTable1 = orderTableDao.save(createEmptyTable());
        OrderTable orderTable2 = orderTableDao.save(createEmptyTable());
        TableGroup tableGroup = createTableGroup(List.of(orderTable1, orderTable2));

        // when
        TableGroup savedTableGroup = sut.create(tableGroup);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getCreatedDate()).isBeforeOrEqualTo(LocalDateTime.now());
        assertThatTableGroupIdAndEmptyIsSet(savedTableGroup.getOrderTables(), savedTableGroup.getId());
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @DisplayName("테이블 단체에 속한 주문 테이블의 주문 상태가 MEAL 또는 COOKING이면 그룹 해제를 할 수 없다")
    void cannotUngroup_WhenIncludedOrderTables_MEAL_or_COOKING(OrderStatus orderStatus) {
        // given
        TableGroup tableGroup = sut.create(newTableGroup());
        List<OrderTable> orderTables = tableGroup.getOrderTables();

//        for (OrderTable orderTable : orderTables) {
//            Order order = new Order();
//            order.setOrderTableId(orderTable.getId());
//            order.setOrderedTime(LocalDateTime.now());
//            order.setOrderStatus(orderStatus.name());
//            order.setOrderLineItems(List.of(newOrderLineItem()));
//            orderDao.save(order);
//        }

        // when && then
        assertThatThrownBy(() -> sut.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 주문 상태입니다");
    }

    @Test
    @DisplayName("테이블 단체를 해제한다")
    void ungroup() {
        // given
        TableGroup tableGroup = sut.create(newTableGroup());

        // when
        sut.ungroup(tableGroup.getId());

        // then
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(tableGroup.getId());
        assertThat(orderTables).isEmpty();
    }

    private void assertThatTableGroupIdAndEmptyIsSet(List<OrderTable> orderTables, Long tableGroupId) {
        for (OrderTable orderTable : orderTables) {
            assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroupId);
            assertThat(orderTable.isEmpty()).isFalse();
        }
    }

    private TableGroup newTableGroup() {
        return createTableGroup(List.of(
                orderTableDao.save(createEmptyTable()),
                orderTableDao.save(createEmptyTable())));
    }
}
