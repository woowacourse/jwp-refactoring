package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
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
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("테이블을 추가한다.")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(true);

        OrderTable savedTable = new OrderTable();
        savedTable.setId(1L);
        savedTable.setNumberOfGuests(orderTable.getNumberOfGuests());
        savedTable.setEmpty(orderTable.isEmpty());

        given(tableService.create(orderTable)).willReturn(savedTable);

        OrderTable expected = tableService.create(orderTable);

        assertAll(
            () -> assertThat(expected).extracting(OrderTable::getId).isEqualTo(savedTable.getId()),
            () -> assertThat(expected).extracting(OrderTable::getNumberOfGuests).isEqualTo(
                savedTable.getNumberOfGuests()),
            () -> assertThat(expected).extracting(OrderTable::isEmpty).isEqualTo(true)
        );
    }

    @DisplayName("전체 테이블 목록을 조회한다.")
    @Test
    void list() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);
        orderTable.setEmpty(true);

        given(tableService.list()).willReturn(Collections.singletonList(orderTable));
        List<OrderTable> expected = tableService.list();

        assertThat(expected).hasSize(1);
    }

    @DisplayName("테이블을 비우거나 채울 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable savedTable = new OrderTable();
        savedTable.setId(1L);
        savedTable.setEmpty(false);
        savedTable.setNumberOfGuests(1);

        OrderTable updateInfo = new OrderTable();
        updateInfo.setEmpty(true);

        given(orderTableDao.findById(savedTable.getId())).willReturn(Optional.of(savedTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(savedTable.getId(),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        tableService.changeEmpty(savedTable.getId(), updateInfo);

        assertThat(savedTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블을 비우거나 채울 때 해당되는 테이블 번호가 없다면 예외 처리한다.")
    @Test
    void changeEmptyWithNotExistingTableId() {
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 비우거나 채울 때 테이블 그룹이 지정된 테이블이면 예외 처리한다.")
    @Test
    void changeEmptyWithTableGroup() {
        OrderTable savedTable = new OrderTable();
        savedTable.setId(1L);
        savedTable.setTableGroupId(1L);

        given(orderTableDao.findById(savedTable.getId())).willReturn(Optional.of(savedTable));

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 비우거나 채울 때 테이블에 완료되지 않은 주문이 있는 경우 예외 처리한다.")
    @Test
    void changeEmptyWithNotComplementedOrder() {
        OrderTable savedTable = new OrderTable();
        savedTable.setId(1L);

        given(orderTableDao.findById(savedTable.getId())).willReturn(Optional.of(savedTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(savedTable.getId(),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 현재 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable savedTable = new OrderTable();
        savedTable.setId(1L);
        savedTable.setEmpty(false);
        savedTable.setNumberOfGuests(5);

        OrderTable updateInfo = new OrderTable();
        updateInfo.setNumberOfGuests(6);

        given(orderTableDao.findById(savedTable.getId())).willReturn(Optional.of(savedTable));
        given(orderTableDao.save(savedTable)).willReturn(savedTable);

        OrderTable expected = tableService.changeNumberOfGuests(savedTable.getId(), updateInfo);

        assertThat(expected).extracting(OrderTable::getNumberOfGuests).isEqualTo(updateInfo.getNumberOfGuests());
    }

    @DisplayName("테이블의 손님 수를 변경할 시 수정될 값이 음수일 경우 예외 처리한다.")
    @Test
    void changeNumberOfGuestsWithNegativeNumberOfGuests() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경할 시 존재하지 않는 테이블일 경우 예외 처리한다.")
    @Test
    void changeNumberOfGuestsWithNotExistingTableId() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);

        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경할 시 비어있는 테이블일 경우 예외 처리한다.")
    @Test
    void changeNumberOfGuestsWith() {
        OrderTable savedTable = new OrderTable();
        savedTable.setId(1L);
        savedTable.setNumberOfGuests(4);
        savedTable.setEmpty(true);

        OrderTable updateInfo = new OrderTable();
        updateInfo.setNumberOfGuests(1);

        given(orderTableDao.findById(savedTable.getId())).willReturn(Optional.of(savedTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), updateInfo))
            .isInstanceOf(IllegalArgumentException.class);
    }
}