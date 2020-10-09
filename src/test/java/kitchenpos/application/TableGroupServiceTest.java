package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.utils.TestFixture;

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

    @DisplayName("create: 테이블 그룹 생성 테스트")
    @Test
    void createTest() {
        final OrderTable orderTable1 = TestFixture.getOrderTableWithEmpty();
        final OrderTable orderTable2 = TestFixture.getOrderTableWithEmpty();
        final TableGroup tableGroup = TestFixture.getTableGroup(orderTable1, orderTable2);

        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(tableGroupDao.save(any())).thenReturn(tableGroup);
        final TableGroup actual = tableGroupService.create(tableGroup);

        assertThat(actual.getOrderTables()).hasSize(2);
    }

    @DisplayName("create: 비어있지 않은 테이블이 있다면 예외처리")
    @Test
    void createTestByNotEmptyTable() {
        final OrderTable orderTable1 = TestFixture.getOrderTableWithEmpty();
        final OrderTable orderTable2 = TestFixture.getOrderTableWithNotEmpty();
        final TableGroup tableGroup = TestFixture.getTableGroup(orderTable1, orderTable2);

        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("ungroup: 테이블 그룹 해제 테스트")
    @Test
    void ungroupTest() {
        final OrderTable orderTable1 = TestFixture.getOrderTableWithEmpty();
        final OrderTable orderTable2 = TestFixture.getOrderTableWithEmpty();

        when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(false);
        tableGroupService.ungroup(1L);
    }

    @DisplayName("ungroup: 테이블 상태가 Cocking이거나 Meal일 경우에는 예외처리")
    @Test
    void ungroupTestByStatusEqualsCokingAndMeal() {
        final OrderTable orderTable1 = TestFixture.getOrderTableWithEmpty();
        final OrderTable orderTable2 = TestFixture.getOrderTableWithEmpty();

        when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
