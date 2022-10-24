package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

class TableServiceTest extends ServiceTest {

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

    @Test
    void changeEmpty() {
        // given
        Long id = 1L;
        OrderTable savedOrderTable = getOrderTable(id, null, 3, true);
        OrderTable updatedOrderTable = getOrderTable(id, null, 3, false);

        given(orderTableDao.findById(id))
                .willReturn(Optional.ofNullable(savedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(id, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);
        given(orderTableDao.save(any()))
                .willReturn(updatedOrderTable);

        // when
        OrderTable actual = tableService.changeEmpty(id, updatedOrderTable);

        // then
        assertThat(actual.isEmpty()).isFalse();
    }

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
