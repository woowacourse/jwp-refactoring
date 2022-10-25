package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableServiceTest extends ServiceTest {

    @DisplayName("주문 테이블을 추가한다.")
    @Test
    void create() {
        // given
        OrderTable orderTable = getOrderTable(1L, null, 3, true);

        given(orderTableDao.save(any()))
                .willReturn(orderTable);

        // when
        OrderTable actual = tableService.create(orderTable);

        // then
        assertThat(actual).isNotNull();
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        // given
        OrderTable orderTable1 = getOrderTable(1L, null, 3, true);
        OrderTable orderTable2 = getOrderTable(2L, null, 3, true);
        OrderTable orderTable3 = getOrderTable(3L, null, 3, true);
        List<OrderTable> orderTables = List.of(orderTable1, orderTable2, orderTable3);

        given(orderTableDao.findAll())
                .willReturn(orderTables);

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).hasSize(orderTables.size());
    }

    @DisplayName("주문 테이블의 사용 여부를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        Long id = 1L;
        OrderTable savedOrderTable = getOrderTable(id, null, 3, true);
        OrderTable updatedOrderTable = getOrderTable(id, null, 3, false);

        given(orderTableDao.findById(id))
                .willReturn(Optional.ofNullable(savedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(id,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);
        given(orderTableDao.save(any()))
                .willReturn(updatedOrderTable);

        // when
        OrderTable actual = tableService.changeEmpty(id, updatedOrderTable);

        // then
        assertThat(actual.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블의 방문 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        Long id = 1L;
        OrderTable savedOrderTable = getOrderTable(id, null, 3, false);
        OrderTable updatedOrderTable = getOrderTable(id, null, 5, false);

        given(orderTableDao.findById(id))
                .willReturn(Optional.ofNullable(savedOrderTable));
        given(orderTableDao.save(any()))
                .willReturn(updatedOrderTable);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(id, updatedOrderTable);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(5);
    }
}
