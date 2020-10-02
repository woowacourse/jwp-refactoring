package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("create: 테이블 등록 생성 테스트")
    @Test
    void createTest() {
        final OrderTable table = new OrderTable();
        table.setNumberOfGuests(0);
        table.setEmpty(true);

        when(orderTableDao.save(any())).thenReturn(table);

        final OrderTable expcted = tableService.create(table);

        assertThat(expcted.getNumberOfGuests()).isEqualTo(0);
    }

    @DisplayName("list: 테이블 전체 조회 테스트")
    @Test
    void listTest() {
        final OrderTable oneTable = new OrderTable();
        oneTable.setNumberOfGuests(0);
        oneTable.setEmpty(true);

        final OrderTable twoTable = new OrderTable();
        twoTable.setNumberOfGuests(0);
        twoTable.setEmpty(true);

        when(orderTableDao.findAll()).thenReturn(Arrays.asList(oneTable, twoTable));

        final List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(2);
    }

    @DisplayName("changeEmpty: 테이블의 비어있는 상태를 변경하는 테스트")
    @Test
    void changeEmptyTest() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(false);
        when(orderTableDao.save(any())).thenReturn(orderTable);

        final OrderTable expected = new OrderTable();
        expected.setEmpty(false);
        final OrderTable actual = tableService.changeEmpty(1L, expected);

        assertThat(actual.isEmpty()).isEqualTo(false);
    }

    @DisplayName("changeNumberOfGuests")
    @Test
    void changeNumberOfGuestsTest() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(false);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
        when(orderTableDao.save(any())).thenReturn(orderTable);

        final OrderTable expected = new OrderTable();
        orderTable.setNumberOfGuests(5);
        final OrderTable actual = tableService.changeNumberOfGuests(1L, expected);

        assertThat(actual.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
    }
}
