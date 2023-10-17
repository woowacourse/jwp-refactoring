package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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

    @Test
    void 주문_테이블_리스트는_빈값일_수_없습니다() {
        TableGroup tableGroup = TableGroupFixtures.단체지정();
        tableGroup.setOrderTables(Collections.EMPTY_LIST);
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_리스트_개수는_1개_이하일_수_없습니다() {
        TableGroup tableGroup = TableGroupFixtures.단체지정();
        tableGroup.setOrderTables(List.of(new OrderTable()));
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_개수와_실제_저장된_테이블의_개수가_다르면_예외를_반환한다() {
        TableGroup tableGroup = TableGroupFixtures.단체지정();
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        when(orderTableDao.findAllByIdIn(List.of(1L, 2L))).thenReturn(Collections.EMPTY_LIST);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 저장된_주문_테이블은_empty일_수_없다() {
        TableGroup tableGroup = TableGroupFixtures.단체지정();
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(false);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(false);
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        when(orderTableDao.findAllByIdIn(List.of(1L, 2L))).thenReturn(List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 저장된_주문_테이블은_테이블_그룹이_존재해야한다() {
        TableGroup tableGroup = TableGroupFixtures.단체지정();
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(3L);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        when(orderTableDao.findAllByIdIn(List.of(1L, 2L))).thenReturn(List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_테이블_생성할_수_있다() {
        TableGroup tableGroup = TableGroupFixtures.단체지정();
        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        when(orderTableDao.findAllByIdIn(List.of(1L, 2L))).thenReturn(List.of(orderTable1, orderTable2));
        when(tableGroupDao.save(tableGroup)).thenReturn(tableGroup);

        tableGroupService.create(tableGroup);

        verify(tableGroupDao).save(tableGroup);
        verify(orderTableDao, times(2)).save(any(OrderTable.class));
    }

    @Test
    void COOKING_MEAL_상태일때는_삭제할_수_없다() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(2L);
        long tableGroupId = 1L;
        when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(List.of(orderTable));

        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                List.of(2L),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_테이블_삭제할_수_있다() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(2L);
        long tableGroupId = 1L;
        when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(List.of(orderTable));

        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                List.of(2L),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).thenReturn(false);

        tableGroupService.ungroup(tableGroupId);

        verify(orderTableDao).save(orderTable);
    }
}
