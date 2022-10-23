package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class TableServiceTest {

    private final TableService tableService;
    private final OrderService orderService;

    TableServiceTest(TableService tableService, OrderService orderService) {
        this.tableService = tableService;
        this.orderService = orderService;
    }

    @Test
    void 테이블을_생성한다() {
        OrderTable orderTable = new OrderTable(null, 0, true);

        assertThat(tableService.create(orderTable)).isInstanceOf(OrderTable.class);
    }

    @Test
    void 테이블을_모두_조회한다() {
        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSize(9);
    }

    @Test
    void 테이블을_비운다() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        OrderTable savedTable = tableService.create(orderTable);

        OrderTable result = tableService.changeEmpty(savedTable.getId(), new OrderTable(1L, 0, true));

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void 테이블을_비울때_테이블이_존재하지_않을_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> tableService.changeEmpty(-1L, null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블을_비울때_조리중이거나_식사중인_주문이_있는_경우_예외를_발생시킨다() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        OrderTable savedTable = tableService.create(orderTable);
        Order order = new Order(savedTable.getId(), "NONE", LocalDateTime.now(),
                List.of(new OrderLineItem(1L, 1L, 1L, 1)));
        orderService.create(order);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), new OrderTable(1L, 0, true)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_손님수를_변경한다() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        OrderTable savedTable = tableService.create(orderTable);

        orderTable.setNumberOfGuests(1);
        OrderTable result = tableService.changeNumberOfGuests(savedTable.getId(), orderTable);

        assertThat(result.getNumberOfGuests()).isEqualTo(1);
    }

    @Test
    void 테이블의_손님수를_변경할때_수정할_손님수가_0보다_작을때_예외를_발생시킨다() {
        OrderTable orderTable = new OrderTable(null, 0, false);
        OrderTable savedTable = tableService.create(orderTable);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), new OrderTable(null, -1, false)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_손님수를_변경할때_테이블이_존재하지_않는_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, new OrderTable(null, 1, false)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_손님수를_변경할때_이미_비어있는_경우_예외를_발생시킨다() {
        OrderTable orderTable = new OrderTable(null, 0, true);
        OrderTable savedTable = tableService.create(orderTable);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), new OrderTable(null, 1, true)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
