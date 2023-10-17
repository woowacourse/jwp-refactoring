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
        TableGroup 단체테이블 = TableGroupFixtures.단체테이블();
        단체테이블.setOrderTables(Collections.EMPTY_LIST);
        assertThatThrownBy(() -> tableGroupService.create(단체테이블)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_리스트_개수는_1개_이하일_수_없습니다() {
        TableGroup 단체테이블 = TableGroupFixtures.단체테이블();
        단체테이블.setOrderTables(List.of(OrderTableFixtures.빈테이블3번()));
        assertThatThrownBy(() -> tableGroupService.create(단체테이블)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_개수와_실제_저장된_테이블의_개수가_다르면_예외를_반환한다() {
        TableGroup 단체테이블 = TableGroupFixtures.단체테이블();
        OrderTable 빈테이블3번 = OrderTableFixtures.빈테이블3번();
        OrderTable 빈테이블4번 = OrderTableFixtures.빈테이블4번();
        단체테이블.setOrderTables(List.of(빈테이블3번, 빈테이블4번));

        when(orderTableDao.findAllByIdIn(List.of(빈테이블3번.getId(), 빈테이블4번.getId()))).thenReturn(Collections.EMPTY_LIST);

        assertThatThrownBy(() -> tableGroupService.create(단체테이블)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 저장된_주문_테이블은_empty일_수_없다() {
        TableGroup 단체테이블 = TableGroupFixtures.단체테이블();
        OrderTable 주문테이블1번 = OrderTableFixtures.주문테이블1번();
        OrderTable 주문테이블2번 = OrderTableFixtures.주문테이블2번();
        단체테이블.setOrderTables(List.of(주문테이블1번, 주문테이블2번));

        when(orderTableDao.findAllByIdIn(List.of(1L, 2L))).thenReturn(List.of(주문테이블1번, 주문테이블2번));

        assertThatThrownBy(() -> tableGroupService.create(단체테이블)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 저장된_주문_테이블은_테이블_그룹이_존재해야한다() {
        TableGroup 단체테이블 = TableGroupFixtures.단체테이블();
        OrderTable 주문테이블1번 = OrderTableFixtures.주문테이블1번();
        OrderTable 주문테이블2번 = OrderTableFixtures.주문테이블2번();
        단체테이블.setOrderTables(List.of(주문테이블1번, 주문테이블2번));

        when(orderTableDao.findAllByIdIn(List.of(주문테이블1번.getId(), 주문테이블2번.getId()))).thenReturn(List.of(주문테이블1번, 주문테이블2번));

        assertThatThrownBy(() -> tableGroupService.create(단체테이블)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_테이블_생성할_수_있다() {
        TableGroup 단체테이블 = TableGroupFixtures.단체테이블();
        OrderTable 빈테이블3번 = OrderTableFixtures.빈테이블3번();
        OrderTable 빈테이블4번 = OrderTableFixtures.빈테이블4번();
        단체테이블.setOrderTables(List.of(빈테이블3번, 빈테이블4번));

        when(orderTableDao.findAllByIdIn(List.of(빈테이블3번.getId(), 빈테이블4번.getId()))).thenReturn(List.of(빈테이블3번, 빈테이블4번));
        when(tableGroupDao.save(단체테이블)).thenReturn(단체테이블);

        tableGroupService.create(단체테이블);

        verify(tableGroupDao).save(단체테이블);
        verify(orderTableDao, times(2)).save(any(OrderTable.class));
    }

    @Test
    void COOKING_MEAL_상태일때는_삭제할_수_없다() {
        OrderTable 빈테이블3번 = OrderTableFixtures.빈테이블3번();
        long tableGroupId = 3L;
        when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(List.of(빈테이블3번));

        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                List.of(tableGroupId),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_테이블_삭제할_수_있다() {
        OrderTable orderTable = OrderTableFixtures.빈테이블3번();
        long tableGroupId = 3L;
        when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(List.of(orderTable));

        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                List.of(tableGroupId),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).thenReturn(false);

        tableGroupService.ungroup(tableGroupId);

        verify(orderTableDao).save(orderTable);
    }
}
