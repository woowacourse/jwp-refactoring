package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
class TableServiceTest {

    @SpyBean
    private OrderDao orderDao;

    @SpyBean
    private OrderTableDao orderTableDao;

    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("테이블을 생성한다.")
    void create() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        // when
        final OrderTable savedTable = tableService.create(orderTable);

        // then
        assertAll(
                () -> assertThat(savedTable.getId()).isNotNull(),
                () -> assertThat(savedTable.getTableGroupId()).isNull()
        );
    }

    @Test
    @DisplayName("테이블의 비운상태을 수정할 수 있다.")
    void changeEmpty() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
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
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setTableGroupId(1L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

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
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

        // when
        final OrderTable newStatusOrderTable = new OrderTable();
        newStatusOrderTable.setNumberOfGuests(5);
        final OrderTable updateOrderTable = tableService.changeNumberOfGuests(1L, newStatusOrderTable);

        // then
        assertThat(updateOrderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    @DisplayName("테이블의 손님 수를 0으로 바꾼다.")
    void changeNumberOfGuestsZero() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

        // when
        final OrderTable newStatusOrderTable = new OrderTable();
        newStatusOrderTable.setNumberOfGuests(0);
        final OrderTable updateOrderTable = tableService.changeNumberOfGuests(1L, newStatusOrderTable);

        // then
        assertThat(updateOrderTable.getNumberOfGuests()).isEqualTo(0);
    }

    @Test
    @DisplayName("테이블의 손님수는 0 미만으로 바꿀 수 없다.")
    void changeNumberOfGuestsWithInvalid() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

        // when
        final OrderTable newStatusOrderTable = new OrderTable();
        newStatusOrderTable.setNumberOfGuests(-1);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, newStatusOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
