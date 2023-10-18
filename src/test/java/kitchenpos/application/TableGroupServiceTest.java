package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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

    @Nested
    class 테이블_그룹_생성 {
        @Test
        void 테이블_그룹을_생성한다() {
            // given
            final OrderTable savedOrderTable1 = new OrderTable();
            final OrderTable savedOrderTable2 = new OrderTable();
            savedOrderTable1.setEmpty(true);
            savedOrderTable2.setEmpty(true);

            final TableGroup savedTabledGroup = new TableGroup();
            savedTabledGroup.setId(1L);

            when(orderTableDao.findAllByIdIn(any()))
                    .thenReturn(List.of(savedOrderTable1, savedOrderTable2));
            when(tableGroupDao.save(any(TableGroup.class)))
                    .thenReturn(savedTabledGroup);
            when(orderTableDao.save(any(OrderTable.class)))
                    .thenReturn(null);

            // when
            final TableGroup tableGroup = new TableGroup();
            final OrderTable orderTable1 = new OrderTable();
            final OrderTable orderTable2 = new OrderTable();
            orderTable1.setId(1L);
            orderTable2.setId(2L);
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            final TableGroup result = tableGroupService.create(tableGroup);
            final List<OrderTable> orderTablesResult = result.getOrderTables();

            // then
            assertAll(
                    () -> assertThat(result.getId()).isEqualTo(1),
                    () -> assertThat(orderTablesResult).hasSize(2),
                    () -> assertThat(orderTablesResult.get(0).getTableGroupId()).isEqualTo(1),
                    () -> assertThat(orderTablesResult.get(0).isEmpty()).isFalse()
            );
        }

        @Test
        void 테이블_그룹을_생성할_때_전달된_주문_테이블이_없으면_실패한다() {
            // when, then
            assertThatThrownBy(() -> tableGroupService.create(new TableGroup()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블_그룹을_생성할_때_전달된_주문_테이블의_개수가_2개보다_적으면_실패한다() {
            // given
            final TableGroup tableGroup = new TableGroup();
            final OrderTable orderTable = new OrderTable();
            tableGroup.setOrderTables(List.of(orderTable));

            // when, then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블_그룹을_생성할_때_전달된_주문_테이블이_DB에_존재하지_않으면_실패한다() {
            // given
            when(orderTableDao.findAllByIdIn(any()))
                    .thenReturn(Collections.emptyList());

            // when
            final TableGroup tableGroup = new TableGroup();

            final OrderTable orderTable1 = new OrderTable();
            final OrderTable orderTable2 = new OrderTable();
            orderTable1.setId(1L);
            orderTable2.setId(2L);
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블_그룹을_생성할_때_전달된_주문_테이블이_빈_상태가_아니면_실패한다() {
            // given
            final OrderTable savedOrderTable1 = new OrderTable();
            final OrderTable savedOrderTable2 = new OrderTable();
            savedOrderTable1.setEmpty(false);
            savedOrderTable2.setEmpty(true);

            when(orderTableDao.findAllByIdIn(any()))
                    .thenReturn(List.of(savedOrderTable1, savedOrderTable2));

            // when
            final TableGroup tableGroup = new TableGroup();
            final OrderTable orderTable1 = new OrderTable();
            final OrderTable orderTable2 = new OrderTable();
            orderTable1.setId(1L);
            orderTable2.setId(2L);
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블_그룹을_생성할_때_전달된_주문_테이블이_다른_테이블_그룹에_속해_있다면_실패한다() {
            // given
            final OrderTable savedOrderTable1 = new OrderTable();
            final OrderTable savedOrderTable2 = new OrderTable();
            savedOrderTable1.setEmpty(true);
            savedOrderTable1.setTableGroupId(1L);
            savedOrderTable2.setEmpty(true);

            when(orderTableDao.findAllByIdIn(any()))
                    .thenReturn(List.of(savedOrderTable1, savedOrderTable2));

            // when
            final TableGroup tableGroup = new TableGroup();
            final OrderTable orderTable1 = new OrderTable();
            final OrderTable orderTable2 = new OrderTable();
            orderTable1.setId(1L);
            orderTable2.setId(2L);
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_삭제 {
        @Test
        void 테이블_그룹을_삭제한다() {
            // given
            final OrderTable savedOrderTable1 = new OrderTable();
            final OrderTable savedOrderTable2 = new OrderTable();
            savedOrderTable1.setId(1L);
            savedOrderTable2.setId(2L);

            when(orderTableDao.findAllByTableGroupId(anyLong()))
                    .thenReturn(List.of(savedOrderTable1, savedOrderTable2));
            when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
                    .thenReturn(false);
            when(orderTableDao.save(any(OrderTable.class)))
                    .thenReturn(null);

            // when
            tableGroupService.ungroup(1L);

            // then
            verify(orderTableDao, times(2))
                    .save(any(OrderTable.class));
        }

        @Test
        void 테이블_그룹을_삭제할_때_그룹에_속한_주문_테이블_중_하나라도_COOKING이나_MEAL_상태면_실패한다() {
            // given
            final OrderTable savedOrderTable1 = new OrderTable();
            final OrderTable savedOrderTable2 = new OrderTable();
            savedOrderTable1.setId(1L);
            savedOrderTable2.setId(2L);

            when(orderTableDao.findAllByTableGroupId(anyLong()))
                    .thenReturn(List.of(savedOrderTable1, savedOrderTable2));
            when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
                    .thenReturn(true);

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}