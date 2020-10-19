package kitchenpos.ui;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderChangeStatusRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.utils.TestFixture;

class OrderRestControllerTest extends ControllerTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("create: 주문 등록 테스트")
    @Test
    void createTest() throws Exception {
        final OrderTable orderTable = orderTableDao.save(new OrderTable(5, false));
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("세트 메뉴"));
        final Menu menu = menuDao.save(new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup.getId()));
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2);
        final OrderCreateRequest order = new OrderCreateRequest(orderTable.getId(),
                Collections.singletonList(orderLineItemRequest));

        create("/api/orders", order)
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.orderTableId").value(1));
    }

    @DisplayName("전체 주문을 확인하는 테스트")
    @Test
    void listTest() throws Exception {
        final OrderTable orderTable = orderTableDao.save(TestFixture.getOrderTableWithNotEmpty());
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("세트 메뉴"));
        final Menu menu = menuDao.save(new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup.getId()));
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 2);
        orderDao.save(new Order(orderTable.getId(), "COOKING", Collections.singletonList(orderLineItem)));

        findList("/api/orders")
                .andExpect(jsonPath("$.[0].id").exists());
    }

    @DisplayName("changeOrderStatus: 주문 상태를 변경하는 테스트")
    @Test
    void changeOrderStatusTest() throws Exception {
        final OrderTable orderTable = orderTableDao.save(TestFixture.getOrderTableWithNotEmpty());
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("세트 메뉴"));
        final Menu menu = menuDao.save(new Menu("후라이드+후라이드", BigDecimal.valueOf(19000), menuGroup.getId()));
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 2);
        final Order order = orderDao.save(
                new Order(orderTable.getId(), "COOKING", Collections.singletonList(orderLineItem)));
        final OrderChangeStatusRequest orderChangeStatusRequest = new OrderChangeStatusRequest("MEAL");

        update("/api/orders/" + order.getId() + "/order-status", orderChangeStatusRequest)
                .andExpect(jsonPath("$.orderStatus").value("MEAL"));
    }
}
