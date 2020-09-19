package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private TableService tableService;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    private OrderTable orderTable1;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
        orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setNumberOfGuests(1);
        orderTable1.setEmpty(false);
    }

    @Test
    void create() {
        given(orderTableDao.save(any())).willReturn(orderTable1);

        OrderTable savedOrderTable = tableService.create(orderTable1);

        assertThat(savedOrderTable.getId()).isNull();
        assertThat(savedOrderTable.getTableGroupId()).isNull();
        assertThat(savedOrderTable.isEmpty()).isFalse();
    }

    @Test
    void list() {
        OrderTable orderTable2 = new OrderTable();
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        given(orderTableDao.findAll()).willReturn(orderTables);

        List<OrderTable> foundOrderTables = tableService.list();
        assertThat(foundOrderTables.size()).isEqualTo(orderTables.size());
        assertThat(foundOrderTables.get(0)).isEqualTo(orderTables.get(0));
        assertThat(foundOrderTables.get(1)).isEqualTo(orderTables.get(1));
    }

    @Test
    void changeEmpty() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        given(orderTableDao.findById(1L)).willReturn(java.util.Optional.ofNullable(orderTable1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(
            1L, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);

        assertThat(orderTable1.isEmpty()).isFalse();

        OrderTable savedOrderTable = tableService.changeEmpty(1L, orderTable);
        assertThat(savedOrderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("empty 상태를 변경하기 위해서는 테이블 그룹에 포함되어있지 않아야 한다.")
    void changeEmptyFailWhenInGroup() {
        OrderTable orderTable = new OrderTable();
        orderTable1.setTableGroupId(1L);

        given(orderTableDao.findById(1L)).willReturn(java.util.Optional.ofNullable(orderTable1));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("changeEmpty Error: 테이블 그룹에 포함되어있지 않아야 합니다.");
    }

    @Test
    @DisplayName("empty 상태를 변경하기 위해서는 주문 상태가 결제 완료여야 한다.")
    void changeEmptyFailWhenOrderStatusNotComplete() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        given(orderTableDao.findById(1L)).willReturn(java.util.Optional.ofNullable(orderTable1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(
            1L, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("changeEmpty Error: 주문 상태가 결제 완료여야 합니다.");
    }

    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);

        given(orderTableDao.findById(1L)).willReturn(java.util.Optional.ofNullable(orderTable1));
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);

        int guests = orderTable1.getNumberOfGuests();
        OrderTable savedOrderTable = tableService.changeNumberOfGuests(1L, orderTable);
        assertThat(guests).isEqualTo(1);
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(4);

    }

    @Test
    @DisplayName("테이블의 손님 수는 0명 이상이어야 한다")
    void changeNumberOfGuestFailWhenUnderZero() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("changeNumberOfGuest Error: 손님은 0명 이상이어야 합니다.");
    }

    @Test
    @DisplayName("테이블의 상태는 empty가 아니어야 한다.")
    void changeNumberOfGuestFailWhenEmpty() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);
        orderTable1.setEmpty(true);

        given(orderTableDao.findById(1L)).willReturn(java.util.Optional.ofNullable(orderTable1));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("changeNumberOfGuest Error: 테이블이 비어있습니다.");
    }
}
