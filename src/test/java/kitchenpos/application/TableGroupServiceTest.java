package kitchenpos.application;

import kitchenpos.application.dto.OrderTableDto;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import kitchenpos.persistence.TableGroupRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Nested
    class 테이블_그룹_생성 {
        @Test
        void 테이블_그룹을_생성한다() {
            // given
            final OrderTable savedOrderTable1 = new OrderTable(1L, null, 0, true);
            final OrderTable savedOrderTable2 = new OrderTable(2L, null, 0, true);
            final TableGroup savedTabledGroup = new TableGroup(1L);

            when(orderTableRepository.findAllByIdIn(any()))
                    .thenReturn(List.of(savedOrderTable1, savedOrderTable2));
            when(tableGroupRepository.save(any(TableGroup.class)))
                    .thenReturn(savedTabledGroup);

            // when
            final List<OrderTableDto> request = List.of(new OrderTableDto(1L), new OrderTableDto(2L));
            final TableGroupResponse result = tableGroupService.create(request);

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
            when(orderTableRepository.findAllByIdIn(any()))
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
            final OrderTable savedOrderTable1 = new OrderTable(null, null, 0, false);
            final OrderTable savedOrderTable2 = new OrderTable(null, null, 0, true);

            when(orderTableRepository.findAllByIdIn(any()))
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
            final TableGroup tableGroup = new TableGroup(1L);
            final OrderTable savedOrderTable1 = new OrderTable(null, tableGroup.getId(), 0, true);
            final OrderTable savedOrderTable2 = new OrderTable(null, null, 0, true);

            when(orderTableRepository.findAllByIdIn(any()))
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
        void 테이블_그룹을_해제한다() {
            // given
            final OrderTable savedOrderTable1 = new OrderTable(1L, null, 0, true);
            final OrderTable savedOrderTable2 = new OrderTable(2L, null, 0, true);
            final TableGroup savedTableGroup = new TableGroup(1L);

            when(tableGroupRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedTableGroup));
            when(orderTableRepository.findAllByTableGroupId(anyLong()))
                    .thenReturn(List.of(savedOrderTable1, savedOrderTable2));
            when(orderRepository.existsByOrderTableInAndOrderStatusIn(any(), any()))
                    .thenReturn(false);

            // when
            tableGroupService.ungroup(1L);

            // then
            verify(orderTableRepository, times(1))
                    .saveAll(any());
        }

        @Test
        void 테이블_그룹을_해제할_때_DB에_존재하지_않는_테이블_그룹_아이디가_전달될_경우_실패한다() {
            // given
            when(tableGroupRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블_그룹을_삭제할_때_그룹에_속한_주문_테이블_중_하나라도_COOKING이나_MEAL_상태면_실패한다() {
            // given
            final OrderTable savedOrderTable1 = new OrderTable(1L, null, 0, true);
            final OrderTable savedOrderTable2 = new OrderTable(2L, null, 0, true);
            final TableGroup savedTableGroup = new TableGroup(1L);

            when(tableGroupRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedTableGroup));
            when(orderTableRepository.findAllByTableGroupId(anyLong()))
                    .thenReturn(List.of(savedOrderTable1, savedOrderTable2));
            when(orderRepository.existsByOrderTableInAndOrderStatusIn(any(), any()))
                    .thenReturn(true);

            // when, then
            assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}