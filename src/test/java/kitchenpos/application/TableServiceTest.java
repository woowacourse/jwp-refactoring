package kitchenpos.application;

import static kitchenpos.fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.common.TestObjectUtils;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

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
    void createTable() {
        OrderTable createOrderTable =
                TestObjectUtils.createOrderTable(null, 1L, 0, true);
        when(orderTableDao.save(any())).thenReturn(ORDER_TABLE1);

        assertThat(tableService.create(createOrderTable).getNumberOfGuests()).isEqualTo(0);
    }

    @DisplayName("주문 테이블의 목록을 조회할 수 있다.")
    @Test
    void listTest() {
        when(orderTableDao.findAll()).thenReturn(ORDER_TABLES1);

        List<OrderTable> list = tableService.list();
        assertAll(
                () -> assertThat(list.size()).isEqualTo(2),
                () -> assertThat(list.get(0).getId()).isEqualTo(1L),
                () -> assertThat(list.get(1).getId()).isEqualTo(2L)
        );
    }

    @DisplayName("단체 지정된 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.")
    @Test
    void notChangeEmptyTest_when_tableGroupNotNull() {
        when(orderTableDao.findById(any())).thenReturn(Optional.of(ORDER_TABLE3));

        assertThatThrownBy(
                () -> tableService.changeEmpty(ORDER_TABLE3.getId(), CHANGING_GUEST_ORDER_TABLE))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("주문 상태가 조리 또는 식사인 주문 테이블은 빈 테이블 설정 또는 해지할 수 없다.")
    @Test
    void notChangeEmptyTest_when_orderStatusIsMEALAndCOOKING() {
        when(orderTableDao.findById(any())).thenReturn(Optional.of(ORDER_TABLE1));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).thenReturn(true);

        assertThatThrownBy(
                () -> tableService.changeEmpty(ORDER_TABLE1.getId(), CHANGING_GUEST_ORDER_TABLE)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수를 입력할 수 있다.")
    @Test
    void changeNumberOfGuestsTest() {
        when(orderTableDao.findById(any())).thenReturn(Optional.of(ORDER_TABLE3));
        when(orderTableDao.save(any())).thenReturn(CHANGING_GUEST_ORDER_TABLE);

        assertThat(tableService.changeNumberOfGuests(ORDER_TABLE3.getId(),
                CHANGING_GUEST_ORDER_TABLE)
                .getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("방문한 손님 수가 올바르지 않으면 입력할 수 없다.")
    @Test
    void notChangeNumberOfGuestsTest_when_invalidNumberOfGuest() {
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(ORDER_TABLE1.getId(),
                        INVALID_NUMBER_OF_GUEST_ORDER_TABLE)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블은 방문한 손님 수를 입력할 수 없다.")
    @Test
    void notChangeNumberOfGuestsTest_when_emptyTable() {
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(ORDER_TABLE1.getId(), EMPTY_ORDER_TABLE)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}