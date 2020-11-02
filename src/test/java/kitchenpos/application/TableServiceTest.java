package kitchenpos.application;

import static kitchenpos.util.ObjectUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    @DisplayName("테이블 생성")
    void create() {
        OrderTable orderTable = createOrderTable(null, null, 0, false);

        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        OrderTable result = tableService.create(orderTable);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("테이블 목록 조회")
    void list() {
        OrderTable orderTable = createOrderTable(1L, 2L, 4, false);
        given(orderTableDao.findAll()).willReturn(Collections.singletonList(orderTable));

        List<OrderTable> result = tableService.list();

        assertThat(result).isNotNull();
        assertAll(
            () -> assertThat(result).hasSize(1),
            () -> assertThat(result.get(0)).usingRecursiveComparison()
                .isEqualTo(orderTable)
        );
    }

    @Test
    @DisplayName("주문 테이블로 상태 변경")
    void changeEmpty() {
        OrderTable saved = createOrderTable(1L, null, 0, true);
        OrderTable ordered = createOrderTable(null, null, 0, false);

        given(orderTableDao.findById(1L)).willReturn(Optional.of(saved));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(ordered);

        OrderTable result = tableService.changeEmpty(1L, ordered);

        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블이 다른 테이블 그룹에 속하는 경우 예외 발생")
    void tableBelongToOtherTableGroup() {
        OrderTable saved = createOrderTable(1L, 3L, 0, true);
        OrderTable ordered = createOrderTable(null, null, 0, false);

        given(orderTableDao.findById(1L)).willReturn(Optional.of(saved));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, ordered))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문이 완료되지 않은 경우 예외 발생")
    void orderNotCompleted() {
        OrderTable saved = createOrderTable(1L, null, 0, true);
        OrderTable ordered = createOrderTable(null, null, 0, false);

        given(orderTableDao.findById(1L)).willReturn(Optional.of(saved));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(1L, ordered))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블의 고객수 변경")
    void changeNumberOfGuests() {
        OrderTable saved = createOrderTable(null, null, 0, false);
        OrderTable ordered = createOrderTable(null, null, 2, false);

        given(orderTableDao.findById(1L)).willReturn(Optional.of(saved));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(ordered);

        OrderTable result = tableService.changeNumberOfGuests(1L, ordered);

        assertThat(result).isNotNull();
        assertThat(result.getNumberOfGuests()).isEqualTo(2);
    }

    @Test
    @DisplayName("테이블의 고객 수가 0 미만인 경우 예외 발생")
    void numberOfGuestsBelowZero() {
        OrderTable ordered = createOrderTable(null, null, -2, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, ordered))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 빈상태인 경우 예외 발생")
    void emptyTable() {
        OrderTable saved = createOrderTable(null, null, 0, true);
        OrderTable ordered = createOrderTable(null, null, 2, false);

        given(orderTableDao.findById(1L)).willReturn(Optional.of(saved));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, ordered))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
