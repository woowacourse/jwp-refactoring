package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.annotation.MockTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@MockTest
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @InjectMocks
    private TableGroupService tableGroupService;

    @Nested
    class 테이블_그룹_저장 {

        @Test
        void 요청된_테이블의_수가_2개_미만이면_예외를_발생한다() {
            // given
            TableGroup tableGroup = new TableGroup();

            // then: size 0
            tableGroup.setOrderTables(List.of());
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);

            // then: size 1
            tableGroup.setOrderTables(List.of(new OrderTable()));
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청된_테이블들이_DB에_저장되어_있지_않으면_예외를_발생한다() {
            // given
            TableGroup tableGroup = new TableGroup();
            OrderTable orderTable1 = new OrderTable();
            OrderTable orderTable2 = new OrderTable();
            OrderTable orderTable3 = new OrderTable();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2, orderTable3));

            List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toList());
            when(orderTableDao.findAllByIdIn(orderTableIds)).thenReturn(List.of());

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청된_모든_테이블들이_빈_테이블이_아니면_예외를_발생한다() {
            // given
            TableGroup tableGroup = new TableGroup();
            OrderTable orderTable1 = new OrderTable();
            orderTable1.setEmpty(false);
            OrderTable orderTable2 = new OrderTable();
            OrderTable orderTable3 = new OrderTable();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2, orderTable3));

            List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toList());
            when(orderTableDao.findAllByIdIn(orderTableIds)).thenReturn(tableGroup.getOrderTables());

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청된_모든_테이블들이_이미_다른_테이블_그룹이_저장되어_있으면_예외를_발생한다() {
            // given
            TableGroup tableGroup = new TableGroup();
            OrderTable orderTable1 = new OrderTable();
            orderTable1.setTableGroupId(3L);
            OrderTable orderTable2 = new OrderTable();
            OrderTable orderTable3 = new OrderTable();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2, orderTable3));

            List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toList());
            when(orderTableDao.findAllByIdIn(orderTableIds)).thenReturn(tableGroup.getOrderTables());

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청된_모든_테이블에_테이블_그룹_정보를_저장하고_주문을_등록할_수_있는_상태로_변경한다() {
            // given
            TableGroup tableGroup = new TableGroup();
            OrderTable orderTable1 = new OrderTable();
            OrderTable orderTable2 = new OrderTable();
            OrderTable orderTable3 = new OrderTable();
            orderTable1.setEmpty(true);
            orderTable2.setEmpty(true);
            orderTable3.setEmpty(true);
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2, orderTable3));

            List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toList());
            when(orderTableDao.findAllByIdIn(orderTableIds)).thenReturn(tableGroup.getOrderTables());

            when(tableGroupDao.save(any(TableGroup.class))).thenReturn(tableGroup);

            // when
            TableGroup result = tableGroupService.create(tableGroup);

            // then
            for (OrderTable orderTable : result.getOrderTables()) {
                assertThat(orderTable.getTableGroupId()).isEqualTo(result.getId());
                assertThat(orderTable.isEmpty()).isFalse();
            }
        }
    }

    @Nested
    class 테이블_그룹_헤제 {

        @Test
        void 요청된_테이블_그룹_ID로_묶인_주문_테이블들의_상태가_하나라도_COOKING이거나_MEAL이면_예외를_발생한다() {
            // given
            long tableGroupId = 1L;

            OrderTable orderTable1 = new OrderTable();
            OrderTable orderTable2 = new OrderTable();
            orderTable1.setId(1L);
            orderTable2.setId(2L);
            List<OrderTable> orderTables = List.of(orderTable1, orderTable2);
            when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(orderTables);

            List<Long> orderTableIds = orderTables.stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toList());

            // when
            when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                    orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
            ).thenReturn(true);

            // then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 요청된_테이블_그룹_ID로_묶인_주문_테이블들을_빈_테이블로_만들어준다() {
            // given
            long tableGroupId = 1L;

            OrderTable orderTable1 = new OrderTable();
            OrderTable orderTable2 = new OrderTable();
            orderTable1.setId(1L);
            orderTable2.setId(2L);
            List<OrderTable> orderTables = List.of(orderTable1, orderTable2);
            when(orderTableDao.findAllByTableGroupId(tableGroupId)).thenReturn(orderTables);

            List<Long> orderTableIds = orderTables.stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toList());

            when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                    orderTableIds, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
            ).thenReturn(false);

            // when
            tableGroupService.ungroup(tableGroupId);

            // then
            for (OrderTable orderTable : orderTables) {
                assertThat(orderTable.getTableGroupId()).isNull();
                assertThat(orderTable.isEmpty()).isFalse();
            }
        }
    }
}
