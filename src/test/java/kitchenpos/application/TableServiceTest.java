package kitchenpos.application;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable table;

    @BeforeEach
    void setUp() {
        table = new OrderTable();
        table.setEmpty(false);
        table.setTableGroupId(null);
        table.setId(1L);
        table.setNumberOfGuests(3);
    }

    @Test
    @DisplayName("주문 테이블을 생성한다.")
    void 주문_테이블을_생성한다() {
        assertDoesNotThrow(() -> tableService.create(table));
    }

    @Test
    @DisplayName("모든 주문 테이블을 조회한다.")
    void findAllOrderTable() {
        // given
        given(orderTableDao.findAll()).willReturn(Collections.singletonList(table));

        // when, then
        List<OrderTable> orderTables = assertDoesNotThrow(() -> tableService.list());
    }

    @Test
    @DisplayName("주문 테이블의 상태를 빈 상태로 변경한다.")
    void changeStatusToEmpty() {
        // given
        given(orderTableDao.findById(any())).willReturn(Optional.of(table));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);

        // when, then
        assertDoesNotThrow(() -> tableService.changeEmpty(table.getId(), table));
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블의 상태 변경 시 예외를 발생시킨다.")
    void throwExceptionWhenTableNull() {
        // given
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(table.getId(), table));
    }

    @Test
    @DisplayName("단체 지정되어 있는 주문 테이블의 상태 변경 시 예외를 발생시킨다.")
    void throwExceptionWhenGroupTable() {
        // given
        table.setTableGroupId(1L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(table));

        // when, then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(table.getId(), table));
    }

    @Test
    @DisplayName("주문 테이블이 조리 중이거나 식사 중이라면 예외를 발생시킨다.")
    void throwExceptionWhenTableNotCompleted() {
        // given
        given(orderTableDao.findById(any())).willReturn(Optional.of(table));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(true);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(table.getId(), table));
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        given(orderTableDao.findById(any())).willReturn(Optional.of(table));

        // when, then
        assertDoesNotThrow(() -> tableService.changeNumberOfGuests(table.getId(), table));
    }

    @Test
    @DisplayName("주문 테이블의 변경할 손님 수가 음이면 예외를 발생시킨다.")
    void changeNegativeNumberOfGuests() {
        // given
        table.setNumberOfGuests(-1);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeNumberOfGuests(table.getId(), table));
    }

    @Test
    @DisplayName("손님 수를 변경할 주문 테이블이 존재하지 않으면 예외를 발생시킨다.")
    void throwExceptionWhenChangeNumberOfGuestsNullTable() {
        // given
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeNumberOfGuests(table.getId(), table));
    }

    @Test
    @DisplayName("손님 수를 변경할 주문 테이블이 비어 있으면 예외를 발생시킨다.")
    void throwExceptionWhenChangeNumberOfGuestsEmptyTable() {
        // given
        table.setEmpty(true);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeNumberOfGuests(table.getId(), table));
    }
}