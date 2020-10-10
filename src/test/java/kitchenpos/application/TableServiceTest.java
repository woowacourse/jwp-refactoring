package kitchenpos.application;

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
@ExtendWith(MockitoExtension.class)
@Sql(value = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);
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
        OrderTable tableOne = new OrderTable();
        tableOne.setId(1L);
        OrderTable tableTwo = new OrderTable();
        tableTwo.setId(2L);

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
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(1L);
        orderTable.setEmpty(true);
        given(orderTableDao.findById(any(Long.class)))
            .willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 들어가지 않거나, 계산이 완료되지 않은 경우 사람유무를 변경했을 때 예외가 발생한다.")
    @Test
    void changeEmptyWhenOrderStatusIsNullOrCompletion() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setTableGroupId(null);

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
        OrderTable origin = new OrderTable();
        origin.setEmpty(true);
        origin.setTableGroupId(null);

        OrderTable expect = new OrderTable();
        expect.setId(1L);
        expect.setEmpty(false);

        given(orderTableDao.findById(any(Long.class)))
            .willReturn(Optional.of(origin));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(Long.class), anyList()))
            .willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(expect);

        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = tableService.changeEmpty(1L, orderTable);

        assertAll(
            () -> assertThat(savedOrderTable.getId()).isEqualTo(1L),
            () -> assertThat(savedOrderTable.isEmpty()).isEqualTo(false)
        );
    }

    @DisplayName("변경하려는 손님의 수가 0미만일 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWhenNumberIsBelowZero() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경할 때 주문 테이블이 없는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWhenNoOrderTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(3);

        given(orderTableDao.findById(any(Long.class)))
            .willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경할 때 주문테이블의 착석한 손님이 없는 경우 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsWhenIsEmpty() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(3);

        given(orderTableDao.findById(any(Long.class)))
            .willReturn(Optional.of(orderTable));

        OrderTable orderTable2 = new OrderTable();
        orderTable.setNumberOfGuests(3);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable2))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setEmpty(false);
        savedOrderTable.setNumberOfGuests(3);

        OrderTable expect = new OrderTable();
        expect.setId(1L);
        expect.setNumberOfGuests(4);

        given(orderTableDao.findById(any(Long.class)))
            .willReturn(Optional.of(savedOrderTable));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(expect);

        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(3);
        OrderTable actual = tableService.changeNumberOfGuests(1L, orderTable);

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(1L),
            () -> assertThat(actual.getNumberOfGuests()).isEqualTo(4)
        );
    }
}