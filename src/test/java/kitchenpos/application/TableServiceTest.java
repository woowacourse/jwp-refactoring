package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.KitchenposException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static kitchenpos.exception.KitchenposException.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    private OrderTable orderTable;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(false);
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(3);

        orderTable2 = new OrderTable();
        orderTable2.setId(1L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);
        orderTable2.setNumberOfGuests(6);
    }

    @Test
    @DisplayName("주문 테이블을 생성하면 id와 테이블그룹이 연결되지 않은 테이블이 만들어진다.")
    void create() {
        when(orderTableDao.save(any(OrderTable.class)))
                .thenReturn(orderTable);
        OrderTable actual = tableService.create(orderTable);

        assertThat(actual.getId()).isNull();
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(orderTable);
    }

    @Test
    @DisplayName("모든 주문 테이블을 조회한다.")
    void list() {
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(null);
        orderTable2.setTableGroupId(null);
        orderTable2.setEmpty(true);

        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(orderTable);
        orderTables.add(orderTable2);

        when(orderTableDao.findAll())
                .thenReturn(orderTables);

        List<OrderTable> actual = tableService.list();
        assertThat(actual).hasSize(2);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(orderTables);
    }

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 전환한다.")
    void changeEmpty() {
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .thenReturn(false);
        when(orderTableDao.save(any(OrderTable.class)))
                .thenReturn(orderTable2);

        assertThat(orderTable.isEmpty()).isFalse();
        OrderTable actual = tableService.changeEmpty(1L, orderTable2);
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문테이블이 존재하지 않으면 예외가 발생한다.")
    void changeEmptyExceptionTable() {
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable2))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(ILLEGAL_ORDER_TABLE_ID);
    }

    @Test
    @DisplayName("주문테이블에 테이블 그룹이 존재하면 예외가 발생한다.")
    void changeEmptyExceptionTableGroup() {
        orderTable.setTableGroupId(1L);
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable2))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(IMPOSSIBLE_TABLE_GROUP_ID);
    }

    @Test
    @DisplayName("요리중이거나 식사중인 테이블은 비울 수 없다.")
    void changeEmptyExceptionStatus() {
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .thenReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable2))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(IMPOSSIBLE_TABLE_STATUS);
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(orderTable));
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(3);

        orderTable.setNumberOfGuests(orderTable2.getNumberOfGuests());
        when(orderTableDao.save(any(OrderTable.class)))
                .thenReturn(orderTable);
        OrderTable actual = tableService.changeEmpty(1L, orderTable2);
        assertThat(actual.getNumberOfGuests()).isEqualTo(6);
    }

    @Test
    @DisplayName("주문 테이블의 손님 수가 0보다 작으면 에러가 발생한다.")
    void changeNumberOfGuestsExceptionNegative() {
        orderTable.setNumberOfGuests(-1);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(IMPOSSIBLE_NUMBER_OF_GUESTS);
    }

    @Test
    @DisplayName("손님 수 변경 시 주문 테이블이 존재하지 않으면 에러가 발생한다.")
    void changeNumberOfGuestsExceptionIllegalTable() {
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable2))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(ILLEGAL_ORDER_TABLE_ID);
    }

    @Test
    @DisplayName("손님 수 변경 시 주문 테이블이 비어있으면 에러가 발생한다.")
    void changeNumberOfGuestsExceptionEmptyTable() {
        orderTable.setEmpty(true);
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(orderTable));
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable2))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(EMPTY_ORDER_TABLE);
    }
}
