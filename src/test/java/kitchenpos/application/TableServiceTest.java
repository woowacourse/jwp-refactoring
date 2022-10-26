package kitchenpos.application;

import static kitchenpos.Fixture.테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private OrderService orderService;

    @Test
    void 테이블을_생성한다() {
        OrderTable actual = tableService.create(테이블(true));
        assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 테이블을_모두_조회한다() {
        테이블_생성(true);

        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void 테이블을_비운다() {
        OrderTable result = tableService.changeEmpty(테이블_생성(false).getId(), 테이블(true));

        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void 테이블을_비울때_테이블이_존재하지_않을_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> tableService.changeEmpty(-1L, null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블을_비울때_조리중인_주문이_있는_경우_예외를_발생시킨다() {
        OrderTable savedTable = 테이블_생성(false);
        Order order = new Order(savedTable.getId(), List.of(new OrderLineItem(메뉴_생성().getId(), 1)));
        orderService.create(order);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), new OrderTable(0, true)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블을_비울때_식사중인_주문이_있는_경우_예외를_발생시킨다() {
        OrderTable savedTable = 테이블_생성(false);
        Order savedOrder = 주문_생성(savedTable.getId());
        savedOrder.setOrderStatus("MEAL");
        orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), new OrderTable(0, true)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_손님수를_변경한다() {
        OrderTable savedTable = 테이블_생성(false);

        savedTable.setNumberOfGuests(1);
        OrderTable result = tableService.changeNumberOfGuests(savedTable.getId(), savedTable);

        assertThat(result.getNumberOfGuests()).isEqualTo(1);
    }

    @Test
    void 테이블의_손님수를_변경할때_수정할_손님수가_0보다_작을때_예외를_발생시킨다() {
        OrderTable savedTable = 테이블_생성(false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
                savedTable.getId(), new OrderTable(-1, false)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_손님수를_변경할때_테이블이_존재하지_않는_경우_예외를_발생시킨다() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(-1L, 테이블(false)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_손님수를_변경할때_이미_비어있는_경우_예외를_발생시킨다() {
        OrderTable savedTable = 테이블_생성(true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), 테이블(true)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
