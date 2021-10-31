package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체석을 생성한다.")
    @Test
    void create() {
        //given
        OrderTable tableA = orderTableConstructor(1L, null, 4, true);
        OrderTable tableB = orderTableConstructor(2L, null, 2, true);
        OrderTable tableC = orderTableConstructor(3L, null, 6, true);

        List<OrderTable> orderTables = Arrays.asList(
            tableA,
            tableB,
            tableC
        );
        TableGroup tableGroup = tableGroupConstructor(orderTables);
        TableGroup expected = tableGroupConstructor(1L, LocalDateTime.now(), orderTables);

        given(orderTableDao.findAllByIdIn(Arrays.asList(tableA.getId(), tableB.getId(), tableC.getId()))).willReturn(orderTables);
        given(tableGroupDao.save(tableGroup)).willReturn(expected);
        given(orderTableDao.save(tableA)).willReturn(tableA);
        given(orderTableDao.save(tableB)).willReturn(tableB);
        given(orderTableDao.save(tableC)).willReturn(tableC);

        //when
        TableGroup actual = tableGroupService.create(tableGroup);

        //then
        assertEquals(actual, expected);
    }

    @DisplayName("주문 테이블이 비어있으면 예외가 발생한다.")
    @Test
    void createWhenOrderTableEmpty() {
        //given
        List<OrderTable> orderTables = Collections.emptyList();
        TableGroup tableGroup = tableGroupConstructor(orderTables);

        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 크기가 2보다 작으면 예외가 발생한다.")
    @Test
    void createWhenOrderTableSizeLessThanTwo() {
        //given
        OrderTable table = orderTableConstructor(1L, null, 4, true);

        List<OrderTable> orderTables = Collections.singletonList(table);
        TableGroup tableGroup = tableGroupConstructor(orderTables);

        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 크기와 저장된 주문 테이블의 크기가 다르면 예외가 발생한다.")
    @Test
    void createWhenDifferentOrderTableSize() {
        //given
        OrderTable tableA = orderTableConstructor(1L, null, 4, true);
        OrderTable tableB = orderTableConstructor(2L, null, 2, true);
        OrderTable tableC = orderTableConstructor(3L, null, 6, true);

        List<OrderTable> orderTables = Arrays.asList(
            tableA,
            tableB,
            tableC
        );
        TableGroup tableGroup = tableGroupConstructor(orderTables);

        given(orderTableDao.findAllByIdIn(Arrays.asList(tableA.getId(), tableB.getId(), tableC.getId()))).willReturn(Collections.emptyList());

        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장된 주문 테이블의 그룹 아이디가 존재하면 예외가 발생한다.")
    @Test
    void createWhenOrderTableHasGroupId() {
        //given
        Long tableGroupId = 6L;
        OrderTable tableA = orderTableConstructor(1L, tableGroupId, 4, true);
        OrderTable tableB = orderTableConstructor(2L, tableGroupId, 2, true);
        OrderTable tableC = orderTableConstructor(3L, tableGroupId, 6, true);

        List<OrderTable> orderTables = Arrays.asList(
            tableA,
            tableB,
            tableC
        );
        TableGroup tableGroup = tableGroupConstructor(orderTables);

        given(orderTableDao.findAllByIdIn(Arrays.asList(tableA.getId(), tableB.getId(), tableC.getId()))).willReturn(orderTables);

        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체석을 해제한다.")
    @Test
    void ungroup() {
        //given
        Long tableGroupId = 10L;
        OrderTable tableA = orderTableConstructor(1L, tableGroupId, 4, true);
        OrderTable tableB = orderTableConstructor(2L, tableGroupId, 2, true);
        OrderTable tableC = orderTableConstructor(3L, tableGroupId, 6, true);

        List<OrderTable> orderTables = Arrays.asList(
            tableA,
            tableB,
            tableC
        );
        List<Long> orderTableIds = Arrays.asList(tableA.getId(), tableB.getId(), tableC.getId());

        given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .willReturn(false);
        given(orderTableDao.save(tableA)).willReturn(tableA);
        given(orderTableDao.save(tableB)).willReturn(tableB);
        given(orderTableDao.save(tableC)).willReturn(tableC);

        //when
        tableGroupService.ungroup(tableGroupId);

        //then
        assertThat(tableA.getTableGroupId()).isNull();
        assertThat(tableB.getTableGroupId()).isNull();
        assertThat(tableC.getTableGroupId()).isNull();
        assertFalse(tableA.isEmpty());
        assertFalse(tableB.isEmpty());
        assertFalse(tableC.isEmpty());
    }

    @DisplayName("요리중이거나 식사중인 단체석을 해제하면 예외가 발생한다.")
    @Test
    void ungroupWhenCookingOrMeal() {
        //given
        Long tableGroupId = 10L;
        OrderTable tableA = orderTableConstructor(1L, tableGroupId, 4, true);
        OrderTable tableB = orderTableConstructor(2L, tableGroupId, 2, true);
        OrderTable tableC = orderTableConstructor(3L, tableGroupId, 6, true);

        List<OrderTable> orderTables = Arrays.asList(
            tableA,
            tableB,
            tableC
        );
        List<Long> orderTableIds = Arrays.asList(tableA.getId(), tableB.getId(), tableC.getId());

        given(orderTableDao.findAllByTableGroupId(tableGroupId)).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
            orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .willReturn(true);

        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    private TableGroup tableGroupConstructor(final List<OrderTable> orderTables) {
        return tableGroupConstructor(null, LocalDateTime.now(), orderTables);
    }

    private TableGroup tableGroupConstructor(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(id);
        tableGroup.setCreatedDate(createdDate);
        tableGroup.setOrderTables(orderTables);

        return tableGroup;
    }

    private OrderTable orderTableConstructor(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }
}
