package kitchenpos.ui;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.application.OrderService;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.utils.TestFixture;

class OrderRestControllerTest extends ControllerTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("create: 주문 등록 테스트")
    @Test
    void createTest() throws Exception {
        final OrderTable orderTable = orderTableDao.save(TestFixture.getOrderTableWithNotEmpty());
        final OrderLineItem orderLineItem = TestFixture.getOrderLineItem();
        final Order order = TestFixture.getOrderWithCooking(orderLineItem, orderTable.getId());

        create("/api/orders", order)
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.orderTableId").value(14));
    }

    @DisplayName("전체 주문을 확인하는 테스트")
    @Test
    void listTest() throws Exception {
        final OrderTable orderTable = orderTableDao.save(TestFixture.getOrderTableWithNotEmpty());
        final OrderLineItem orderLineItem = TestFixture.getOrderLineItem();
        final Order order = TestFixture.getOrderWithCooking(orderLineItem, orderTable.getId());

        orderService.create(order);

        findList("/api/orders")
                .andExpect(jsonPath("$.[0].id").value(2));
    }

    @DisplayName("changeOrderStatus: 주문 상태를 변경하는 테스트")
    @Test
    void changeOrderStatusTest() throws Exception {
        final OrderTable orderTable = orderTableDao.save(TestFixture.getOrderTableWithNotEmpty());
        final OrderLineItem orderLineItem = TestFixture.getOrderLineItem();
        final Order orderWithCooking = TestFixture.getOrderWithCooking(orderLineItem, orderTable.getId());
        final Order orderWithCompletion = TestFixture.getOrderWithCompletion(orderLineItem);

        final Order order = orderService.create(orderWithCooking);

        update("/api/orders/" + order.getId() + "/order-status", orderWithCompletion)
                .andExpect(jsonPath("$.orderStatus").value("COMPLETION"));
    }
}
