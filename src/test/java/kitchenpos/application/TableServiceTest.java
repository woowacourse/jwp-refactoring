package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderDao orderDao;

    @Test
    void 테이블을_생성할_수_있다() {
        OrderTable orderTable = new OrderTable(null, 5, false);

        OrderTable actual = tableService.create(orderTable);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getNumberOfGuests()).isEqualTo(5);
            assertThat(actual.isEmpty()).isFalse();
        });
    }

    @Test
    void 전체_테이블을_조회할_수_있다() {
        OrderTable orderTable1 = new OrderTable(null, 3, false);
        OrderTable orderTable2 = new OrderTable(null, 5, false);

        tableService.create(orderTable1);
        tableService.create(orderTable2);

        List<OrderTable> actual = tableService.list();

        assertThat(actual).hasSize(2);
    }

    @Test
    void 기존_테이블의_빈_테이블_여부를_변경할_수_있다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 5, false));
        OrderTable newOrderTable = new OrderTable(null, 0, true);

        OrderTable actual = tableService.changeEmpty(orderTable.getId(), newOrderTable);

        assertThat(actual.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @EnumSource(mode = EXCLUDE, names = {"COMPLETION"})
    void 기존_테이블의_주문_상태가_완료_상태가_아니면_빈_테이블로_변경할_수_없다(OrderStatus orderStatus) {
        OrderTable orderTable = tableService.create(new OrderTable(null, 5, false));
        OrderTable newOrderTable = new OrderTable(null, 0, true);

        Order order = new Order(orderTable.getId(), orderStatus.name(), new ArrayList<>());
        orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 기존_테이블의_손님_수를_변경할_수_있다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 0, false));
        OrderTable newOrderTable = new OrderTable(null, 5, false);

        OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable);

        assertThat(actual.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    void 손님_수가_음수인_경우_손님_수를_변경할_수_없다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 0, false));
        OrderTable newOrderTable = new OrderTable(null, -1, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_테이블은_손님_수를_변경할_수_없다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 0, true));
        OrderTable newOrderTable = new OrderTable(null, 5, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
