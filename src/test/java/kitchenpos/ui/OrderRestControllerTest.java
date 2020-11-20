package kitchenpos.ui;

import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.OrderFixture.createOrder;
import static kitchenpos.fixture.OrderFixture.createOrderRequest;
import static kitchenpos.fixture.OrderFixture.createOrderRequestChangeOrderStatus;
import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItemRequest;
import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.OrderService;
import kitchenpos.application.dto.OrderChangeOrderStatusRequest;
import kitchenpos.application.dto.OrderCreateRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class OrderRestControllerTest extends AbstractControllerTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderDao orderDao;


    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void create() throws Exception {
        OrderTable orderTable = orderTableDao.save(createOrderTable(null, false, 0, null));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "메뉴그룹"));
        Menu menu = menuDao.save(createMenu(null, "메뉴", 0L, menuGroup.getId()));
        OrderCreateRequest orderRequest = createOrderRequest(
            orderTable.getId(),
            Collections.singletonList(createOrderLineItemRequest(menu.getId(), 1))
        );

        mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(orderRequest))
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.orderTableId").value(orderRequest.getOrderTableId()))
            .andExpect(jsonPath("$.orderStatus").value(OrderStatus.COOKING.name()))
            .andExpect(jsonPath("$.orderedTime").exists())
            .andExpect(
                jsonPath("$.orderLineItems", hasSize(orderRequest.getOrderLineItems().size())));
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        List<Order> orders = orderDao.findAll();

        String json = mockMvc.perform(get("/api/orders"))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        List<Order> response = objectMapper.readValue(json,
            objectMapper.getTypeFactory().constructCollectionType(List.class, Order.class));

        assertThat(response).usingFieldByFieldElementComparator().containsAll(orders);
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() throws Exception {
        OrderTable orderTable = orderTableDao
            .save(orderTableDao.save(createOrderTable(null, false, 0, null)));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup(null, "메뉴그룹"));
        Menu menu = menuDao.save(createMenu(null, "메뉴", 0L, menuGroup.getId()));
        Order order = orderDao
            .save(createOrder(null, LocalDateTime.now(), OrderStatus.COOKING, orderTable.getId()));
        OrderChangeOrderStatusRequest orderRequest = createOrderRequestChangeOrderStatus(
            OrderStatus.MEAL);

        mockMvc.perform(put("/api/orders/{orderId}/order-status", order.getId())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(orderRequest))
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(order.getId()))
            .andExpect(jsonPath("$.orderTableId").value(order.getOrderTableId()))
            .andExpect(jsonPath("$.orderStatus").value(orderRequest.getOrderStatus().name()))
            .andExpect(jsonPath("$.orderedTime").value(order.getOrderedTime().toString()))
            .andExpect(jsonPath("$.orderLineItems").isEmpty());
    }
}
