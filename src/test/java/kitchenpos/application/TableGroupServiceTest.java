package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.exception.TableGroupServiceException.CannotAssignOrderTableException;
import kitchenpos.application.exception.TableGroupServiceException.ExistsNotCompletionOrderException;
import kitchenpos.application.exception.TableGroupServiceException.InsufficientOrderTableSizeException;
import kitchenpos.application.exception.TableGroupServiceException.NotExistsOrderTableException;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    private TableGroup tableGroup = new TableGroup();
    private OrderTable orderTable1 = new OrderTable();
    private OrderTable orderTable2 = new OrderTable();

    @BeforeEach
    void init() {
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        orderTable1.setId(1L);
        orderTable2.setId(2L);

        orderTable1.setEmpty(true);
        orderTable2.setEmpty(true);

        orderTable1.setTableGroupId(null);
        orderTable2.setTableGroupId(null);
    }

    @Test
    @DisplayName("테이블 그룹을 생성할 수 있다.")
    void create_success() {
        TableGroup newTableGroup = new TableGroup();
        long savedTableGroupId = 1L;
        newTableGroup.setId(savedTableGroupId);
        List<OrderTable> foundOrderTables = List.of(orderTable1, orderTable2);
        when(orderTableDao.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId())))
                .thenReturn(foundOrderTables);
        when(tableGroupDao.save(tableGroup)).thenReturn(newTableGroup);

        // 테이블 그룹 생성 이전 상태
        assertAll(
                () -> assertThat(tableGroup.getId()).isNull(),
                () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                () -> assertThat(orderTable1.isEmpty()).isTrue(),
                () -> assertThat(tableGroup.getCreatedDate()).isNull()
        );

        LocalDateTime start = LocalDateTime.now();
        TableGroup actual = tableGroupService.create(tableGroup);
        LocalDateTime end = LocalDateTime.now();

        // 테이블 그룹 생성 이후 변경 상태
        assertAll(
                () -> assertThat(tableGroup.getCreatedDate()).isBetween(start, end),
                () -> assertThat(actual.getId()).isEqualTo(savedTableGroupId),
                () -> assertThat(actual.getOrderTables().get(0).getTableGroupId()).isEqualTo(savedTableGroupId),
                () -> assertThat(actual.getOrderTables().get(0).isEmpty()).isFalse(),
                () -> verify(orderTableDao, times(foundOrderTables.size())).save(any())
        );
    }

    @Test
    @DisplayName("테이블 그룹에 포함된 주문 테이블이 없으면 예외가 발생한다.")
    void create_fail_orderTable_size1() {
        tableGroup.setOrderTables(Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(InsufficientOrderTableSizeException.class);
    }

    @Test
    @DisplayName("테이블 그룹에 포함된 주문 테이블이 1개만 있더라도 예외가 발생한다.")
    void create_fail_orderTable_size2() {
        tableGroup.setOrderTables(List.of(orderTable1));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(InsufficientOrderTableSizeException.class);
    }

    @Test
    @DisplayName("테이블 그룹에 포함된 주문 테이블 번호는 현재 존재하는 주문 테이블 번호에 포함되어 있지 않으면 예외가 발생한다.")
    void create_fail_no_OrderTable() {
        List<OrderTable> foundOrderTables = List.of(orderTable1);
        when(orderTableDao.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId())))
                .thenReturn(foundOrderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(NotExistsOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블은 매핑된 테이블 그룹의 번호가 있으면 예외가 발생한다.")
    void create_fail_cannot_assign_tableGroup1() {
        orderTable1.setTableGroupId(100L);
        List<OrderTable> foundOrderTables = List.of(orderTable1, orderTable2);
        when(orderTableDao.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId())))
                .thenReturn(foundOrderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(CannotAssignOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블은 빈 테이블이 아니면 예외가 발생한다.")
    void create_fail_cannot_assign_tableGroup2() {
        orderTable1.setEmpty(false);

        List<OrderTable> foundOrderTables = List.of(orderTable1, orderTable2);
        when(orderTableDao.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId())))
                .thenReturn(foundOrderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(CannotAssignOrderTableException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 해제할 수 있다.")
    void ungroup_success() {
        long tableGroupId = 1L;
        orderTable1.setTableGroupId(tableGroupId);
        orderTable2.setTableGroupId(tableGroupId);

        List<OrderTable> foundOrderTables = List.of(orderTable1, orderTable2);
        when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(foundOrderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(false);

        // ungroup 이전
        assertAll(
                () -> assertThat(orderTable1.getTableGroupId()).isEqualTo(tableGroupId),
                () -> assertThat(orderTable1.isEmpty()).isTrue() // 현재 비즈니스 로직에서는 empty가 false이지만 변경사항을 파악하기 위해 true로 지정
        );

        tableGroupService.ungroup(tableGroupId);

        // ungroup 이후
        assertAll(
                () -> assertThat(orderTable1.getTableGroupId()).isNull(),
                () -> assertThat(orderTable1.isEmpty()).isFalse(),
                () -> verify(orderTableDao, times(foundOrderTables.size())).save(any())
        );
    }

    @Test
    @DisplayName("테이블 그룹에 포함된 주문 테이블 중 COOKING 상태이거나 MEAL 상태인 주문의 주문 테이블이 있으면 예외가 발생한다.")
    void ungroup_fail() {
        long tableGroupId = 1L;
        List<OrderTable> foundOrderTables = List.of(orderTable1, orderTable2);
        when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(foundOrderTables);
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(ExistsNotCompletionOrderException.class);
    }
}

