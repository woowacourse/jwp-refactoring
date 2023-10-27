package kitchenpos.application;

import kitchenpos.application.dto.request.CreateOrderTableRequest;
import kitchenpos.application.dto.request.UpdateOrderTableEmptyRequest;
import kitchenpos.application.dto.request.UpdateOrderTableGuests;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private TableOrderStatusValidator tableOrderStatusValidator;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @Test
    void 테이블을_생성한다() {
        // given
        CreateOrderTableRequest createOrderTableRequest = new CreateOrderTableRequest(0, true);

        OrderTable savedOrderTable = new OrderTable(1L, 0, true);
        given(orderTableRepository.save(any(OrderTable.class)))
                .willReturn(savedOrderTable);

        // when, then
        tableService.create(createOrderTableRequest);
        then(orderTableRepository).should(times(1)).save(any());
    }

    @Test
    void 테이블의_상태를_변경한다() {
        // given
        OrderTable orderTable = new OrderTable(1, false);
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

        willDoNothing()
                .given(tableOrderStatusValidator).validateIsTableCompleteMeal(anyLong());
        tableService.changeEmpty(1L, new UpdateOrderTableEmptyRequest(false));
    }

    @Test
    void 상태를_바꾸려는_테이블은_반드시_존재해야_한다() {
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new UpdateOrderTableEmptyRequest(false)))
                .isInstanceOf(IllegalArgumentException.class);
        then(tableOrderStatusValidator).should(never()).validateIsTableCompleteMeal(anyLong());
    }

    @Test
    void 상태를_바꾸려는_테이블의_테이블_그룹이_존재하면_예외발생() {
        // given
        OrderTable orderTable = new OrderTable(3, false);
        orderTable.changeTableGroup(new TableGroup());
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new UpdateOrderTableEmptyRequest(false)))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(never()).save(any());
    }

    @Test
    void 상태를_바꾸려는_테이블의_주문_상태가_식사중이면_예외발생() {
        // given
        OrderTable orderTable = new OrderTable(3, false);
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));
        willThrow(IllegalArgumentException.class)
                .given(tableOrderStatusValidator).validateIsTableCompleteMeal(anyLong());

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new UpdateOrderTableEmptyRequest(false)))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(never()).save(any());
    }

    @Test
    void 테이블에_방문한_손님_수가_음수면_예외발생() {
        // given
        UpdateOrderTableGuests updateOrderTableGuests = new UpdateOrderTableGuests(-1);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, updateOrderTableGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블에_방문한_손님_조절_시_테이블이_존재하지_않으면_예외발생() {
        // given
        UpdateOrderTableGuests updateOrderTableGuests = new UpdateOrderTableGuests(1);

        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, updateOrderTableGuests))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(never()).save(any());
    }

    @Test
    void 방문하려는_테이블의_상태가_빈_테이블이면_예외발생() {
        // given
        UpdateOrderTableGuests updateOrderTableGuests = new UpdateOrderTableGuests(1);

        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, updateOrderTableGuests))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableRepository).should(never()).save(any());
    }

    @Test
    void 테이블에_방문한_손님_수를_설정한다() {
        // given
        UpdateOrderTableGuests updateOrderTableGuests = new UpdateOrderTableGuests(1);

        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(new OrderTable(1L, 3, false)));

        // when, then
        assertDoesNotThrow(() -> tableService.changeNumberOfGuests(1L, updateOrderTableGuests));
    }
}
