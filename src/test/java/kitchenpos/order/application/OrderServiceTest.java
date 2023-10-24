package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.presentation.dto.ChangeOrderStatusRequest;
import kitchenpos.order.presentation.dto.CreateOrderRequest;
import kitchenpos.order.presentation.dto.OrderLineItemRequest;
import kitchenpos.support.TestSupporter;
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
    private TestSupporter testSupporter;

    @Test
    void 주문을_생성한다() {
        // given
        final OrderTable orderTable = testSupporter.createOrderTable();
        final Menu menu = testSupporter.createMenu();
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
        final Order order = testSupporter.createOrder();

        // when
        final List<Order> orders = orderService.list();

        // then
        assertThat(orders.get(0)).isEqualTo(order);
    }

    @Test
    void 주문_상태를_변경한다() {
        // given
        final Order order = testSupporter.createOrder();
        final String orderStatus = "COMPLETION";
        final ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(orderStatus);

        // when
        final Order actual = orderService.changeOrderStatus(order.getId(),
                                                            changeOrderStatusRequest);

        // then
        assertThat(actual.getOrderStatus().name()).isEqualTo(orderStatus);
    }

    @Test
    void 주문을_생성할_때_주문_테이블이_실재하지_않는다면_예외가_발생한다() {
        // given
        final OrderTable orderTable = testSupporter.createOrderTable(false);
        final Menu menu = testSupporter.createMenu();
        final OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(menu.getId(), 1);
        final OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(menu.getId(), 1);
        final CreateOrderRequest createOrderRequest = new CreateOrderRequest(orderTable.getId() + 1,
                                                                             List.of(orderLineItemRequest1,
                                                                                     orderLineItemRequest2));

        // when & then
        assertThatThrownBy(() -> orderService.create(createOrderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할_때_주문_테이블이_빈_테이블이라면_예외가_발생한다() {
        // given
        final OrderTable orderTable = testSupporter.createOrderTable(true);
        final Menu menu = testSupporter.createMenu();
        final OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(menu.getId(), 1);
        final OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(menu.getId(), 1);
        final CreateOrderRequest createOrderRequest = new CreateOrderRequest(orderTable.getId(),
                                                                             List.of(orderLineItemRequest1,
                                                                                     orderLineItemRequest2));

        // when & then
        assertThatThrownBy(() -> orderService.create(createOrderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태를_변경할_때_주문이_실재하지_않으면_예외가_발생한다() {
        // given
        final Order order = testSupporter.createOrder();
        final ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest("COMPLETION");

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId() + 1, changeOrderStatusRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태를_변경할_때_기존_주문의_주문_상태가_COMPLETION_이라면_예외가_발생한다() {
        // given
        final Order order = testSupporter.createOrder();
        final ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest("COMPLETION");
        orderService.changeOrderStatus(order.getId(), changeOrderStatusRequest);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrderStatusRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
