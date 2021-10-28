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
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable;
    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private List<OrderTable> orderTables;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
        orderTable1 = OrderTable.builder()
                .id(1L)
                .build();
        orderTable2 = OrderTable.builder()
                .id(2L)
                .build();
        orderTables = Arrays.asList(orderTable1, orderTable2);
    }

    @DisplayName("주문 테이블을 등록할 수 있다")
    @Test
    void create() {
        when(orderTableRepository.save(orderTable)).thenReturn(orderTable1);

        final OrderTable actual = tableService.create(orderTable);
        assertThat(actual).isEqualTo(orderTable1);
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다")
    @Test
    void list() {
        when(orderTableRepository.findAll()).thenReturn(orderTables);

        assertThat(tableService.list()).isEqualTo(orderTables);
    }

    @DisplayName("주문 테이블을 비울 수 있다")
    @Test
    void changeEmpty() {
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(orderTable1));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).thenReturn(false);
        when(orderTableRepository.save(orderTable1)).thenReturn(orderTable1);

        assertThatCode(() -> tableService.changeEmpty(anyLong(), orderTable1)).doesNotThrowAnyException();
    }

    @DisplayName("등록되어 있는 주문 테이블이 존재하지 않으면 예외가 발생한다")
    @Test
    void clearExceptionExists() {
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(anyLong(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("주문 테이블의 단체 지정이 되어 있지 않으면 예외가 발생한다.")
    @Test
    void clearExceptionNonGroup() {
        final Long orderTableId = 1L;
        final OrderTable savedOrderTable = OrderTable.builder()
                .tableGroupId(1L)
                .build();

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(savedOrderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, savedOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 주문이 있고, 주문 상태가 `COOKING`, `MEAL`인 것이 있다면 예외가 발생한다")
    @Test
    void clearExceptionExistsAndStatus() {
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(orderTable1));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), any())).thenReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 수정할 수 있다")
    @Test
    void changeNumberOfGuests() {
        final OrderTable newOrderTable = OrderTable.builder()
                .of(orderTable1)
                .numberOfGuests(0)
                .build();

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(orderTable1));

        assertThatCode(() -> tableService.changeNumberOfGuests(anyLong(), newOrderTable)).doesNotThrowAnyException();
    }

    @DisplayName("방문한 손님 수는 0 명 이상이어야 한다")
    @Test
    void changeNumberOfGuestsExceptionUnderZero() {
        final OrderTable newOrderTable = OrderTable.builder()
                .of(orderTable1)
                .numberOfGuests(-1)
                .build();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(newOrderTable.getId(), newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되어 있는 주문 테이블이 존재해야 한다")
    @Test
    void changeNumberOfGuestsExceptionExists() {
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(anyLong(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되어 있는 주문 테이블이 비어있으면 안 된다")
    @Test
    void changeNumberOfGuestsExceptionEmpty() {
        final OrderTable savedOrderTable = OrderTable.builder()
                .of(orderTable1)
                .empty(true)
                .build();

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(savedOrderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(anyLong(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
