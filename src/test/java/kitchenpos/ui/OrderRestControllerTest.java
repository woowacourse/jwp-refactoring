package kitchenpos.ui;

import static kitchenpos.data.TestData.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

class OrderRestControllerTest extends ControllerTest {
    @Autowired
    OrderService orderService;

    @Autowired
    OrderTableDao orderTableDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    MenuDao menuDao;

    @Autowired
    MenuProductDao menuProductDao;

    @Autowired
    OrderDao orderDao;

    @DisplayName("create: 테이블, 주문 라인 목록과 함께 주문 추가 요청을 한다. 새 주문 생성 성공 후 201 응답을 반환한다.")
    @Test
    void create() throws Exception {
        OrderTable nonEmptyTable = orderTableDao.save(createTable(null, 5, false));
        Product product = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(15_000)));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup("단품 그룹"));
        Menu menu = menuDao.save(createMenu("치킨 세트", BigDecimal.valueOf(15_000), menuGroup.getId(), null));
        menuProductDao.save(createMenuProduct(menu.getId(), product.getId(), 1));

        OrderLineItem firstOrderLineItem = createOrderLineItem(null, menu.getId(), 1);
        Order order = createOrder(nonEmptyTable.getId(), null, OrderStatus.COOKING, Lists.list(firstOrderLineItem));

        final String createOrderApiUrl = "/api/orders";
        final ResultActions resultActions = create(createOrderApiUrl, order);

        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.orderTableId", notNullValue()))
                .andExpect(jsonPath("$.orderStatus", is("COOKING")))
                .andExpect(jsonPath("$.orderedTime", notNullValue()))
                .andExpect(jsonPath("$.orderLineItems", hasSize(1)));
    }

    @DisplayName("list: 전체 주문 목록 조회 요청시, 200 상태 코드와 함께, 전체 주문 내역을 반환한다.")
    @Test
    void list() throws Exception {
        OrderTable nonEmptyTable = orderTableDao.save(createTable(null, 5, false));
        Product product = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(15_000)));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup("단품 그룹"));
        Menu menu = menuDao.save(createMenu("치킨 세트", BigDecimal.valueOf(15_000), menuGroup.getId(), null));
        menuProductDao.save(createMenuProduct(menu.getId(), product.getId(), 1));

        OrderLineItem firstOrderLineItem = createOrderLineItem(null, menu.getId(), 1);
        Order order = createOrder(nonEmptyTable.getId(), null, OrderStatus.COOKING, Lists.list(firstOrderLineItem));
        orderService.create(order);

        final String findOrdersApiUrl = "/api/orders";
        final ResultActions resultActions = findList(findOrdersApiUrl);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @DisplayName("changeOrderStatus: 요리 완료 상태가 아닌 경우, 주문 현재 진행 상태 변경 요청시 변경 후, 200 상태코드와, 변경한 주문 내용을 반환한다.")
    @Test
    void changeOrderStatus() throws Exception {
        OrderTable notEmptyTable = orderTableDao.save(createTable(null, 5, false));
        Order nonCompletedOrder = orderDao.save(
                createOrder(notEmptyTable.getId(), LocalDateTime.of(2020, 10, 10, 20, 40), OrderStatus.COOKING, null));

        Order newOrder = createOrder(null, null, OrderStatus.MEAL, null);
        Long savedOrderId = nonCompletedOrder.getId();

        String updateOrderStatusApiUrl = "/api/orders/{orderId}/order-status";
        final ResultActions resultActions = updateByPathIdAndBody(updateOrderStatusApiUrl, savedOrderId, newOrder);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.orderTableId", notNullValue()))
                .andExpect(jsonPath("$.orderStatus", is("MEAL")))
                .andExpect(jsonPath("$.orderedTime", notNullValue()));

    }
}