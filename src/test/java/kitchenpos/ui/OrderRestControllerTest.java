package kitchenpos.ui;

import kitchenpos.application.OrderService;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static java.util.Collections.singletonList;
import static kitchenpos.fixture.FixtureFactory.createOrder;
import static kitchenpos.fixture.FixtureFactory.createOrderLineItem;
import static kitchenpos.ui.OrderRestController.ORDER_API;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = OrderRestController.class)
class OrderRestControllerTest extends ControllerTest {
    @MockBean
    private OrderService orderService;

    @DisplayName("주문 생성 요청")
    @Test
    void create() throws Exception {
        Order request = createOrder(null, 1L, null, null,
                singletonList(createOrderLineItem(null, 1L, 1L)));
        String body = objectMapper.writeValueAsString(request);

        when(orderService.create(any())).thenReturn(new Order());

        requestWithPost(ORDER_API, body);
    }

    @DisplayName("주문 목록 조회 요청")
    @Test
    void list() throws Exception {
        requestWithGet(ORDER_API);
    }

    @DisplayName("주문 상태 변경 요청")
    @Test
    void changeOrderStatus() throws Exception {
        Order request = createOrder(null, null, OrderStatus.MEAL.name(), null, null);
        String body = objectMapper.writeValueAsString(request);

        when(orderService.changeOrderStatus(anyLong(), any())).thenReturn(new Order());

        requestWithPut(ORDER_API + "/1/order-status", body);
    }
}