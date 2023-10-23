package kitchenpos.application;

import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.persistence.TableGroupRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private TableGroupRepository tableGroupRepository;

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

            final TableGroup savedTabledGroup = new TableGroup(1L, LocalDateTime.now());

            when(orderTableDao.findAllByIdIn(any()))
                    .thenReturn(List.of(savedOrderTable1, savedOrderTable2));
            when(tableGroupRepository.save(any(TableGroup.class)))
                    .thenReturn(savedTabledGroup);
            when(orderTableDao.save(any(OrderTable.class)))
                    .thenReturn(null);

            // when
            final List<OrderTableDto> request = List.of(new OrderTableDto(1L), new OrderTableDto(2L));

            final TableGroup result = tableGroupService.create(request);

            // then
            assertThat(result.getId()).isEqualTo(1);
        }

        @Test
        void 테이블_그룹을_생성할_때_전달된_주문_테이블이_없으면_실패한다() {
            // given, when
            final List<OrderTableDto> request = Collections.emptyList();

            // then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블_그룹을_생성할_때_전달된_주문_테이블의_개수가_2개보다_적으면_실패한다() {
            // given, when
            final List<OrderTableDto> request = List.of(new OrderTableDto(1L));


            // then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블_그룹을_생성할_때_전달된_주문_테이블이_DB에_존재하지_않으면_실패한다() {
            // given
            when(orderTableDao.findAllByIdIn(any()))
                    .thenReturn(Collections.emptyList());

            // when
            final List<OrderTableDto> request = List.of(new OrderTableDto(1L), new OrderTableDto(2L));


            // then
            assertThatThrownBy(() -> tableGroupService.create(request))
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
            final List<OrderTableDto> request = List.of(new OrderTableDto(1L), new OrderTableDto(2L));


            // then
            assertThatThrownBy(() -> tableGroupService.create(request))
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
            final List<OrderTableDto> request = List.of(new OrderTableDto(1L), new OrderTableDto(2L));


            // then
            assertThatThrownBy(() -> tableGroupService.create(request))
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