package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    @Test
    void testCreateSuccess() {
        //given
        final OrderTable orderTable = new OrderTable(1L, 1L, 1, false);
        when(orderTableDao.save(orderTable))
                .thenReturn(orderTable);

        //when
        final OrderTable result = tableService.create(orderTable);

        //then
        assertThat(result).isEqualTo(orderTable);
    }

    @Test
    void testListSuccess() {
        //given
        final OrderTable orderTable1 = new OrderTable(1L, 1L, 1, false);
        final OrderTable orderTable2 = new OrderTable(2L, 2L, 1, false);
        final OrderTable orderTable3 = new OrderTable(3L, 3L, 1, false);
        when(tableService.list())
                .thenReturn(List.of(orderTable1, orderTable2, orderTable3));

        //when
        final List<OrderTable> result = tableService.list();

        //then
        assertThat(result).isEqualTo(List.of(orderTable1, orderTable2, orderTable3));
    }

    @Test
    void testChangeEmptySuccess() {
        // given
        Long orderTableId = 1L;
        OrderTable inputOrderTable = new OrderTable(1L, 1, true);

        OrderTable savedOrderTable = new OrderTable(1L, null, 1, false);

        when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(Long.class), anyList()))
                .thenReturn(false);
        when(orderTableDao.save(savedOrderTable))
                .thenReturn(savedOrderTable);

        // when
        OrderTable resultOrderTable = tableService.changeEmpty(orderTableId, inputOrderTable);

        // then
        assertThat(resultOrderTable).isEqualTo(savedOrderTable);
    }

    @Test
    void testChangeEmptyWhenTableGroupIdNotNull() {
        // given
        Long orderTableId = 1L;
        OrderTable inputOrderTable = new OrderTable(1L, 1, true);

        OrderTable savedOrderTable = new OrderTable(1L, 1L, 1, false);

        when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, inputOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testChangeEmptyWhenOrderAlreadyCookOrMeal() {
        // given
        Long orderTableId = 1L;
        OrderTable inputOrderTable = new OrderTable(1L, 1, true);

        OrderTable savedOrderTable = new OrderTable(1L, null, 1, false);

        when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(savedOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(Long.class), anyList()))
                .thenReturn(true);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, inputOrderTable))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    void testChangeNumberOfGuestsSuccess() {
        // given
        Long orderTableId = 1L;
        int numberOfGuests = 5;

        OrderTable orderTable = new OrderTable(1L, numberOfGuests, false);
        OrderTable savedOrderTable = new OrderTable(1L, numberOfGuests, false);

        when(orderTableDao.findById(orderTableId))
                .thenReturn(Optional.of(savedOrderTable));
        when(orderTableDao.save(savedOrderTable))
                .thenReturn(savedOrderTable);

        // when
        OrderTable resultOrderTable = tableService.changeNumberOfGuests(orderTableId, orderTable);

        // then
        assertThat(resultOrderTable).isNotNull();
        assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    void testChangeNumberOfGuestsWhenNumberOfGuestsLowerThanZeroFailure() {
        // given
        Long orderTableId = 1L;
        int numberOfGuests = -5;

        OrderTable orderTable = new OrderTable(1L, numberOfGuests, false);

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testChangeNumberOfGuestsWhenOrderTableIsEmptyFailure() {
        // given
        Long orderTableId = 1L;
        int numberOfGuests = 5;

        OrderTable orderTable = new OrderTable(1L, numberOfGuests, true);
        OrderTable savedOrderTable = new OrderTable(1L, numberOfGuests, true);

        when(orderTableDao.findById(orderTableId))
                .thenReturn(Optional.of(savedOrderTable));

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testChangeNumberOfGuestsWhenOrderTableNotFoundFailure() {
        // given
        Long orderTableId = 1L;
        int numberOfGuests = 5;

        OrderTable orderTable = new OrderTable(1L, numberOfGuests, false);

        when(orderTableDao.findById(orderTableId))
                .thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
