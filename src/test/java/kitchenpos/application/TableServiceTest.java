package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
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

    @Test
    void order_table을_저장할_수_있다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(3);
        when(orderTableDao.save(any(OrderTable.class))).thenReturn(orderTable);

        // when
        OrderTable savedOrderTable = tableService.create(orderTable);

        // then
        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    void order_table_목록을_조회할_수_있다() {
        // given
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(orderTable1, orderTable2));

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).hasSize(2);
    }

    @Test
    void empty_변경_시_일치하는_order_table_id가_없을_시_예외를_반환한다() {
        // given
        when(orderTableDao.findById(any(Long.class))).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void empty_변경_시_table_group_id_값이_null이_아니면_예외를_반환한다() {
        // given
        OrderTable orderTable = new OrderTable();
        OrderTable foundOrderTable = new OrderTable();
        foundOrderTable.setTableGroupId(1L);
        when(orderTableDao.findById(any(Long.class))).thenReturn(Optional.of(foundOrderTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void empty_변경_시_order_상태가_COOKING_또는_MEAL_상태이면_예외를_반환한다() {
        // given
        OrderTable orderTable = new OrderTable();
        OrderTable foundOrderTable = new OrderTable();
        when(orderTableDao.findById(any(Long.class))).thenReturn(Optional.of(foundOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(COOKING.name(), MEAL.name()))).thenReturn(
                true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void empty를_변경할_수_있다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        OrderTable foundOrderTable = new OrderTable();
        when(orderTableDao.findById(any(Long.class))).thenReturn(Optional.of(foundOrderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(COOKING.name(), MEAL.name()))).thenReturn(
                false);

        // when
        tableService.changeEmpty(1L, orderTable);

        // then
        assertThat(foundOrderTable.isEmpty()).isTrue();
    }

    @Test
    void 손님의_수를_변경할_시_손님의_수가_0보다_작으면_예외를_반환한다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(-1);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님의_수를_변경할_시_ID에_맞는_order_table이_없을_경우_예외를_반환한다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(3);
        when(orderTableDao.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님의_수를_변경할_시_order_table이_비어있으면_예외를_반환한다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(3);
        OrderTable foundOrderTable = new OrderTable();
        foundOrderTable.setEmpty(true);
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(foundOrderTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님의_수를_변경할_수_있다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(3);
        OrderTable foundOrderTable = new OrderTable();
        foundOrderTable.setEmpty(false);
        when(orderTableDao.findById(1L)).thenReturn(Optional.of(foundOrderTable));

        // when
        tableService.changeNumberOfGuests(1L, orderTable);

        // then
        assertThat(foundOrderTable.getNumberOfGuests()).isEqualTo(3);
    }
}
