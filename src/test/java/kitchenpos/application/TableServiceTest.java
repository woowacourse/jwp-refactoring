package kitchenpos.application;

import static kitchenpos.Fixture.ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderDao orderDao;

    @Test
    void create() {
        //given
        given(orderTableDao.save(any(OrderTable.class)))
                .willReturn(ORDER_TABLE);

        //when
        OrderTable orderTable = new OrderTable(0, true);
        OrderTable savedOrderTable = tableService.create(orderTable);

        //then
        assertThat(savedOrderTable).isEqualTo(ORDER_TABLE);
    }

    @Test
    void list() {
        //given
        given(orderTableDao.findAll())
                .willReturn(List.of(ORDER_TABLE));

        //when
        List<OrderTable> orderTables = tableService.list();

        //then
        assertThat(orderTables).hasSize(1);
    }

    @Test
    void changeEmpty() {
        //given
        boolean expected = false;
        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(ORDER_TABLE));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .willReturn(false);
        given(orderTableDao.save(any(OrderTable.class)))
                .willReturn(new OrderTable(ORDER_TABLE.getId(), ORDER_TABLE.getNumberOfGuests(), expected));

        //when
        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable(0, expected);
        OrderTable changedOrderTable = tableService.changeEmpty(orderTableId, orderTable);

        //then
        assertThat(changedOrderTable.isEmpty()).isEqualTo(expected);
    }

    @Test
    void changeNumberOfGuests() {
        //given
        int expected = 4;
        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(ORDER_TABLE));
        given(orderTableDao.save(any(OrderTable.class)))
                .willReturn(new OrderTable(ORDER_TABLE.getId(), expected, ORDER_TABLE.isEmpty()));

        //when
        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable(expected, false);
        OrderTable savedOrderTable = tableService.changeNumberOfGuests(orderTableId, orderTable);

        //then
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -100, -10000})
    void changeNumberOfGuests_게스트가_음수인_경우(int numberOfGuests) {
        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable(numberOfGuests, false);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
