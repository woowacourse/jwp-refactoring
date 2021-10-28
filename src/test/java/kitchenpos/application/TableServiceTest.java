package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
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

    private OrderTable 테이블_A;
    private OrderTable 테이블_B;
    private OrderTable 테이블_C;
    private OrderTable 테이블_D;

    @BeforeEach
    void setUp() {
        테이블_A = orderTableConstructor(1L, 1L, 2, true);
        테이블_B = orderTableConstructor(2L, 1L, 2, false);
        테이블_C = orderTableConstructor(3L, 2L, 4, true);
        테이블_D = orderTableConstructor(4L, 2L, 6, false);
    }

    @DisplayName("테이블을 등록한다.")
    @Test
    void create() {
        //given
        OrderTable expected = orderTableConstructor(1L, 1L, 5, true);
        OrderTable orderTable = orderTableConstructor(5);
        given(tableService.create(orderTable)).willReturn(expected);

        //when
        OrderTable actual = tableService.create(orderTable);

        //then
        assertEquals(actual, expected);
    }

    @DisplayName("테이블 리스트를 조회한다.")
    @Test
    void readAll() {
        //given
        List<OrderTable> expected = Arrays.asList(
            테이블_A,
            테이블_B,
            테이블_C,
            테이블_D
        );
        given(orderTableDao.findAll()).willReturn(expected);

        //when
        List<OrderTable> actual = tableService.list();

        //then
        assertEquals(actual, expected);
    }

    @DisplayName("빈 테이블의 상태를 변경한다.")
    @Test
    void changeEmptyTrue() {
        //given
        Long orderTableId = 4L;
        OrderTable notEmptyTable = orderTableConstructor(5, false);
        OrderTable before = orderTableConstructor(orderTableId, null, 5, true);
        OrderTable afterExpected = orderTableConstructor(orderTableId, null, 5, false);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(before));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(before)).willReturn(before);

        //when
        OrderTable actual = tableService.changeEmpty(orderTableId, notEmptyTable);

        //then
        assertFalse(actual.isEmpty());
        assertThat(actual).usingRecursiveComparison().isEqualTo(afterExpected);
    }

    @DisplayName("존재하지 않는 테이블을 변경하면 예외가 발생한다.")
    @Test
    void changeWhenNullTable() {
        //given
        Long orderTableId = 4L;
        OrderTable notEmptyTable = orderTableConstructor(5, false);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, notEmptyTable))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹에 속한 테이블을 변경하면 예외가 발생한다.")
    @Test
    void changeWhenJoinGroupTable() {
        //given
        Long orderTableId = 4L;
        OrderTable notEmptyTable = orderTableConstructor(5, false);
        OrderTable before = orderTableConstructor(orderTableId, 1L, 5, false);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(before));

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, notEmptyTable))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        Long orderTableId = 4L;
        OrderTable changeNumberOfGuestTable = orderTableConstructor(8, false);
        OrderTable before = orderTableConstructor(orderTableId, null, 5, false);
        OrderTable afterExpected = orderTableConstructor(orderTableId, null, 8, false);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(before));
        given(tableService.changeNumberOfGuests(orderTableId, changeNumberOfGuestTable)).willReturn(afterExpected);

        //when
        OrderTable actual = tableService.changeNumberOfGuests(orderTableId, changeNumberOfGuestTable);

        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(afterExpected);
    }

    @DisplayName("변경하려는 테이블의 손님 수가 0 미만이면 예외가 발생한다.")
    @Test
    void changeWhenNumberOfGuestsLessThanZero() {
        //given
        Long orderTableId = 4L;
        OrderTable changeNumberOfGuestTable = orderTableConstructor(-1, false);
        OrderTable before = orderTableConstructor(orderTableId, null, 5, false);
        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, changeNumberOfGuestTable))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("변경하려는 테이블이 비어있으면 예외가 발생한다.")
    @Test
    void changeWhenNumberOfGuestsEmptyTable() {
        //given
        Long orderTableId = 4L;
        OrderTable changeNumberOfGuestTable = orderTableConstructor(3, true);
        OrderTable before = orderTableConstructor(orderTableId, null, 5, false);
        OrderTable afterExpected = orderTableConstructor(orderTableId, null, 8, false);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(before));
        given(tableService.changeNumberOfGuests(orderTableId, changeNumberOfGuestTable)).willReturn(afterExpected);

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, changeNumberOfGuestTable))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조리 중이거나 식사 중인 테이블을 변경하면 예외가 발생한다.")
    @Test
    void changeWhenCookingOrMealTable() {
        //given
        Long orderTableId = 4L;
        OrderTable notEmptyTable = orderTableConstructor(5, false);
        OrderTable before = orderTableConstructor(orderTableId, null, 5, true);
        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(before));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(
            orderTableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        //then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, notEmptyTable))
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable orderTableConstructor(final int numberOfGuests) {
        return orderTableConstructor(null, null, numberOfGuests, true);
    }

    private OrderTable orderTableConstructor(final int numberOfGuests, final boolean empty) {
        return orderTableConstructor(null, null, numberOfGuests, empty);
    }

    private OrderTable orderTableConstructor(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);

        return orderTable;
    }
}
