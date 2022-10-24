package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest{

    @Autowired
    private TableService tableService;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
        orderTable.setId(1L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(orderTable)).willReturn(orderTable);
    }

    @Test
    @DisplayName("테이블의 비운상태을 수정할 수 있다.")
    void changeEmpty() {
        // given
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);

        // when
        final OrderTable newStatusOrderTable = new OrderTable();
        newStatusOrderTable.setEmpty(false);
        final OrderTable updateOrderTable = tableService.changeEmpty(orderTable.getId(), newStatusOrderTable);

        // then
        assertThat(updateOrderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블 그룹이 존재할 경우 테이블의 비운상태를 수정하면 예외가 발생한다.")
    void changeEmptyWithTableGroup() {
        // given
        orderTable.setTableGroupId(1L);

        // when
        final OrderTable newStatusOrderTable = new OrderTable();
        newStatusOrderTable.setEmpty(false);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), newStatusOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문상태가 COOKING 이거나 MEAL일 경우 테이블의 비운상태를 수정하면 예외가 발생한다.")
    void changeEmptyWithCooking() {
        // given
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        // when
        final OrderTable newStatusOrderTable = new OrderTable();
        newStatusOrderTable.setEmpty(false);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, newStatusOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 손님 수를 바꾼다.")
    void changeNumberOfGuests() {
        // given
        final OrderTable newStatusOrderTable = new OrderTable();

        // when
        newStatusOrderTable.setNumberOfGuests(5);
        final OrderTable updateOrderTable = tableService.changeNumberOfGuests(1L, newStatusOrderTable);

        // then
        assertThat(updateOrderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("테이블의 손님 수를 0으로 바꾼다.")
    void changeNumberOfGuestsZero() {
        // given
        final OrderTable newStatusOrderTable = new OrderTable();

        // when
        newStatusOrderTable.setNumberOfGuests(0);
        final OrderTable updateOrderTable = tableService.changeNumberOfGuests(1L, newStatusOrderTable);

        // then
        assertThat(updateOrderTable.getNumberOfGuests()).isEqualTo(0);
    }

    @Test
    @DisplayName("테이블의 손님수는 0 미만으로 바꿀 수 없다.")
    void changeNumberOfGuestsWithInvalid() {
        // given
        final OrderTable newStatusOrderTable = new OrderTable();

        // when
        newStatusOrderTable.setNumberOfGuests(-1);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, newStatusOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
