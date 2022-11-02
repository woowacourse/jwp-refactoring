package kitchenpos.core.order.application;

import static kitchenpos.core.order.domain.OrderStatus.COMPLETION;
import static kitchenpos.fixture.OrderFixture.getOrderRequest;
import static kitchenpos.fixture.TableFixture.getOrderTableRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.common.IntegrationTest;
import kitchenpos.core.order.domain.Order;
import kitchenpos.core.order.domain.OrderChangeEvent;
import kitchenpos.core.table.application.TableService;
import kitchenpos.core.table.domain.OrderTable;
import kitchenpos.core.table.domain.OrderTableDao;
import kitchenpos.support.event.Events;
import kitchenpos.support.event.EventsConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import(EventsConfiguration.class)
public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private Events events;

    @AfterEach
    void cleanUp() {
        events.clear();
    }

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

    @Test
    void 주문의_상태가_변하면_테이블의_주문상태도_변한다() {
        final OrderTable orderTable = tableService.create(getOrderTableRequest(1L, 10, false));
        final Order order = orderService.create(getOrderRequest(orderTable.getId()));

        orderService.changeOrderStatus(order.getId(), getOrderRequest(orderTable.getId(), COMPLETION.name()));

        final OrderTable actual = orderTableDao.findById(orderTable.getId()).get();
        assertAll(
                () -> assertThat(actual.getOrderStatus()).isEqualTo(COMPLETION),
                () -> assertThat(events.count(OrderChangeEvent.class)).isEqualTo(1)
        );
    }
}
