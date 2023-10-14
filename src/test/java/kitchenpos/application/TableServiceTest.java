package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;

    @Test
    void 테이블을_등록할_수_있다() {
        // given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(0);

        // when
        final OrderTable result = tableService.create(orderTable);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.isEmpty()).isTrue();
        assertThat(result.getNumberOfGuests()).isZero();
    }

    @Test
    void 테이블목록을_조회할_수_있다() {
        // given
        createOrderTable(true, 0);

        // when
        final List<OrderTable> results = tableService.list();

        // then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).isEmpty()).isTrue();
        assertThat(results.get(0).getNumberOfGuests()).isZero();
    }

    @Test
    void 테이블의_비어있는_상태를_변경할_수_있다() {
        // given
        final OrderTable savedOrderTable = createOrderTable(true, 0);

        final OrderTable updateTable = new OrderTable();
        updateTable.setEmpty(false);

        // when
        final OrderTable result = tableService.changeEmpty(savedOrderTable.getId(), updateTable);

        // then
        assertThat(result.isEmpty()).isFalse();
    }

    @Test
    void 테이블의_손님수를_변경할_수_있다() {
        // given
        final OrderTable orderTable = createOrderTable(false, 0);

        final OrderTable savedTable = tableService.create(orderTable);

        final OrderTable updateTable = new OrderTable();
        updateTable.setNumberOfGuests(4);

        // when
        final OrderTable result = tableService.changeNumberOfGuests(savedTable.getId(), updateTable);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(4);
    }

    @Test
    void 빈_테이블에_손님수를_변경할_때_예외가_발생한다() {
        // given when
        final OrderTable savedOrderTable = createOrderTable(true, 0);

        final OrderTable updateTable = new OrderTable();
        updateTable.setNumberOfGuests(4);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), updateTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님수가_음수일_때_예외가_발생한다() {
        // given
        final OrderTable savedOrderTable = createOrderTable(false, 0);

        // when
        final OrderTable updateTable = new OrderTable();
        updateTable.setNumberOfGuests(-4);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), updateTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_테이블을_수정할때_예외가_발생한다() {
        // given
        final OrderTable updateTable = new OrderTable();
        updateTable.setEmpty(false);

        // when
        final Long notExistOrderTableId = 99999L;

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(notExistOrderTableId, updateTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 조리_또는_식사_중인_테이블의_빈상태를_변경할_때_예외가_발생한다() {
        // given
        final OrderTable savedOrderTable = createOrderTable(false, 2);

        // when
        final Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderedTime(LocalDateTime.now());

        orderDao.save(order);

        final OrderTable updateTable = new OrderTable();
        updateTable.setEmpty(true);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), updateTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable createOrderTable(final boolean emptyStatus, final int guests) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(emptyStatus);
        orderTable.setNumberOfGuests(guests);
        return tableService.create(orderTable);
    }
}
