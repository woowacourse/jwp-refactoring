package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Nested
    class Create {

        @Test
        void 주문_테이블을_만들_수_있다() {
            // given
            final OrderTable orderTable = new OrderTable(6, false);

            // when
            tableService.create(orderTable);

            // then
            verify(orderTableRepository, only()).save(orderTable);
        }
    }

    @Nested
    class FindAll {

        @Test
        void 주문_테이블을_전체_조회할_수_있다() {
            // when
            tableService.list();

            // then
            verify(orderTableRepository, only()).findAll();
        }
    }

    @Nested
    class ChangeEmpty {
        @Test
        void 주문_테이블을_비어있는_상태로_만들_수_있다() {
            // given
            final OrderTable spyOrderTable = spy(new OrderTable(5, false));
            given(orderTableRepository.findById(anyLong())).willReturn(Optional.ofNullable(spyOrderTable));

            final TableGroup emptyTableGroup = mock(TableGroup.class);
            given(spyOrderTable.getTableGroup()).willReturn(emptyTableGroup);
            final Long emptyTableGroupId = null;
            given(emptyTableGroup.getId()).willReturn(emptyTableGroupId);

            final long orderTableId = 1L;
            given(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(COOKING, MEAL))).willReturn(false);

            // when
            tableService.changeEmpty(orderTableId, spyOrderTable);

            // then
            verify(orderTableRepository, times(1)).save(any(OrderTable.class));
        }

        @Test
        void 단체_지정_아이디가_null이_아니면_예외를_던진다() {
            // given
            final OrderTable target = spy(new OrderTable(5, false));
            given(orderTableRepository.findById(anyLong())).willReturn(Optional.ofNullable(target));

            // when
            final TableGroup nonNullTableGroup = mock(TableGroup.class);
            final long nonNullTableGroupId = 1L;
            when(nonNullTableGroup.getId()).thenReturn(nonNullTableGroupId);
            when(target.getTableGroup()).thenReturn(nonNullTableGroup);

            // then
            final long orderTableId = 1L;
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, target))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문상태가_요리중이거나_식사중이면_예외가_발생한다() {
            // given
            final OrderTable orderTable = spy(new OrderTable(5, false));
            given(orderTableRepository.findById(anyLong())).willReturn(Optional.ofNullable(orderTable));

            final TableGroup emptyTableGroup = mock(TableGroup.class);
            given(orderTable.getTableGroup()).willReturn(emptyTableGroup);
            final Long emptyTableGroupId = null;
            given(emptyTableGroup.getId()).willReturn(emptyTableGroupId);

            final long orderTableId = 1L;
            given(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, List.of(COOKING, MEAL))).willReturn(true);

            // when, then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class ChangeNumberOfGuests {

        @Test
        void 게스트의_수를_변경할_수_있다() {
            // given
            final OrderTable target = spy(new OrderTable(5, false));
            final long orderTableId = 1L;
            given(orderTableRepository.findById(orderTableId)).willReturn(Optional.ofNullable(target));

            // when
            tableService.changeNumberOfGuests(orderTableId, target);

            // then
            verify(orderTableRepository, times(1)).save(target);
        }

        @Test
        void 게스트의_수가_0이면_예외가_발생한다() {
            // given
            final OrderTable target = spy(new OrderTable(5, false));

            // when, then
            final long orderTableId = 1L;
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, target))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_비어있으면_예외가_발생한다() {
            // given
            final OrderTable target = spy(new OrderTable(5, true));
            final long orderTableId = 1L;
            given(orderTableRepository.findById(orderTableId)).willReturn(Optional.ofNullable(target));

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, target))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
