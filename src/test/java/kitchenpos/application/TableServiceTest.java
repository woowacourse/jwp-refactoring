package kitchenpos.application;

import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.request.OrderTableUpdateRequest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.persistence.OrderRepository;
import kitchenpos.persistence.OrderTableRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @Test
    void 주문_테이블을_생성한다() {
        // given
        final OrderTable savedOrderTable = new OrderTable(1L, null, 0, false);

        when(orderTableRepository.save(any(OrderTable.class)))
                .thenReturn(savedOrderTable);

        // when
        final OrderTableCreateRequest request = new OrderTableCreateRequest(0, false);
        final OrderTable result = tableService.create(request);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1),
                () -> assertThat(result.getTableGroup()).isNull()
        );
    }

    @Test
    void 전체_주문_테이블_목록을_가져온다() {
        // given
        when(orderTableRepository.findAll())
                .thenReturn(Collections.emptyList());

        // when
        final List<OrderTable> result = tableService.list();

        // then
        assertThat(result).isEmpty();
    }

    @Nested
    class 주문_테이블_상태_변경 {
        @Test
        void 주문_테이블의_빈_상태를_변경한다() {
            // given
            final OrderTable savedOrderTable = new OrderTable(1L, null, 0, false);

            when(orderTableRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedOrderTable));
            when(orderRepository.existsByOrderTableInAndOrderStatusIn(any(), any()))
                    .thenReturn(false);
            when(orderTableRepository.save(any(OrderTable.class)))
                    .thenReturn(savedOrderTable);

            // when
            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(null, false);
            final OrderTable result = tableService.changeEmpty(savedOrderTable.getId(), request);

            // then
            assertThat(result.isEmpty()).isFalse();
        }

        @Test
        void 주문_테이블의_빈_상태를_변경할_때_주문_테이블이_어떤_테이블_그룹에_속해_있을_때_실패한다() {
            // given
            final TableGroup tableGroup = new TableGroup(1L, null);
            final OrderTable savedOrderTable = new OrderTable(1L, tableGroup, 0, true);

            when(orderTableRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedOrderTable));

            // when
            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(null, false);

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블의_빈_상태를_변경할_때_주문_테이블의_주문_상태가_COOKING이나_MEAL이면_실패한다() {
            // given
            final OrderTable savedOrderTable = new OrderTable(1L, null, 0, false);

            when(orderTableRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedOrderTable));
            when(orderRepository.existsByOrderTableInAndOrderStatusIn(any(), any()))
                    .thenReturn(true);

            // when
            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(null, false);

            // then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 주문_테이블_사용자_수_변경 {
        @Test
        void 주문_테이블의_사용자_수를_변경한다() {
            // given
            final OrderTable savedOrderTable = new OrderTable(1L, null, 1, false);

            when(orderTableRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedOrderTable));
            when(orderTableRepository.save(any(OrderTable.class)))
                    .thenReturn(savedOrderTable);

            // when
            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(1, null);
            final OrderTable result = tableService.changeNumberOfGuests(1L, request);

            // then
            assertThat(result.getNumberOfGuests()).isEqualTo(1);
        }

        @Test
        void 주문_테이블의_사용자_수를_변경할_때_전달받은_사용자_수가_0보다_작으면_실패한다() {
            // given
            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(-1, null);

            // when, then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블의_사용자_수를_변경할_때_존재하는_주문_테이블이_아니면_실패한다() {
            // given
            when(orderTableRepository.findById(anyLong()))
                    .thenReturn(Optional.empty());

            // when
            final Long notExistTableId = 1L;
            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(1, null);

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistTableId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블의_사용자_수를_변경할_때_주문_테이블이_빈_상태면_실패한다() {
            // given
            final OrderTable savedOrderTable = new OrderTable(1L, null, 0, true);
            when(orderTableRepository.findById(anyLong()))
                    .thenReturn(Optional.of(savedOrderTable));

            // when
            final OrderTableUpdateRequest request = new OrderTableUpdateRequest(1, null);

            // then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}