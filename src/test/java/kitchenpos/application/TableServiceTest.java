package kitchenpos.application;

import static kitchenpos.util.ObjectUtil.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @DisplayName("id가 없는 테이블로 id가 있는 테이블을 정상적으로 생성한다.")
    @Test
    void createTest() {
        final OrderTable expectedTable = createOrderTable(1L, null, 0, false);

        given(orderTableRepository.save(any(OrderTable.class))).willReturn(expectedTable);

        assertThat(tableService.create(createOrderTable(null, null, 0, false))).isEqualToComparingFieldByField(
            expectedTable);
    }

    @DisplayName("테이블들을 정상적으로 조회한다.")
    @Test
    void listTest() {
        final OrderTable firstTable = createOrderTable(1L, 2L, 4, false);
        final OrderTable secondTable = createOrderTable(2L, 3L, 3, false);
        final List<OrderTable> expectedOrderTables = Arrays.asList(firstTable, secondTable);

        given(orderTableRepository.findAll()).willReturn(expectedOrderTables);

        assertThat(tableService.list()).usingRecursiveComparison()
            .isEqualTo(expectedOrderTables);
    }

    @DisplayName("테이블을 빈 테이블로 수정한다.")
    @Test
    void changeEmptyTest() {
        final OrderTable persistOrderTable = createOrderTable(1L, null, 0, true);
        final OrderTable orderTable = createOrderTable(1L, 2L, 3, true);

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(persistOrderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(createOrderTable(1L, null, 0, true));

        assertThat(tableService.changeEmpty(1L, orderTable)).isEqualToComparingFieldByField(persistOrderTable);
    }

    @DisplayName("없는 테이블을 빈 테이블로 수정 시도하면 예외를 반환한다.")
    @Test
    void changeEmptyTest2() {
        final OrderTable orderTable = createOrderTable(1L, 2L, 0, false);

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 테이블을 빈 테이블로 수정 시도하면 예외를 반환한다.")
    @Test
    void changeEmptyTest3() {
        final OrderTable groupOrderTable = createOrderTable(1L, 1L, 0, true);

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(groupOrderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, groupOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("계산 완료되지 않은 주문이 있는 테이블을 빈 테이블로 수정 시도하면 예외를 반환한다.")
    @Test
    void changeEmptyTest4() {
        final OrderTable orderTable = createOrderTable(1L, null, 3, false);

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 정상적으로 수정한다.")
    @Test
    void changeNumberOfGuestsTest() {
        final OrderTable persistOrderTable = createOrderTable(1L, 2L, 0, false);
        final OrderTable orderTable = createOrderTable(1L, 2L, 3, false);

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(persistOrderTable));
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(orderTable);

        assertThat(tableService.changeNumberOfGuests(1L, orderTable))
            .isEqualToComparingFieldByField(orderTable);
    }

    @DisplayName("테이블의 손님 수를 0이하로 수정하려고 하면 예외를 반환한다.")
    @Test
    void changeNumberOfGuestsTest2() {
        final OrderTable requestOrderTable = createOrderTable(1L, 2L, -2, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, requestOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("없는 테이블의 손님 수를 수정하려고 하면 예외를 반환한다.")
    @Test
    void changeNumberOfGuestsTest3() {
        final OrderTable requestOrderTable = createOrderTable(null, 2L, 2, false);

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, requestOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 손님 수를 수정하려고 하면 예외를 반환한다.")
    @Test
    void changeNumberOfGuestsTest4() {
        final OrderTable persistOrderTable = createOrderTable(1L, 2L, 0, true);
        final OrderTable orderTable = createOrderTable(1L, 2L, 3, false);

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(persistOrderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
