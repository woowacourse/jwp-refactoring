package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("TableService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("테이블을 생성할 수 있다. - 아무 그룹에도 속해있지 않아야한다.")
    void create() {
        // given
        OrderTable table = new OrderTable(0, true);
        OrderTable expected = new OrderTable(1L, null, 0, true);
        given(orderTableDao.save(table)).willReturn(expected);

        // when
        OrderTable actual = tableService.create(table);

        // then
        assertNull(actual.getTableGroupId());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("전체 테이블을 조회할 수 있다.")
    void list() {
        // given
        OrderTable table1 = new OrderTable(1L, 1L, 5, true);
        OrderTable table2 = new OrderTable(2L, null, 0, true);
        List<OrderTable> expected = Arrays.asList(table1, table2);
        given(orderTableDao.findAll()).willReturn(expected);

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("테이블이 비어있는지를 나타내는 상태를 수정할 수 있다.")
    void changeEmpty() {
        // given
        Long tableId = 1L;
        OrderTable changeEmptyTable = new OrderTable(null, null, 0, false);
        OrderTable table = new OrderTable(1L, null, 0, true);
        OrderTable expected = new OrderTable(1L, null, 0, false);
        given(orderTableDao.findById(tableId)).willReturn(Optional.of(table));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(tableId,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(table)).willReturn(table);

        // when
        OrderTable actual = tableService.changeEmpty(tableId, changeEmptyTable);

        // then
        assertFalse(actual.isEmpty());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("빈 상태를 수정하려면 테이블은 존재해야 한다.")
    void changeEmptyWrongNotExist() {
        // given
        Long tableId = 1L;
        OrderTable changeEmptyTable = new OrderTable(null, null, 0, false);
        given(orderTableDao.findById(tableId)).willReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeEmpty(tableId, changeEmptyTable));
        assertEquals("주문 테이블이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("빈 상태를 수정하려면 테이블은 그룹에 속해있지 않아야한다.")
    void changeEmptyWrongNotInGroup() {
        // given
        Long tableId = 1L;
        OrderTable changeEmptyTable = new OrderTable(null, null, 0, false);
        OrderTable table = new OrderTable(1L, 1L, 0, true);
        given(orderTableDao.findById(tableId)).willReturn(Optional.of(table));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeEmpty(tableId, changeEmptyTable));
        assertEquals("주문 테이블이 그룹에 속해있습니다. 그룹을 해제해주세요.", exception.getMessage());
    }

    @Test
    @DisplayName("빈 상태를 수정하려면 테이블이 조리중(COOKING)이나 식사중(MEAL)이 아니어야한다.")
    void changeEmptyWrongCookingOrMeal() {
        // given
        Long tableId = 1L;
        OrderTable changeEmptyTable = new OrderTable(null, null, 0, false);
        OrderTable table = new OrderTable(1L, null, 0, true);
        given(orderTableDao.findById(tableId)).willReturn(Optional.of(table));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(tableId,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeEmpty(tableId, changeEmptyTable));
        assertEquals("조리중이나 식사중인 경우 상태를 변경할 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주문 테이블의 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        Long tableId = 1L;
        OrderTable changeNumberTable = new OrderTable(null, null, 5, false);
        OrderTable table = new OrderTable(1L, null, 0, false);
        OrderTable expected = new OrderTable(1L, null, 5, false);
        given(orderTableDao.findById(tableId)).willReturn(Optional.of(table));
        given(orderTableDao.save(table)).willReturn(table);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(tableId, changeNumberTable);

        // then
        assertEquals(5, actual.getNumberOfGuests());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("손님의 수를 변경하려면 기존 손님의 수는 0 이상이어야 한다.")
    void changeNumberOfGuestsWrongNumber() {
        // given
        Long tableId = 1L;
        OrderTable changeNumberTable = new OrderTable(null, null, -1, false);

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(tableId, changeNumberTable));
        assertEquals("변경하려는 손님 수는 0이상이어야 합니다.", exception.getMessage());
    }

    @Test
    @DisplayName("손님의 수를 변경하려면 테이블은 존재해야 한다.")
    void changeNumberOfGuestsWrongTableNotExist() {
        // given
        Long tableId = 1L;
        OrderTable changeNumberTable = new OrderTable(null, null, 5, false);
        given(orderTableDao.findById(tableId)).willReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(tableId, changeNumberTable));
        assertEquals("주문 테이블이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("손님의 수를 변경하려면 테이블은 비어있지 않아야한다.")
    void changeNumberOfGuestsWrongTableEmpty() {
        // given
        Long tableId = 1L;
        OrderTable changeNumberTable = new OrderTable(null, null, 5, false);
        OrderTable table = new OrderTable(1L, null, 0, true);
        given(orderTableDao.findById(tableId)).willReturn(Optional.of(table));

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(tableId, changeNumberTable));
        assertEquals("비어있는 테이블의 손님 수를 변경할 수 없습니다.", exception.getMessage());
    }
}
