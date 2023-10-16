package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Test
    void 주문_테이블을_생성한다() {
        // given
        final OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setId(1L);

        when(orderTableDao.save(any(OrderTable.class)))
                .thenReturn(savedOrderTable);

        // when
        final OrderTable result = tableService.create(new OrderTable());

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1),
                () -> assertThat(result.getTableGroupId()).isNull()
        );
    }

    @Test
    void 전체_주문_테이블_목록을_가져온다() {
        // given
        when(orderTableDao.findAll())
                .thenReturn(Collections.emptyList());

        // when
        final List<OrderTable> result = tableService.list();

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void 주문_테이블의_빈_상태를_변경한다() {
        // given
        final OrderTable savedOrderTable = new OrderTable();

        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(savedOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any()))
                .thenReturn(false);
        when(orderTableDao.save(any(OrderTable.class)))
                .thenReturn(savedOrderTable);

        // when
        final OrderTable result = tableService.changeEmpty(1L, new OrderTable());

        // then
        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    void 주문_테이블의_빈_상태를_변경할_때_주문_테이블이_어떤_테이블_그룹에_속해_있을_때_실패한다() {
        // given
        final OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setTableGroupId(1L);

        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(savedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_빈_상태를_변경할_때_주문_테이블의_주문_상태가_COOKING이나_MEAL이면_실패한다() {
        // given
        final OrderTable savedOrderTable = new OrderTable();

        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(savedOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), any()))
                .thenReturn(true);

        // when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_사용자_수를_변경한다() {
        // given
        final OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setNumberOfGuests(1);

        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(savedOrderTable));
        when(orderTableDao.save(any(OrderTable.class)))
                .thenReturn(savedOrderTable);

        // when
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);
        final OrderTable result = tableService.changeNumberOfGuests(1L, orderTable);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(1);
    }

    @Test
    void 주문_테이블의_사용자_수를_변경할_때_전달받은_사용자_수가_0보다_작으면_실패한다() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(-1);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_사용자_수를_변경할_때_존재한는_주문_테이블이_아니면_실패한다() {
        // given
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_사용자_수를_변경할_때_주문_테이블이_빈_상태면_실패한다() {
        // given
        final OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setEmpty(true);
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(savedOrderTable));

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}