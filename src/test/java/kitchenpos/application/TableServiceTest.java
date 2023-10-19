package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.exception.OrderTableException.EmptyTableException;
import kitchenpos.domain.exception.OrderTableException.ExistsNotCompletionOrderException;
import kitchenpos.domain.exception.OrderTableException.ExistsTableGroupException;
import kitchenpos.domain.exception.OrderTableException.InvalidNumberOfGuestsException;
import kitchenpos.domain.exception.OrderTableException.NotExistsOrderTableException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    private OrderTable orderTable = new OrderTable();

    @Test
    @DisplayName("현재 저장된 주문 테이블을 확인할 수 있다.")
    void list_success() {
        tableService.list();

        verify(orderTableRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("주문 테이블이 db에 저장되어있지 않으면 예외가 발생한다.")
    void changeEmpty_fail_no_order_table() {
        OrderTable changeEmptyOrderTable = new OrderTable();

        when(orderTableRepository.getById(orderTable.getId())).thenThrow(new NotExistsOrderTableException()); // db에 저장 x

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeEmptyOrderTable))
                .isInstanceOf(NotExistsOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블이 어떤 테이블 그룹에 포함되어 있으면 예외가 발생한다.")
    void changeEmpty_fail_exists_tableGroup() {
        orderTable.setTableGroup(new TableGroup()); // 테이블 그룹에 포함
        OrderTable changeEmptyOrderTable = new OrderTable();

        when(orderTableRepository.getById(orderTable.getId())).thenReturn(orderTable);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeEmptyOrderTable))
                .isInstanceOf(ExistsTableGroupException.class);
    }

    @Test
    @DisplayName("주문 테이블에 매핑된 주문이 COOKING 상태이거나 MEAL 상태라면 예외가 발생한다.")
    void changeEmpty_fail_not_completion() {
        orderTable.setTableGroup(null);
        OrderTable changeEmptyOrderTable = new OrderTable();

        when(orderTableRepository.getById(orderTable.getId())).thenReturn(orderTable);
        when(orderRepository.existsByOrderTableAndOrderStatusIn(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), changeEmptyOrderTable))
                .isInstanceOf(ExistsNotCompletionOrderException.class);
    }

    @Test
    @DisplayName("방문한 손님 수를 바꿀 수 있다.")
    void changeNumberOfGuests_success() {
        OrderTable changeNumberOrderTable = new OrderTable();
        int changeNumberOfGuests = 10;
        changeNumberOrderTable.setNumberOfGuests(changeNumberOfGuests);
        orderTable.changeEmpty(false);

        when(orderTableRepository.getById(orderTable.getId())).thenReturn(orderTable);

        int beforeNumberOfGuests = orderTable.getNumberOfGuests();

        tableService.changeNumberOfGuests(orderTable.getId(), changeNumberOrderTable);

        int afterNumberOfGuests = orderTable.getNumberOfGuests();

        assertAll(
                () -> assertThat(beforeNumberOfGuests).isZero(),
                () -> assertThat(afterNumberOfGuests).isEqualTo(changeNumberOfGuests)
        );
    }

    @Test
    @DisplayName("방문한 손님 수가 0명 미만인 경우 예외가 발생한다.")
    void changeNumberOfGuests_fail_number_of_guest_less_than_zero() {
        orderTable.setNumberOfGuests(-1);

        when(orderTableRepository.getById(orderTable.getId())).thenReturn(orderTable);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(InvalidNumberOfGuestsException.class);
    }

    @Test
    @DisplayName("주문 테이블이 db에 저장되어있지 않으면 예외가 발생한다")
    void changeNumberOfGuests_fail_no_order_table() {
        OrderTable changeNumberOrderTable = new OrderTable();
        changeNumberOrderTable.setNumberOfGuests(10);

        when(orderTableRepository.getById(orderTable.getId())).thenThrow(new NotExistsOrderTableException());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeNumberOrderTable))
                .isInstanceOf(NotExistsOrderTableException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블인 경우 예외가 발생한다.")
    void changeNumberOfGuests_fail_empty_table() {
        orderTable.setEmpty(true);
        OrderTable changeNumberOrderTable = new OrderTable();
        changeNumberOrderTable.setNumberOfGuests(10);

        when(orderTableRepository.getById(orderTable.getId())).thenReturn(orderTable);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changeNumberOrderTable))
                .isInstanceOf(EmptyTableException.class);
    }
}
