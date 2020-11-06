package kitchenpos.application;

import static kitchenpos.application.ServiceIntegrationTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
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
public class TableServiceTest {
    @InjectMocks
    TableService tableService;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @DisplayName("테이블이 등록되어 있지 않으면 빈 테이블 여부를 변경할 수 없다.")
    @Test
    void changeEmpty_willThrowException_whenTableDoesNotExist() {
        given(orderTableDao.findById(anyLong())).willThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> tableService.changeEmpty(9L, getOrderTableNotEmpty()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블이 단체로 지정되어 있으면 빈 테이블 여부를 변경할 수 없다.")
    @Test
    void changeEmpty_willThrowException_whenTableHasTableGroup() {
        Long tableId = 3L;
        OrderTable orderTable = getOrderTableWithTableGroupId(tableId, 1L);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(tableId, getOrderTableNotEmpty()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 주문 상태가 조리 또는 식사인 경우 빈 테이블 여부를 변경할 수 없다.")
    @Test
    void changeEmpty_willThrowException_whenTableOrderStatusIsCookingOrMeal() {
        Long tableId = 2L;
        OrderTable orderTable = getOrderTableWithId(tableId);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(
            tableId, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))
        ).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(tableId, getOrderTableNotEmpty()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문한 손님 수가 0명 미만이면 손님 수를 입력할 수 없다.")
    @Test
    void changeNumberOfGuests_willThrowException_whenNumberIsUnderZero() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, getOrderTableWithGuests(-3)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 등록되어 있지 않으면 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_willThrowException_whenTableDoesNotExist() {
        given(orderTableDao.findById(anyLong())).willThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(2L, getOrderTableWithGuests(3)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블은 방문한 손님 수를 입력할 수 없다.")
    @Test
    void changeNumberOfGuests_willThrowException_whenTableIsEmpty() {
        Long tableId = 2L;
        OrderTable orderTable = getOrderTableEmpty(tableId);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(tableId, getOrderTableNotEmpty()))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
