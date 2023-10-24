package kitchenpos.application;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.TableGroup;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.order.repository.OrderTableRepository;
import kitchenpos.domain.order.service.TableService;
import kitchenpos.domain.order.service.dto.OrderTableCreateRequest;
import kitchenpos.domain.order.service.dto.OrderTableResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
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
            final OrderTableCreateRequest request = new OrderTableCreateRequest(6, false);
            final OrderTable spyOrderTable = spy(new OrderTable(null, request.getNumberOfGuests(), request.getEmpty()));
            given(orderTableRepository.save(any(OrderTable.class))).willReturn(spyOrderTable);
            final long savedId = 1L;
            given(spyOrderTable.getId()).willReturn(savedId);

            // when
            final OrderTableResponse actual = tableService.create(request);

            // then
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
                    () -> assertThat(actual.getEmpty()).isEqualTo(request.getEmpty())
            );
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
            final OrderTable chnagedOrderTable = new OrderTable(5, false);

            final OrderTable expected = spy(new OrderTable(1, false));
            final long orderTableId = 1L;
            given(orderTableRepository.findById(orderTableId)).willReturn(Optional.ofNullable(expected));

            // when
            final OrderTable actual = tableService.changeNumberOfGuests(orderTableId, chnagedOrderTable);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(5);
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
