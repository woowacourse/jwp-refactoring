package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("새 주문 테이블을 저장한다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable = new OrderTable();

        given(orderTableDao.save(any(OrderTable.class)))
            .willReturn(orderTable);

        // when
        final OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable).isEqualTo(orderTable);
    }

    @DisplayName("모든 주문 테이블을 조회한다.")
    @Test
    void list() {
        // given
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);

        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(1L);

        given(orderTableDao.findAll())
            .willReturn(List.of(orderTable1, orderTable2));

        // when
        final List<OrderTable> list = tableService.list();

        // then
        assertThat(list).usingRecursiveComparison()
            .isEqualTo(List.of(orderTable1, orderTable2));
    }

    @DisplayName("주문 테이블을 비어있도록 변경한다.")
    @Test
    void changeEmpty_success() {
        // given
        final OrderTable orderTableToChange = new OrderTable();
        orderTableToChange.setId(1L);
        orderTableToChange.setTableGroupId(null);

        final OrderTable orderTableWithChange = new OrderTable();
        orderTableWithChange.setEmpty(false);

        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(orderTableToChange));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
            .willReturn(false);

        final OrderTable expectedOrderTable = new OrderTable();
        expectedOrderTable.setId(orderTableToChange.getId());
        expectedOrderTable.setEmpty(orderTableWithChange.isEmpty());

        given(orderTableDao.save(any(OrderTable.class)))
            .willReturn(expectedOrderTable);

        // when
        final OrderTable result = tableService.changeEmpty(orderTableToChange.getId(),
            orderTableWithChange);

        // then
        assertThat(result).isEqualTo(expectedOrderTable);
    }

    @DisplayName("존재하지 않는 주문 테이블을 변경 요청하면 예외가 발생한다.")
    @Test
    void changeEmpty_notExistTableId_fail() {
        // given
        final OrderTable orderTableToChange = new OrderTable();

        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTableToChange))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 그룹에 속해있다면 예외가 발생한다.")
    @Test
    void changeEmpty_tableNotInGroup_fail() {
        // given
        final OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setId(1L);
        savedOrderTable.setTableGroupId(1L);

        final OrderTable orderTableToChange = new OrderTable();

        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(savedOrderTable));

        // when, then
        final Long tableId = savedOrderTable.getId();
        assertThatThrownBy(() -> tableService.changeEmpty(tableId, orderTableToChange))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 상태가 COMPLETION 이 아니라면 예외가 발생한다.")
    @Test
    void changeEmpty_tableNotComplete_fail() {
        // given
        final OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setId(1L);
        savedOrderTable.setTableGroupId(null);

        final OrderTable orderTableToChange = new OrderTable();

        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(savedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
            .willReturn(true);

        // when, then
        final Long tableId = savedOrderTable.getId();
        assertThatThrownBy(() -> tableService.changeEmpty(tableId, orderTableToChange))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests_success() {
        // given
        final OrderTable prevOrderTable = new OrderTable();
        prevOrderTable.setId(1L);
        prevOrderTable.setNumberOfGuests(0);
        prevOrderTable.setEmpty(false);

        final OrderTable orderTableToChange = new OrderTable();
        orderTableToChange.setNumberOfGuests(2);

        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(prevOrderTable));

        final OrderTable expected = new OrderTable();
        expected.setId(1L);
        expected.setNumberOfGuests(2);
        expected.setEmpty(false);

        given(orderTableDao.save(prevOrderTable))
            .willReturn(expected);

        // when
        final OrderTable result = tableService.changeNumberOfGuests(prevOrderTable.getId(),
            orderTableToChange);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("주문 테이블의 변경하려는 손님 수가 0보다 작으면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_wrongGuest_fail() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(-1);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("변경하려는 주문 테이블이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_notExistTable_fail() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);

        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("변경하려는 주문 테이블이 비어있다면 예외가 발생한다.")
    @Test
    void changeNumberOfGuests_empty_fail() {
        // given
        final OrderTable prevOrderTable = new OrderTable();
        prevOrderTable.setId(1L);
        prevOrderTable.setEmpty(true);

        final OrderTable orderTableToChange = new OrderTable();
        orderTableToChange.setNumberOfGuests(2);

        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(prevOrderTable));


        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTableToChange))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
