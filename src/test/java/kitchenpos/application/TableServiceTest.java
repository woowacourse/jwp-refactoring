package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.generator.TableGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class TableServiceTest extends ServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블 등록")
    @Test
    void create() {
        OrderTable orderTable = TableGenerator.newInstance(0, true);

        tableService.create(orderTable);
        verify(orderTableDao, times(1)).save(orderTable);
    }

    @DisplayName("주문 테이블들 조회")
    @Test
    void list() {
        List<OrderTable> orderTables = Arrays.asList(
            TableGenerator.newInstance(1L, null, 0, true),
            TableGenerator.newInstance(2L, null, 0, true)
        );
        when(orderTableDao.findAll()).thenReturn(orderTables);

        List<OrderTable> actual = tableService.list();

        assertThat(actual).hasSameSizeAs(orderTables)
            .usingRecursiveFieldByFieldElementComparator()
            .isEqualTo(orderTables);
    }

    @DisplayName("주문 테이블 빈 상태 수정")
    @Test
    void changeEmpty() {
        OrderTable savedOrderTable = TableGenerator.newInstance(1L, null, 0, true);
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(savedOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
            savedOrderTable.getId(),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
        ).thenReturn(false);
        when(orderTableDao.save(any(OrderTable.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderTable orderTable = TableGenerator.newInstance(false);
        OrderTable changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), orderTable);

        OrderTable expected = TableGenerator.newInstance(savedOrderTable.getId(), savedOrderTable.getTableGroupId(), savedOrderTable.getNumberOfGuests(),
            orderTable.isEmpty());
        verify(orderTableDao, times(1)).save(any(OrderTable.class));
        assertThat(changedOrderTable).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @DisplayName("등록되지 않은 주문 테이블의 빈 상태를 수정할 경우 예외 처리")
    @Test
    void changeEmptyWithNotFoundOrderTable() {
        long idToChange = 1L;
        when(orderTableDao.findById(idToChange)).thenReturn(Optional.empty());

        OrderTable orderTable = TableGenerator.newInstance(true);
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체로 등록된 주문 테이블의 빈 상태를 수정할 경우 예외 처리")
    @Test
    void changeEmptyWithTableDesignatedAsGroup() {
        OrderTable savedOrderTable = TableGenerator.newInstance(1L, 1L, 0, true);
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(savedOrderTable));

        OrderTable orderTable = TableGenerator.newInstance(true);
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조리나 식사 상태인 주문 테이블의 빈 상태를 수정할 경우 예외 처리")
    @Test
    void changeEmptyWithCookingOrMealStatus() {
        OrderTable savedOrderTable = TableGenerator.newInstance(1L, null, 0, true);
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(savedOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
            savedOrderTable.getId(),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
        ).thenReturn(true);

        OrderTable orderTable = TableGenerator.newInstance(true);
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 방문한 손님 수 수정")
    @Test
    void changeNumberOfGuests() {
        OrderTable savedOrderTable = TableGenerator.newInstance(1L, null, 0, false);
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(savedOrderTable));
        when(orderTableDao.save(any(OrderTable.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderTable orderTable = TableGenerator.newInstance(4);
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(1L, orderTable);

        OrderTable expected = TableGenerator.newInstance(
            savedOrderTable.getId(),
            savedOrderTable.getTableGroupId(),
            orderTable.getNumberOfGuests(),
            savedOrderTable.isEmpty()
        );
        assertThat(changedOrderTable).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @DisplayName("방문한 손님 수가 음수일 경우 예외 처리")
    @Test
    void changeNumberOfGuestsWithNegativeNumber() {
        OrderTable orderTable = TableGenerator.newInstance(-1);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 주문 테이블의 방문한 손님 수를 수정할 경우 예외 처리")
    @Test
    void changeNumberOfGuestsWithNotFoundOrderTable() {
        when(orderTableDao.findById(1L)).thenReturn(Optional.empty());

        OrderTable orderTable = TableGenerator.newInstance(4);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있는 주문 테이블의 방문한 손님 수를 수정할 경우 예외 처리")
    @Test
    void changeNumberOfGuestsWithEmptyOrderTable() {
        OrderTable savedOrderTable = TableGenerator.newInstance(1L, null, 0, true);
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(savedOrderTable));

        OrderTable orderTable = TableGenerator.newInstance(4);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable)).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
