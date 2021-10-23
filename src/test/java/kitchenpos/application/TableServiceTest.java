package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 등록할 수 있다")
    @Test
    void create() {
        final OrderTable orderTable = new OrderTable();
        final OrderTable savedOrderTable = new OrderTable();

        when(orderTableDao.save(orderTable)).thenReturn(savedOrderTable);

        final OrderTable actual = tableService.create(orderTable);
        assertThat(actual).isEqualTo(savedOrderTable);
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다")
    @Test
    void list() {
        final List<OrderTable> orderTables = Arrays.asList(new OrderTable(), new OrderTable());

        when(orderTableDao.findAll()).thenReturn(orderTables);

        assertThat(tableService.list()).isEqualTo(orderTables);
    }

    @DisplayName("주문 테이블을 비울 수 있다")
    @Test
    void changeEmpty() {
        final OrderTable savedOrderTable = new OrderTable();

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(savedOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(false);
        when(orderTableDao.save(savedOrderTable)).thenReturn(savedOrderTable);

        assertThatCode(() -> tableService.changeEmpty(anyLong(), savedOrderTable)).doesNotThrowAnyException();
    }

    @DisplayName("등록되어 있는 주문 테이블이 존재하지 않으면 예외가 발생한다")
    @Test
    void clearExceptionExists() {
        final OrderTable savedOrderTable = new OrderTable();

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(anyLong(), savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("주문 테이블의 단체 지정이 되어 있지 않으면 예외가 발생한다.")
    @Test
    void clearExceptionNonGroup() {
        final Long orderTableId = 1L;
        final OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setTableGroupId(1L);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(savedOrderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 주문이 있고, 주문 상태가 `COOKING`, `MEAL`인 것이 있다면 예외가 발생한다")
    @Test
    void clearExceptionExistsAndStatus() {
        final Long orderTableId = 1L;
        final OrderTable savedOrderTable = new OrderTable();

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(savedOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).thenReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 수정할 수 있다")
    @Test
    void changeNumberOfGuests() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

        assertThatCode(() -> tableService.changeNumberOfGuests(anyLong(), orderTable)).doesNotThrowAnyException();
    }

    @DisplayName("방문한 손님 수는 0 명 이상이어야 한다")
    @Test
    void changeNumberOfGuestsExceptionUnderZero() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되어 있는 주문 테이블이 존재해야 한다")
    @Test
    void changeNumberOfGuestsExceptionExists() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(anyLong(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되어 있는 주문 테이블이 비어있으면 안 된다")
    @Test
    void changeNumberOfGuestsExceptionEmpty() {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(anyLong(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
