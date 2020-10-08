package kitchenpos.application;

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
class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("생성")
    void create() {
        OrderTable orderTable = new OrderTable();

        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        OrderTable result = tableService.create(orderTable);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("주문 테이블로 상태 변경")
    void changeEmpty() {
        OrderTable saved = new OrderTable();
        saved.setId(1L);
        saved.setEmpty(true);
        OrderTable ordered = new OrderTable();
        ordered.setEmpty(false);

        given(orderTableDao.findById(1L)).willReturn(Optional.of(saved));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(ordered);

        OrderTable result = tableService.changeEmpty(1L, ordered);

        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블이 다른 테이블 그룹에 속하는 경우")
    void tableBelongToOtherTableGroup() {
        OrderTable saved = new OrderTable();
        saved.setId(1L);
        saved.setEmpty(true);
        saved.setTableGroupId(3L);
        OrderTable ordered = new OrderTable();
        ordered.setEmpty(false);

        given(orderTableDao.findById(1L)).willReturn(Optional.of(saved));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, ordered))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문이 완료되지 않은 경우")
    void orderNotCompleted() {
        OrderTable saved = new OrderTable();
        saved.setId(1L);
        saved.setEmpty(true);
        OrderTable ordered = new OrderTable();
        ordered.setEmpty(false);

        given(orderTableDao.findById(1L)).willReturn(Optional.of(saved));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, ordered))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 고객수 변경")
    void changeNumberOfGuests() {
        OrderTable saved = new OrderTable();
        saved.setNumberOfGuests(0);
        saved.setEmpty(false);
        OrderTable ordered = new OrderTable();
        ordered.setNumberOfGuests(2);

        given(orderTableDao.findById(1L)).willReturn(Optional.of(saved));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(ordered);

        OrderTable result = tableService.changeNumberOfGuests(1L, ordered);

        assertThat(result).isNotNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(2);
    }

    @Test
    @DisplayName("테이블의 고객 수가 0 미만인 경우")
    void numberOfGuestsBelowZero() {
        OrderTable ordered = new OrderTable();
        ordered.setNumberOfGuests(-2);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, ordered))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 빈상태인 경우")
    void emptyTable() {
        OrderTable saved = new OrderTable();
        saved.setNumberOfGuests(0);
        saved.setEmpty(true);
        OrderTable ordered = new OrderTable();
        ordered.setNumberOfGuests(2);

        given(orderTableDao.findById(1L)).willReturn(Optional.of(saved));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, ordered))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
