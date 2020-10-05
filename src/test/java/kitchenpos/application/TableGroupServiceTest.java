package kitchenpos.application;

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
import kitchenpos.utils.TextFixture;

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
        final OrderTable orderTable1 = TextFixture.getOrderTable();
        final OrderTable orderTable2 = TextFixture.getOrderTable();
        final TableGroup tableGroup = TextFixture.getTableGroup(orderTable1, orderTable2);

        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(tableGroupDao.save(any())).thenReturn(tableGroup);
        tableGroupService.create(tableGroup);
    }

    @DisplayName("ungroup: 테이블 그룹 해제 테스트")
    @Test
    void ungroupTest() {
        final OrderTable orderTable1 = TextFixture.getOrderTable();
        final OrderTable orderTable2 = TextFixture.getOrderTable();

        when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(false);
        tableGroupService.ungroup(1L);
    }

    @DisplayName("ungroup: 테이블 그룹 해제 테스트")
    @Test
    void ungroupTestByExOrderStatus() {
        final OrderTable orderTable1 = TextFixture.getOrderTable();
        final OrderTable orderTable2 = TextFixture.getOrderTable();

        when(orderTableDao.findAllByTableGroupId(anyLong())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(false);
        tableGroupService.ungroup(1L);
    }
}
