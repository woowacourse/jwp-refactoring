package kitchenpos.application;

import static kitchenpos.domain.DomainCreator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Autowired
    private TableService tableService;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @Test
    void create() {
        OrderTable orderTable = createOrderTable(false);
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(1);

        given(orderTableDao.save(any())).willReturn(orderTable);

        OrderTable savedOrderTable = tableService.create(orderTable);

        assertAll(
            () -> assertThat(savedOrderTable.getId()).isEqualTo(orderTable.getId()),
            () -> assertThat(savedOrderTable.getTableGroupId()).isEqualTo(orderTable.getTableGroupId()),
            () -> assertThat(savedOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty())
        );
    }

    @Test
    @DisplayName("주문 테이블의 목록을 불러올 수 있어야 한다.")
    void list() {
        OrderTable orderTable1 = createOrderTable(false);
        OrderTable orderTable2 = createOrderTable(false);
        orderTable1.setId(1L);
        orderTable2.setId(1L);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);

        given(orderTableDao.findAll()).willReturn(orderTables);

        List<OrderTable> foundOrderTables = tableService.list();

        assertThat(foundOrderTables.size()).isEqualTo(orderTables.size());
    }

    @Test
    void changeEmpty() {
        OrderTable notEmptyOrderTable = createOrderTable(false);
        OrderTable emptyOrderTable = createOrderTable(true);
        notEmptyOrderTable.setId(1L);

        given(orderTableDao.findById(notEmptyOrderTable.getId())).willReturn(java.util.Optional.of(notEmptyOrderTable));
        //결제 완료
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(
            notEmptyOrderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(notEmptyOrderTable)).willReturn(notEmptyOrderTable);

        OrderTable savedOrderTable = tableService.changeEmpty(notEmptyOrderTable.getId(), emptyOrderTable);

        assertThat(savedOrderTable.isEmpty()).isEqualTo(emptyOrderTable.isEmpty());
    }

    @Test
    @DisplayName("empty 상태를 변경하기 위해서는 테이블 그룹에 포함되어있지 않아야 한다.")
    void changeEmptyFailWhenInGroup() {
        OrderTable orderTable = createOrderTable(true);
        orderTable.setId(1L);
        orderTable.setTableGroupId(1L);

        given(orderTableDao.findById(orderTable.getId())).willReturn(java.util.Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), any()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("changeEmpty Error: 테이블 그룹에 포함되어있지 않아야 합니다.");
    }

    @Test
    @DisplayName("empty 상태를 변경하기 위해서는 주문 상태가 결제 완료여야 한다.")
    void changeEmptyFailWhenOrderStatusNotComplete() {
        OrderTable orderTable = createOrderTable(true);

        given(orderTableDao.findById(orderTable.getId())).willReturn(java.util.Optional.of(orderTable));
        //결제 미완료
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("changeEmpty Error: 주문 상태가 결제 완료여야 합니다.");
    }

    @Test
    @DisplayName("테이블 손님의 수를 변경할 수 있어야 한다.")
    void changeNumberOfGuests() {
        OrderTable orderTable = createOrderTable(false);
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(1);
        OrderTable orderTableToChange = createOrderTable(false);
        orderTableToChange.setNumberOfGuests(4);

        given(orderTableDao.findById(orderTable.getId())).willReturn(java.util.Optional.of(orderTable));
        given(orderTableDao.save(orderTable)).willReturn(orderTable);

        int guests = orderTable.getNumberOfGuests();
        OrderTable savedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTableToChange);

        assertThat(guests).isEqualTo(1);
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTableToChange.getNumberOfGuests());

    }

    @Test
    @DisplayName("테이블의 손님 수는 0명 이상이어야 한다")
    void changeNumberOfGuestFailWhenUnderZero() {
        OrderTable orderTable = createOrderTable(false);
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("changeNumberOfGuest Error: 손님은 0명 이상이어야 합니다.");
    }

    @Test
    @DisplayName("테이블의 상태는 empty가 아니어야 한다.")
    void changeNumberOfGuestFailWhenEmpty() {
        OrderTable orderTable = createOrderTable(true);
        OrderTable orderTableToChange = createOrderTable(false);
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(1);
        orderTableToChange.setNumberOfGuests(3);

        given(orderTableDao.findById(orderTable.getId())).willReturn(java.util.Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableToChange))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("changeNumberOfGuest Error: 테이블이 비어있습니다.");
    }
}
