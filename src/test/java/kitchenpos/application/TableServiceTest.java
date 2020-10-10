package kitchenpos.application;

import static kitchenpos.helper.EntityCreateHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @MockBean
    private OrderTableDao orderTableDao;

    @MockBean
    private OrderDao orderDao;

    @DisplayName("주문테이블 추가한다.")
    @Test
    void create() {
        OrderTable orderTable = createOrderTable(1L, true, null, 1);

        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        OrderTable actual = tableService.create(new OrderTable());

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(1L),
            () -> assertThat(actual.isEmpty()).isEqualTo(true)
        );
    }

    @DisplayName("주문테이블 리스트를 조회한다.")
    @Test
    void list() {
        OrderTable tableOne = createOrderTable(1L, true, null, 1);
        OrderTable tableTwo = createOrderTable(2L, true, null, 1);

        List<OrderTable> orderTables = Arrays.asList(tableOne, tableTwo);
        given(orderTableDao.findAll()).willReturn(orderTables);

        List<OrderTable> actual = tableService.list();

        assertAll(
            () -> assertThat(actual).hasSize(2),
            () -> assertThat(actual).containsAll(orderTables)
        );
    }

    @DisplayName("주문테이블이 없는경우에 테이블의 사람유무를 변경했을 때 예외가 발생한다.")
    @Test
    void changeEmptyWhenNoOrderTable() {
        given(orderTableDao.findById(any(Long.class)))
            .willReturn(Optional.empty());

        assertThatThrownBy(
            () -> tableService.changeEmpty(1L, new OrderTable())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체가 지정되어 있는 경우 사람유무를 변경했을 때 예외가 발생한다.")
    @Test
    void changeEmptyWhenHasTableGroupId() {
        OrderTable orderTable = createOrderTable(1L, true, 1L, 1);

        given(orderTableDao.findById(any(Long.class)))
            .willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 들어가지 않거나, 계산이 완료되지 않은 경우 사람유무를 변경했을 때 예외가 발생한다.")
    @Test
    void changeEmptyWhenOrderStatusIsNullOrCompletion() {
        OrderTable orderTable = createOrderTable(1L, true, null, 1);

        given(orderTableDao.findById(any(Long.class)))
            .willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(Long.class), anyList()))
            .willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 사람의 유무 여부를 변경한다")
    @Test
    void changeEmpty() {
        OrderTable origin = createOrderTable(1L, true, null, 1);
        OrderTable expect = createOrderTable(1L, false, null, 1);

        given(orderTableDao.findById(any(Long.class)))
            .willReturn(Optional.of(origin));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(Long.class), anyList()))
            .willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(expect);

        OrderTable orderTable = createOrderTable(1L, false, null, 1);

        OrderTable savedOrderTable = tableService.changeEmpty(1L, orderTable);

        assertAll(
            () -> assertThat(savedOrderTable.getId()).isEqualTo(1L),
            () -> assertThat(savedOrderTable.isEmpty()).isEqualTo(false)
        );
    }

    @DisplayName("변경하려는 손님의 수가 0미만일 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWhenNumberIsBelowZero() {
        OrderTable orderTable = createOrderTable(1L, true, null, -1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경할 때 주문 테이블이 없는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWhenNoOrderTable() {
        OrderTable orderTable = createOrderTable(1L, true, null, 3);

        given(orderTableDao.findById(any(Long.class)))
            .willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경할 때 주문테이블의 착석한 손님이 없는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWhenIsEmpty() {
        OrderTable expect = createOrderTable(1L, true, null, 3);

        given(orderTableDao.findById(any(Long.class)))
            .willReturn(Optional.of(expect));

        OrderTable orderTable = createOrderTable(1L, true, null, 3);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable savedOrderTable = createOrderTable(1L, false, null, 3);
        OrderTable expect = createOrderTable(1L, false, null, 4);

        given(orderTableDao.findById(any(Long.class)))
            .willReturn(Optional.of(savedOrderTable));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(expect);

        OrderTable orderTable = createOrderTable(1L, false, null, 3);
        OrderTable actual = tableService.changeNumberOfGuests(1L, orderTable);

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(1L),
            () -> assertThat(actual.getNumberOfGuests()).isEqualTo(4)
        );
    }
}
