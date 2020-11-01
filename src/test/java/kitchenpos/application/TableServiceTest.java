package kitchenpos.application;

import static kitchenpos.OrderTableFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;
    private TableService tableService;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderRepository, orderTableRepository);
    }

    @DisplayName("id가 없는 테이블로 id가 있는 테이블을 정상적으로 생성한다.")
    @Test
    void createTest() {
        final OrderTable expectedTable = createOrderTableWithId(1L);
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(expectedTable);

        assertThat(tableService.create(createOrderTableWithoutId())).isEqualToComparingFieldByField(expectedTable);
    }

    @DisplayName("테이블들을 정상적으로 조회한다.")
    @Test
    void listTest() {
        final List<OrderTable> expectedOrderTables = createOrderTables();
        given(orderTableRepository.findAll()).willReturn(expectedOrderTables);

        assertThat(tableService.list()).usingRecursiveComparison()
            .isEqualTo(expectedOrderTables);
    }

    @DisplayName("테이블을 빈 테이블로 수정한다.")
    @Test
    void changeEmptyTest() {
        final OrderTable expectedOrderTable = createOrderTableWithId(1L);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(expectedOrderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        expectedOrderTable.setEmpty(true);
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(expectedOrderTable);

        assertThat(tableService.changeEmpty(1L, expectedOrderTable)).isEqualToComparingFieldByField(expectedOrderTable);
    }

    @DisplayName("없는 테이블을 빈 테이블로 수정 시도하면 예외를 반환한다.")
    @Test
    void changeEmptyTest2() {
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(1L, createOrderTableWithId(1L)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 테이블을 빈 테이블로 수정 시도하면 예외를 반환한다.")
    @Test
    void changeEmptyTest3() {
        final OrderTable groupOrderTable = createOrderTableWithId(1L);
        groupOrderTable.setTableGroupId(1L);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(groupOrderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, groupOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("계산 완료되지 않은 주문이 있는 테이블을 빈 테이블로 수정 시도하면 예외를 반환한다.")
    @Test
    void changeEmptyTest4() {
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(createOrderTableWithId(1L)));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, createOrderTableWithId(1L)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 정상적으로 수정한다.")
    @Test
    void changeNumberOfGuestsTest() {
        final OrderTable expectedOrderTable = createOrderTableWithId(1L);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(expectedOrderTable));
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(expectedOrderTable);

        assertThat(tableService.changeNumberOfGuests(1L, createOrderTableWithId(1L)))
            .isEqualToComparingFieldByField(expectedOrderTable);
    }

    @DisplayName("테이블의 손님 수를 0이하로 수정하려고 하면 예외를 반환한다.")
    @Test
    void changeNumberOfGuestsTest2() {
        final OrderTable requestOrderTable = createOrderTableWithId(1L);
        requestOrderTable.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, requestOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("없는 테이블의 손님 수를 수정하려고 하면 예외를 반환한다.")
    @Test
    void changeNumberOfGuestsTest3() {
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, createOrderTableWithoutId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 손님 수를 수정하려고 하면 예외를 반환한다.")
    @Test
    void changeNumberOfGuestsTest4() {
        final OrderTable savedOrderTable = createOrderTableWithId(1L);
        savedOrderTable.setEmpty(true);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(savedOrderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, createOrderTableWithoutId()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
