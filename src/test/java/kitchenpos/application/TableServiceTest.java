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
import kitchenpos.utils.TestFixture;

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
        final OrderTable table = TestFixture.getOrderTableWithEmpty();

        when(orderTableDao.save(any())).thenReturn(table);

        final OrderTable expected = tableService.create(table);

        assertThat(expected.getNumberOfGuests()).isZero();
    }

    @DisplayName("list: 테이블 전체 조회 테스트")
    @Test
    void listTest() {
        final OrderTable oneTable = TestFixture.getOrderTableWithEmpty();
        final OrderTable twoTable = TestFixture.getOrderTableWithEmpty();

        when(orderTableDao.findAll()).thenReturn(Arrays.asList(oneTable, twoTable));

        final List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(2);
    }

    @DisplayName("changeEmpty: 테이블의 비어있는 상태를 변경하는 테스트")
    @Test
    void changeEmptyTest() {
        final OrderTable orderTable = TestFixture.getOrderTableWithEmpty();

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(false);
        when(orderTableDao.save(any())).thenReturn(orderTable);

        final OrderTable expected = TestFixture.getOrderTableWithNotEmpty();
        final OrderTable actual = tableService.changeEmpty(1L, expected);

        assertThat(actual.isEmpty()).isFalse();
    }

    @DisplayName("changeEmpty: 테이블 주문 상태가 음식을 먹고 있거나, 요리중이면 예외처리")
    @Test
    void changeEmptyTestByOrderStatusEqualsCookingAndMeal() {
        final OrderTable orderTable = TestFixture.getOrderTableWithNotEmpty();

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

        final OrderTable expected = TestFixture.getOrderTableWithEmpty();

        assertThatThrownBy(() -> tableService.changeEmpty(1L, expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeNumberOfGuests: 손님 수를 변경하는 테스트")
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

    @DisplayName("changeNumberOfGuests: 손님 수가 0보다 작으면 예외처리")
    @Test
    void changeNumberOfGuestsTestByZero() {
        final OrderTable expected = TestFixture.getOrderTableWithNotEmpty();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeNumberOfGuests: 테이블이 비어있는데 손님수를 변경하려면 예외치리")
    @Test
    void changeNumberOfGuestsTestByEmpty() {
        final OrderTable orderTable = TestFixture.getOrderTableWithEmpty();

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

        final OrderTable expected = TestFixture.getOrderTableWithNotEmpty();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, expected))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
