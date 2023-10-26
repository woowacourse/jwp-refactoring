package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kitchenpos.order.application.TableService;
import kitchenpos.order.application.dto.OrderTableEmptyRequest;
import kitchenpos.order.application.dto.OrderTableGuestRequest;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.exception.OrderTableException.EmptyTableException;
import kitchenpos.order.domain.exception.OrderTableException.ExistsNotCompletionOrderException;
import kitchenpos.order.domain.exception.OrderTableException.ExistsTableGroupException;
import kitchenpos.order.domain.exception.OrderTableException.InvalidNumberOfGuestsException;
import kitchenpos.order.domain.exception.OrderTableException.NotExistsOrderTableException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    private final OrderTable orderTable = new OrderTable(0);
    @InjectMocks
    private TableService tableService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("현재 저장된 주문 테이블을 확인할 수 있다.")
    void list_success() {
        tableService.list();

        verify(orderTableRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("주문 테이블이 db에 저장되어있지 않으면 예외가 발생한다.")
    void changeEmpty_fail_no_order_table() {
        when(orderTableRepository.getById(1L)).thenThrow(new NotExistsOrderTableException()); // db에 저장 x

        OrderTableEmptyRequest orderTableEmptyRequest = new OrderTableEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableEmptyRequest))
                .isInstanceOf(NotExistsOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블이 어떤 테이블 그룹에 포함되어 있으면 예외가 발생한다.")
    void changeEmpty_fail_exists_tableGroup() {
        when(orderTableRepository.getById(1L)).thenReturn(orderTable);
        orderTable.group(new TableGroup());

        OrderTableEmptyRequest orderTableEmptyRequest = new OrderTableEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableEmptyRequest))
                .isInstanceOf(ExistsTableGroupException.class);
    }

    @Test
    @DisplayName("주문 테이블에 매핑된 주문이 COOKING 상태이거나 MEAL 상태라면 예외가 발생한다.")
    void changeEmpty_fail_not_completion() {
        when(orderTableRepository.getById(1L)).thenReturn(orderTable);
        when(orderRepository.existsByOrderTableInAndOrderStatusIn(any(), any())).thenReturn(true);
        orderTable.ungroup();

        OrderTableEmptyRequest orderTableEmptyRequest = new OrderTableEmptyRequest(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableEmptyRequest))
                .isInstanceOf(ExistsNotCompletionOrderException.class);
    }

    @Test
    @DisplayName("방문한 손님 수를 바꿀 수 있다.")
    void changeNumberOfGuests_success() {
        when(orderTableRepository.getById(orderTable.getId())).thenReturn(orderTable);
        int beforeNumberOfGuests = orderTable.getNumberOfGuests();
        orderTable.changeEmpty(false);

        OrderTableGuestRequest orderTableGuestRequest = new OrderTableGuestRequest(10);

        tableService.changeNumberOfGuests(orderTable.getId(), orderTableGuestRequest);

        int afterNumberOfGuests = orderTable.getNumberOfGuests();

        assertAll(
                () -> assertThat(beforeNumberOfGuests).isZero(),
                () -> assertThat(afterNumberOfGuests).isEqualTo(10)
        );
    }

    @Test
    @DisplayName("방문한 손님 수가 0명 미만인 경우 예외가 발생한다.")
    void changeNumberOfGuests_fail_number_of_guest_less_than_zero() {
        when(orderTableRepository.getById(orderTable.getId())).thenReturn(orderTable);

        OrderTableGuestRequest orderTableGuestRequest = new OrderTableGuestRequest(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableGuestRequest))
                .isInstanceOf(InvalidNumberOfGuestsException.class);
    }

    @Test
    @DisplayName("주문 테이블이 db에 저장되어있지 않으면 예외가 발생한다")
    void changeNumberOfGuests_fail_no_order_table() {
        when(orderTableRepository.getById(orderTable.getId())).thenThrow(new NotExistsOrderTableException());

        OrderTableGuestRequest orderTableGuestRequest = new OrderTableGuestRequest(10);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableGuestRequest))
                .isInstanceOf(NotExistsOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블인 경우 예외가 발생한다.")
    void changeNumberOfGuests_fail_empty_table() {
        when(orderTableRepository.getById(orderTable.getId())).thenReturn(orderTable);
        orderTable.changeEmpty(true);

        OrderTableGuestRequest orderTableGuestRequest = new OrderTableGuestRequest(10);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableGuestRequest))
                .isInstanceOf(EmptyTableException.class);
    }
}
