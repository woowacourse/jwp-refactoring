package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("테이블 서비스 테스트")
@MockitoSettings
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @DisplayName("테이블을 empty로 변경한다. - 실패, orderTableId에 해당하는 테이블이 존재하지 않는 경우")
    @Test
    void changeEmptyFailedWhenOrderTableIdNotFound() {
        // given
        long orderTableId = -1L;
        OrderTable orderTable = new OrderTable();
        given(orderTableDao.findById(orderTableId)).willThrow(IllegalArgumentException.class);

        // when - then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableDao).should(times(1))
                .findById(orderTableId);
        then(orderDao).should(never())
                .existsByOrderTableIdAndOrderStatusIn(
                        orderTableId,
                        Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
                );
        then(orderTableDao).should(never())
                .save(any(OrderTable.class));
    }

    @DisplayName("테이블을 empty로 변경한다. - 실패, orderTable의 tableGroupId가 이미 등록되어있는 경우")
    @Test
    void changeEmptyFailedWhenTableGroupIdIsNull() {
        // given
        long orderTableId = 1L;
        OrderTable orderTable = new OrderTable();

        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setId(1L);
        savedOrderTable.setTableGroupId(1L);

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));

        // when - then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableDao).should(times(1))
                .findById(orderTableId);
        then(orderDao).should(never())
                .existsByOrderTableIdAndOrderStatusIn(
                        orderTableId,
                        Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
                );
        then(orderTableDao).should(never())
                .save(any(OrderTable.class));
    }

    @DisplayName("테이블을 empty로 변경한다. - 실패, COOKING, MEAL 상태인 주문이 포함된 orderTableId가 아닌 경우")
    @Test
    void changeEmptyFailedWhenStatusNotSatisfied() {
        // given
        long orderTableId = 1L;
        OrderTable orderTable = new OrderTable();
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setId(1L);

        // savedOrderTable에 포함된 order가 COMPLETION 상태임을 나타내기 위함.
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(
                orderTableId,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
        ).willThrow(IllegalArgumentException.class);

        // when - then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableDao).should(times(1))
                .findById(orderTableId);
        then(orderDao).should(times(1))
                .existsByOrderTableIdAndOrderStatusIn(
                        orderTableId,
                        Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
                );
        then(orderTableDao).should(never())
                .save(any(OrderTable.class));
    }

    @DisplayName("방문 손님 수를 변경한다. - 실패, 방문 손님 수가 음수인 경우")
    @Test
    void changeNumberOfGuestsFailedWhenNegative() {
        // given
        long orderTableId = 1L;
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(-1);

        // when - then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableDao).should(never())
                .findById(orderTableId);
        then(orderTableDao).should(never())
                .save(any(OrderTable.class));
    }

    @DisplayName("방문 손님 수를 변경한다. - 실패, orderTableId가 존재하지 않는 경우")
    @Test
    void changeNumberOfGuestsFailedWhenOrderTableIdNotFound() {
        // given
        long orderTableId = -1L;
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(10);

        given(orderTableDao.findById(orderTableId)).willThrow(IllegalArgumentException.class);

        // when - then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableDao).should(times(1))
                .findById(orderTableId);
        then(orderTableDao).should(never())
                .save(any(OrderTable.class));
    }

    @DisplayName("방문 손님 수를 변경한다. - 실패, orderTableId로 조회한 테이블이 비어있는 경우")
    @Test
    void changeNumberOfGuestsFailedWhenTableIsEmpty() {
        // given
        long orderTableId = 1L;
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(10);

        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setNumberOfGuests(10);
        savedOrderTable.setEmpty(true);

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(savedOrderTable));

        // when - then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderTableDao).should(times(1))
                .findById(orderTableId);
        then(orderTableDao).should(never())
                .save(any(OrderTable.class));
    }
}
