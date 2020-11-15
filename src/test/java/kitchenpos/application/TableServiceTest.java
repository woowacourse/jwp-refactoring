package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    private List<OrderTable> tables;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setTableGroupId(null);
        orderTable1.setNumberOfGuests(0);
        orderTable1.setEmpty(true);

        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setTableGroupId(null);
        orderTable2.setNumberOfGuests(0);
        orderTable2.setEmpty(true);

        OrderTable orderTable3 = new OrderTable();
        orderTable3.setId(3L);
        orderTable3.setTableGroupId(null);
        orderTable3.setNumberOfGuests(0);
        orderTable3.setEmpty(true);

        tables = Arrays.asList(orderTable1, orderTable2, orderTable3);
    }

    @DisplayName("테이블을 정상적으로 생성한다.")
    @Test
    void create() {
        OrderTable expected = tables.get(0);
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(expected);
        OrderTable orderTable = tableService.create(new OrderTable());

        assertThat(orderTable).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("모든 테이블을 조회한다.")
    @Test
    void list() {
        when(orderTableDao.findAll()).thenReturn(tables);
        List<OrderTable> actual = tableService.list();

        assertThat(actual).usingRecursiveComparison().isEqualTo(tables);
    }

    @DisplayName("Empty상태를 변경한다.")
    @Test
    void changeEmptyHappy() {
        OrderTable table = tables.get(0);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(table));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(false);
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(table);
        OrderTable actual = tableService.changeEmpty(table.getId(), table);

        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("OrderTable이 DB에 없는 경우 예외를 반환한다.")
    @Test
    void changeEmptyNotFound() {
        OrderTable orderTable = tables.get(0);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable이 어떤 그룹에 속해있다면 예외를 반환한다.")
    @Test
    void changeEmptyGroupIdNull() {
        OrderTable orderTable = tables.get(0);
        orderTable.setTableGroupId(1L);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("식사중이거나, 조리중인 경우에 빈 테이블로 만들 수 없다.")
    @Test
    void changeEmptyAlreadyDoingSomething() {
        OrderTable orderTable = tables.get(0);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님의 수를 수정한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable expected = tables.get(0);
        expected.setNumberOfGuests(18);
        expected.setEmpty(false);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(expected));
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(expected);
        OrderTable actual = tableService.changeNumberOfGuests(expected.getId(), expected);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("손님의 수가 음수인 경우 예외를 반환한다.")
    @Test
    void changeNumberOfGuestsNegativeGuestNumber() {
        OrderTable expected = tables.get(0);
        expected.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(expected.getId(), expected))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("해당하는 OrderTable이 없으면 예외를 반환한다.")
    @Test
    void changeNumberOfGuestsNoOrderTable() {
        OrderTable expected = tables.get(0);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(expected.getId(), expected))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 없는 테이블에, 손님의 수를 추가할 수 없다.")
    @Test
    void changeNumberOfGuestsEmptyNumber() {
        OrderTable expected = tables.get(0);
        when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(expected.getId(), expected))
            .isInstanceOf(IllegalArgumentException.class);
    }
}