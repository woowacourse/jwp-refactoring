package kitchenpos.core.order.application;

import static kitchenpos.fixture.OrderFixture.getOrderRequest;
import static kitchenpos.fixture.TableFixture.getOrderTableRequest;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.common.IntegrationTest;
import kitchenpos.core.order.domain.Order;
import kitchenpos.core.table.application.TableService;
import kitchenpos.core.table.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Test
    void 주문을_생성한다() {
        final OrderTable orderTable = tableService.create(getOrderTableRequest(1L, 10, false));
        final Order order = orderService.create(getOrderRequest(orderTable.getId()));
        assertThat(order.getId()).isNotNull();
    }

    @Test
    void 주문목록을_조회한다() {
        final OrderTable orderTable = tableService.create(getOrderTableRequest(1L, 10, false));
        orderService.create(getOrderRequest(orderTable.getId()));
        assertThat(orderService.list()).hasSize(1);
    }
}
