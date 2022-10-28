package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.request.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.application.dto.request.OrderTableChangeStatusRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @Nested
    class create는 {

        @Test
        void order_table을_저장할_수_있다() {
            // given
            OrderTableCreateRequest request = new OrderTableCreateRequest(1L, 11L, 3, true);
            when(orderTableRepository.save(any(OrderTable.class))).thenReturn(request.toOrderTable());

            // when
            OrderTableResponse response = tableService.create(request);

            // then
            assertThat(response.getNumberOfGuests()).isEqualTo(3);
        }
    }

    @Nested
    class list는 {

        @Test
        void order_table_목록을_조회할_수_있다() {
            // given
            OrderTable orderTable1 = new OrderTable();
            OrderTable orderTable2 = new OrderTable();
            when(orderTableRepository.findAll()).thenReturn(Arrays.asList(orderTable1, orderTable2));

            // when
            List<OrderTableResponse> responses = tableService.list();

            // then
            assertThat(responses).hasSize(2);
        }
    }

    @Nested
    class changeStatus는 {

        @Test
        void 일치하는_order_table_id가_없을_시_예외를_반환한다() {
            when(orderTableRepository.findById(any(Long.class))).thenReturn(Optional.empty());
            assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTableChangeStatusRequest(true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void table_group_id_값이_null이_아니면_예외를_반환한다() {
            // given
            Long notNullTableGroupId = 11L;
            OrderTable orderTable = new OrderTable(1L, notNullTableGroupId, 3, false);
            when(orderTableRepository.findById(any(Long.class))).thenReturn(Optional.of(orderTable));

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTableChangeStatusRequest(true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order_상태가_COOKING_또는_MEAL_상태이면_예외를_반환한다() {
            // given
            Long orderTableId = 1L;
            OrderTable orderTable = new OrderTable(orderTableId, null, 3, false);
            when(orderTableRepository.findById(any(Long.class))).thenReturn(Optional.of(orderTable));
            when(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                    Arrays.asList(COOKING, MEAL))).thenReturn(
                    true);

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, new OrderTableChangeStatusRequest(true)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void empty를_변경할_수_있다() {
            // given
            Long orderTableId = 1L;
            OrderTable orderTable = new OrderTable(orderTableId, null, 3, false);
            when(orderTableRepository.findById(any(Long.class))).thenReturn(Optional.of(orderTable));
            when(orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId,
                    Arrays.asList(COOKING, MEAL))).thenReturn(
                    false);
            when(orderTableRepository.save(any(OrderTable.class))).thenReturn(orderTable);
            // when
            tableService.changeEmpty(1L, new OrderTableChangeStatusRequest(true));

            // then
            assertThat(orderTable.isEmpty()).isTrue();
        }
    }

    @Nested
    class changeNumberOfGuests는 {

        @Test
        void 손님의_수가_0보다_작으면_예외를_반환한다() {
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(1L, new OrderTableChangeNumberOfGuestsRequest(-1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void ID에_맞는_order_table이_없을_경우_예외를_반환한다() {
            // given
            Long notExistOrderTableId = 1234L;
            when(orderTableRepository.findById(notExistOrderTableId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistOrderTableId,
                    new OrderTableChangeNumberOfGuestsRequest(3)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void order_table이_비어있으면_예외를_반환한다() {
            // given
            Long orderTableId = 1L;
            OrderTable orderTable = new OrderTable(orderTableId, 11L, 5, true);
            when(orderTableRepository.findById(orderTableId)).thenReturn(Optional.of(orderTable));

            // when & then
            assertThatThrownBy(
                    () -> tableService.changeNumberOfGuests(orderTableId, new OrderTableChangeNumberOfGuestsRequest(3)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 손님의_수를_변경할_수_있다() {
            // given
            Long orderTableId = 1L;
            OrderTable orderTable = new OrderTable(orderTableId, 11L, 5, false);
            when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
            when(orderTableRepository.save(any(OrderTable.class))).thenReturn(orderTable);
            // when
            tableService.changeNumberOfGuests(orderTableId, new OrderTableChangeNumberOfGuestsRequest(3));

            // then
            assertThat(orderTable.getNumberOfGuests()).isEqualTo(3);
        }
    }
}
