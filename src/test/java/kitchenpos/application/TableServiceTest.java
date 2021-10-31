package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static kitchenpos.fixtures.OderTableFixture.*;
import static kitchenpos.fixtures.TableGroupFixture.첫번째테이블그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @DisplayName("주문 테이블을 등록할 수 있다.")
    @Test
    void create() {
        //given
        given(orderTableDao.save(any())).willReturn(첫번째주문테이블());

        //when
        OrderTable actual = tableService.create(new OrderTable());

        //then
        assertThat(actual.getId()).isEqualTo(1L);
    }

    @Test
    void list() {
    }

    @DisplayName("주문 테이블을 비울 수 있다.")
    @Test
    void changeEmpty() {
        //given
        OrderTable orderTable = 첫번째주문테이블();
        orderTable.setEmpty(true);
        given(orderTableDao.findById(any())).willReturn(Optional.of(new OrderTable()));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
                .willReturn(false);
        given(orderTableDao.save(any()))
                .willReturn(orderTable);

        //when
        OrderTable actual = tableService.changeEmpty(orderTable.getId(), orderTable);

        //then
        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("테이블그룹에 속한 주문 테이블은 비울 수 없다.")
    @Test
    void changeEmptyInTableGroup() {
        //given
        OrderTable orderTable = 테이블그룹주문테이블1(첫번째테이블그룹());
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

        OrderTable empty = new OrderTable();
        empty.setEmpty(true);

        //when, then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(orderTable.getId(), empty);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("조리, 식사 상태의 주문 테이블은 비울 수 없다.")
    @Test
    void changeEmptyOrderStatus() {
        //given
        OrderTable orderTable = 첫번째주문테이블();
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        OrderTable empty = new OrderTable();
        empty.setEmpty(true);

        //when, then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(orderTable.getId(), empty);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에 손님을 셋팅 할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable orderTable = 첫번째주문테이블();
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

        OrderTable withGuest = new OrderTable();
        withGuest.setNumberOfGuests(7);
        given(orderTableDao.save(any())).willReturn(첫번째주문테이블(7));

        //when
        OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), withGuest);

        //then
        assertThat(actual.getNumberOfGuests()).isEqualTo(7);
    }

    @DisplayName("방문한 손님 수가 0 명이여도 주문 테이블 셋팅이 가능하다.")
    @Test
    void changeNumberOfGuestsZero() {
        //given
        OrderTable orderTable = 첫번째주문테이블();
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

        OrderTable withGuest = new OrderTable();
        withGuest.setNumberOfGuests(0);
        given(orderTableDao.save(any())).willReturn(첫번째주문테이블(0));

        //when
        OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), withGuest);

        //then
        assertThat(actual.getNumberOfGuests()).isEqualTo(0);
    }

    @DisplayName("방문한 손님 수는 정수여야 한다.")
    @Test
    void changeNumberOfGuestsNegative() {
        //given
        OrderTable orderTable = 첫번째주문테이블();
        OrderTable withGuest = new OrderTable();
        withGuest.setNumberOfGuests(-1);

        //when, then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTable.getId(), withGuest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 빈 상태면 손님을 셋팅 할 수 없다.")
    @Test
    void changeNumberOfGuestsEmpty() {
        //given
        OrderTable orderTable = 비어있는주문테이블1();
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        OrderTable withGuest = new OrderTable();
        withGuest.setNumberOfGuests(7);

        //when, then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(orderTable.getId(), withGuest);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}