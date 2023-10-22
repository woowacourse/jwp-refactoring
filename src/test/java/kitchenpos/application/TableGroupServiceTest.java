package kitchenpos.application;

import static kitchenpos.application.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.application.fixture.TableGroupFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
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

    @DisplayName("테이블 그룹을 생성, 저장한다.")
    @Test
    void create_success() {
        // given
        final OrderTable orderTable1 = createOrderTable(1L, 3);
        orderTable1.updateEmpty(true);
        final OrderTable orderTable2 = createOrderTable(2L, 3);
        orderTable2.updateEmpty(true);

        final TableGroup tableGroup = createTableGroup(1L, List.of(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(anyList()))
            .willReturn(List.of(orderTable1, orderTable2));
        given(tableGroupDao.save(any(TableGroup.class)))
            .willReturn(tableGroup);
        given(orderTableDao.save(any(OrderTable.class)))
            .willReturn(orderTable1)
            .willReturn(orderTable2);

        // when
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup).usingRecursiveComparison()
            .isEqualTo(tableGroup);
    }

    @DisplayName("테이블 그룹을 생성할 때, 묶을 테이블이 없으면 예외가 발생한다.")
    @Test
    void create_empty_fail() {
        // given
        final TableGroup tableGroup = createTableGroup(1L);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 생성할 때, 테이블이 2개 보다 적다면 예외가 발생한다.")
    @Test
    void create_wrongSize_fail() {
        // given
        final OrderTable orderTable1 = createOrderTable(1L, 3);
        orderTable1.updateEmpty(true);
        final TableGroup tableGroup = createTableGroup(1L, List.of(orderTable1));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 생성할 때, 존재하지 않는 테이블이 있다면 예외가 발생한다.")
    @Test
    void create_notExistOrderTable_fail() {
        // given
        final OrderTable orderTable1 = createOrderTable(1L, 3);
        final OrderTable orderTable2 = createOrderTable(2L, 3);
        final TableGroup tableGroup = createTableGroup(1L, List.of(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(anyList()))
            .willReturn(List.of(orderTable1));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 생성할 때, 비어있지 않은 테이블이 있다면 예외가 발생한다.")
    @Test
    void create_emptyTable_fail() {
        // given
        final OrderTable orderTable1 = createOrderTable(1L, 3);
        orderTable1.updateEmpty(false);
        final OrderTable orderTable2 = createOrderTable(1L, 3);
        orderTable2.updateEmpty(true);

        final TableGroup tableGroup = createTableGroup(1L, List.of(orderTable1, orderTable2));
        tableGroup.addOrderTables(List.of(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(anyList()))
            .willReturn(List.of(orderTable1, orderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 생성할 때, 이미 다른 그룹에 속한 테이블이 있다면 예외가 발생한다.")
    @Test
    void create_alreadyInOtherGroup_fail() {
        // given
        final OrderTable orderTable1 = createOrderTable(1L, 3);
        final OrderTable orderTable2 = createOrderTable(1L, 3L, 3);

        final TableGroup tableGroup = createTableGroup(1L, List.of(orderTable1, orderTable2));
        tableGroup.addOrderTables(List.of(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(anyList()))
            .willReturn(List.of(orderTable1, orderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹을 해제한다.")
    @Test
    void ungroup_success() {
        // given
        final OrderTable orderTable1 = createOrderTable(1L, 3);
        final OrderTable orderTable2 = createOrderTable(1L, 3);

        final TableGroup tableGroup = createTableGroup(1L, List.of(orderTable1, orderTable2));
        tableGroup.addOrderTables(List.of(orderTable1, orderTable2));
        orderTable1.groupBy(tableGroup);
        orderTable2.groupBy(tableGroup);

        given(orderTableDao.findAllByTableGroupId(anyLong()))
            .willReturn(List.of(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
            .willReturn(false);
        given(orderTableDao.save(any(OrderTable.class)))
            .willReturn(orderTable1, orderTable2);

        // when, then
        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
    }

    @DisplayName("그룹에 속한 테이블 중, 주문 상태가 COMPLETION 인 테이블이 있다면 예외가 발생한다.")
    @Test
    void ungroup_wrongStatus_fail() {
        // given
        final OrderTable orderTable1 = createOrderTable(1L, 3);
        final OrderTable orderTable2 = createOrderTable(1L, 3);

        final TableGroup tableGroup = createTableGroup(1L, List.of(orderTable1, orderTable2));
        tableGroup.addOrderTables(List.of(orderTable1, orderTable2));
        orderTable1.groupBy(tableGroup);
        orderTable2.groupBy(tableGroup);

        given(orderTableDao.findAllByTableGroupId(anyLong()))
            .willReturn(List.of(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
            .willReturn(true);

        // when, then
        final Long groupId = tableGroup.getId();

        assertThatThrownBy(() -> tableGroupService.ungroup(groupId))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
