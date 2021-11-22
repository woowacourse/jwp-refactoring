package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.TableFixture;
import kitchenpos.table.application.dto.GuestNumberRequest;
import kitchenpos.table.application.dto.OrderTableRequest;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.application.dto.OrderTableResponses;
import kitchenpos.table.application.dto.TableEmptyRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderValidator;
import kitchenpos.table.domain.repository.OrderTableRepository;
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
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private List<OrderTable> orderTables;

    @BeforeEach
    void setUp() {
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
        final OrderTableRequest request = new OrderTableRequest(orderTable1.getId());
        when(orderTableRepository.save(any())).thenReturn(orderTable1);

        final OrderTableResponse actual = tableService.create(request);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(new OrderTableResponse(orderTable1));
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다")
    @Test
    void list() {
        when(orderTableRepository.findAll()).thenReturn(orderTables);

        assertThat(tableService.list()).usingRecursiveComparison()
                .isEqualTo(new OrderTableResponses(orderTables));
    }

    @DisplayName("주문 테이블을 비울 수 있다")
    @Test
    void changeEmpty() {
        final TableEmptyRequest request = new TableEmptyRequest(true);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable1));
        doNothing().when(orderValidator).validate(any());
        when(orderTableRepository.save(any())).thenReturn(orderTable1);

        assertThatCode(() -> tableService.changeEmpty(any(), request))
                .doesNotThrowAnyException();
    }

    @DisplayName("등록되어 있는 주문 테이블이 존재하지 않으면 예외가 발생한다")
    @Test
    void clearExceptionExists() {
        final TableEmptyRequest request = new TableEmptyRequest(true);
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(any(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 단체 지정이 되어 있지 않으면 예외가 발생한다.")
    @Test
    void clearExceptionNonGroup() {
        final TableEmptyRequest request = new TableEmptyRequest(true);
        final Long orderTableId = 1L;
        final OrderTable savedOrderTable = OrderTable.builder()
                .tableGroupId(1L)
                .build();

        when(orderTableRepository.findById(any()))
                .thenReturn(Optional.of(savedOrderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("orderValidator 에서 예외가 발생하면 예외가 발생한다")
    @Test
    void clearExceptionExistsAndStatus() {
        final TableEmptyRequest request = new TableEmptyRequest(true);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable1));
        doThrow(IllegalArgumentException.class).when(orderValidator).validate(any());

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 수정할 수 있다")
    @Test
    void changeNumberOfGuests() {
        final GuestNumberRequest request = new GuestNumberRequest(0);

        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable1));
        when(orderTableRepository.save(any())).thenReturn(
                TableFixture.updateOrderTableGuestNumber(orderTable1, request));

        assertThatCode(() -> tableService.changeNumberOfGuests(any(), request))
                .doesNotThrowAnyException();
    }

    @DisplayName("방문한 손님 수는 0 명 이상이어야 한다")
    @Test
    void changeNumberOfGuestsExceptionUnderZero() {
        final GuestNumberRequest request = new GuestNumberRequest(-1);
        final OrderTable savedOrderTable = OrderTable.builder()
                .id(1L)
                .numberOfGuests(10)
                .empty(false)
                .build();
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(savedOrderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되어 있는 주문 테이블이 존재해야 한다")
    @Test
    void changeNumberOfGuestsExceptionExists() {
        final GuestNumberRequest request = new GuestNumberRequest(2);
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(any(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되어 있는 주문 테이블이 비어있으면 안 된다")
    @Test
    void changeNumberOfGuestsExceptionEmpty() {
        final GuestNumberRequest request = new GuestNumberRequest(2);
        final OrderTable savedOrderTable = OrderTable.builder()
                .of(orderTable1)
                .empty(true)
                .build();

        when(orderTableRepository.findById(any()))
                .thenReturn(Optional.of(savedOrderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(any(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
