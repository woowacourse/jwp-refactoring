package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("테이블 서비스 테스트")
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable();
        OrderTable created = tableService.create(orderTable);
        assertThat(created.getId()).isNotNull();
    }

    @DisplayName("테이블 목록을 불러온다.")
    @Test
    void list() {
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();
        tableService.create(orderTable1);
        tableService.create(orderTable2);

        List<OrderTable> orderTables = tableService.list();
        assertThat(orderTables.size()).isEqualTo(2);
    }

    @DisplayName("테이블을 빈 테이블로 변경한다.")
    @Test
    void changeEmpty() {
        Order order = orderService.create(order());
        order.changeStatus(OrderStatus.COMPLETION);
        orderService.changeOrderStatus(order.getId(), order);

        OrderTable changed = tableService
            .changeEmpty(order.getOrderTableId(), OrderTable.EMPTY_TABLE);

        assertThat(changed.isEmpty()).isTrue();
    }

    @DisplayName("테이블의 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(1, false);
        OrderTable created = tableService.create(orderTable);

        created.setNumberOfGuests(3);
        OrderTable changed = tableService
            .changeNumberOfGuests(created.getId(), created);

        assertAll(
            () -> assertThat(changed.getId()).isEqualTo(created.getId()),
            () -> assertThat(changed.getNumberOfGuests()).isEqualTo(3),
            () -> assertThat(changed.isEmpty()).isFalse()
        );
    }
}