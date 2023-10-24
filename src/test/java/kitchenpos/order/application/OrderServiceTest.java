package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.presentation.dto.ChangeOrderStatusRequest;
import kitchenpos.order.presentation.dto.CreateOrderRequest;
import kitchenpos.order.presentation.dto.OrderLineItemRequest;
import kitchenpos.support.NewTestSupporter;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Sql("/truncate.sql")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private NewTestSupporter newTestSupporter;

    @Test
    void 주문을_생성한다() {
        // given
        final OrderTable orderTable = newTestSupporter.createOrderTable();
        final Menu menu = newTestSupporter.createMenu();
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(),
                                                                                   3);
        final CreateOrderRequest createOrderRequest = new CreateOrderRequest(orderTable.getId(),
                                                                             List.of(orderLineItemRequest));

        // when
        final Order order = orderService.create(createOrderRequest);

        // then
        assertThat(order).isNotNull();
    }

    @Test
    void 주문에_대해_전체_조회한다() {
        // given
        final Order order = newTestSupporter.createOrder();

        // when
        final List<Order> orders = orderService.list();

        // then
        assertThat(orders.get(0)).isEqualTo(order);
    }

    @Test
    void 주문_상태를_변경한다() {
        // given
        final Order order = newTestSupporter.createOrder();
        final String orderStatus = "COMPLETION";
        final ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(orderStatus);

        // when
        final Order actual = orderService.changeOrderStatus(order.getId(),
                                                            changeOrderStatusRequest);

        // then
        assertThat(actual.getOrderStatus().name()).isEqualTo(orderStatus);
    }
}
