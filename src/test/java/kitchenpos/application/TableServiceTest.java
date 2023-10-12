package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @MockBean
    private OrderDao orderDao;
    @MockBean
    private OrderTableDao orderTableDao;

    @Test
    void 주문_테이블을_생성한다() {
        OrderTable orderTable = makeOrderTableById(1L);
        Mockito.when(orderTableDao.save(any()))
                .thenReturn(orderTable);

        OrderTable saved = tableService.create(makeEmptyOrderTable());
        assertThat(saved.getId()).isEqualTo(orderTable.getId());
    }

    @Test
    void 주문_테이블_목록을_조회한다() {
        Mockito.when(orderTableDao.findAll())
                .thenReturn(List.of(makeOrderTableById(1L), makeOrderTableById(2L)));

        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables.size()).isEqualTo(2);
    }

    @Test
    void 주문_테이블에서_주문을_받을_수_있는지_여부를_변경한다() {
        OrderTable orderTable = makeEmptyOrderTable();
        OrderTable savedOrderTable = makeEmptyTableGroupOrderTableById(1L);
        Mockito.when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.ofNullable(savedOrderTable));
        Mockito.when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .thenReturn(false);

        savedOrderTable.setEmpty(true);
        Mockito.when(orderTableDao.save(orderTable))
                .thenReturn(savedOrderTable);

        assertThat(savedOrderTable.isEmpty()).isTrue();
    }

    @Test
    void 주문_참여_인원을_변경한다() {
        OrderTable savedOrderTable = makeEmptyTableGroupOrderTableById(1L);
        OrderTable orderTable = makeOrderTableById(1L);
        orderTable.setNumberOfGuests(3);

        Mockito.when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.ofNullable(savedOrderTable));

        savedOrderTable.setNumberOfGuests(3);
        Mockito.when(orderTableDao.save(any(OrderTable.class)))
                .thenReturn(savedOrderTable);

        OrderTable expected = tableService.changeEmpty(1L, orderTable);
        assertThat(expected.getNumberOfGuests()).isEqualTo(3);
    }

    private OrderTable makeOrderTableById(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(1L);
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(1);
        return orderTable;
    }

    private OrderTable makeEmptyTableGroupOrderTableById(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(1);
        return orderTable;
    }

    private OrderTable makeEmptyOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(1);
        return orderTable;
    }
}
