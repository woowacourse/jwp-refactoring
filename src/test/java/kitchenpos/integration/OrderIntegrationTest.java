package kitchenpos.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.config.CustomParameterizedTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("Order 통합테스트")
class OrderIntegrationTest extends IntegrationTest {

    private static final String API_PATH = "/api/orders";

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("생성 - 성공")
    @Test
    void create_Success() throws Exception {
        // given
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setNumberOfGuests(0);
        savedOrderTable.setEmpty(false);
        savedOrderTable = orderTableDao.save(savedOrderTable);

        MenuGroup savedMenuGroup = new MenuGroup("추천메뉴");
        savedMenuGroup = menuGroupRepository.save(savedMenuGroup);

        Menu savedMenu = new Menu();
        savedMenu.setName("양념치킨");
        savedMenu.setPrice(BigDecimal.valueOf(17_000));
        savedMenu.setMenuGroupId(savedMenuGroup.getId());
        savedMenu = menuDao.save(savedMenu);

        final Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", savedOrderTable.getId());

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(1L);

        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        params.put("orderLineItems", orderLineItems);

        // when
        // then
        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
            .andExpect(status().isCreated())
            .andExpect(header().exists(LOCATION))
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.orderTableId").value(savedOrderTable.getId()))
            .andExpect(jsonPath("$.orderStatus").value(OrderStatus.COOKING.name()))
            .andExpect(jsonPath("$.orderedTime").isNotEmpty())
            .andExpect(jsonPath("$.orderLineItems.length()").value(orderLineItems.size()))
            .andExpect(jsonPath("$.orderLineItems[0].seq").isNumber())
            .andExpect(jsonPath("$.orderLineItems[0].orderId").isNumber())
            .andExpect(jsonPath("$.orderLineItems[0].menuId").value(savedMenu.getId()))
            .andExpect(jsonPath("$.orderLineItems[0].quantity").value(orderLineItem.getQuantity()))
        ;

        final List<Order> foundOrders = orderDao.findAll();
        assertThat(foundOrders).hasSize(1);

        final Order foundOrder = foundOrders.get(0);
        assertThat(foundOrder.getOrderTableId()).isEqualTo(savedOrderTable.getId());
        assertThat(foundOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(foundOrder.getOrderedTime()).isNotNull();

        final List<OrderLineItem> foundOrderLineItems = orderLineItemDao.findAllByOrderId(foundOrder.getId());
        assertThat(foundOrderLineItems).hasSize(1);

        final OrderLineItem foundOrderLineItem = foundOrderLineItems.get(0);
        assertThat(foundOrderLineItem.getOrderId()).isEqualTo(foundOrder.getId());
        assertThat(foundOrderLineItem.getMenuId()).isEqualTo(orderLineItem.getMenuId());
        assertThat(foundOrderLineItem.getQuantity()).isEqualTo(orderLineItem.getQuantity());
    }

    @DisplayName("생성 - 실패 - 요청 매개변수 Order의 OrderLineItems가 null일 때")
    @Test
    void create_Fail_When_RequestOrderLineItemsIsNull() {
        // given
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setNumberOfGuests(0);
        savedOrderTable.setEmpty(false);
        savedOrderTable = orderTableDao.save(savedOrderTable);

        final Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", savedOrderTable.getId());
        params.put("orderLineItems", null);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<Order> foundOrders = orderDao.findAll();
        assertThat(foundOrders).isEmpty();

        final List<OrderLineItem> foundOrderLineItems = orderLineItemDao.findAll();
        assertThat(foundOrderLineItems).isEmpty();
    }

    @DisplayName("생성 - 실패 - 요청 매개변수 Order의 OrderLineItems의 Menu들이 모두 DB에 존재하지 않을 때")
    @Test
    void create_Fail_When_RequestMenusInOrderLineItemsNotExistsInDB() {
        // given
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setNumberOfGuests(0);
        savedOrderTable.setEmpty(false);
        savedOrderTable = orderTableDao.save(savedOrderTable);

        final Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", savedOrderTable.getId());

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(0L);
        orderLineItem.setQuantity(1L);

        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        params.put("orderLineItems", orderLineItems);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<Order> foundOrders = orderDao.findAll();
        assertThat(foundOrders).isEmpty();

        final List<OrderLineItem> foundOrderLineItems = orderLineItemDao.findAll();
        assertThat(foundOrderLineItems).isEmpty();
    }

    @DisplayName("생성 - 실패 - 요청 매개변수 Order의 OrderTableId가 DB에 존재하지 않을 때")
    @Test
    void create_Fail_When_RequestOrderTableIdOfOrderNotExistsInDB() {
        // given
        MenuGroup savedMenuGroup = new MenuGroup("추천메뉴");
        savedMenuGroup = menuGroupRepository.save(savedMenuGroup);

        Menu savedMenu = new Menu();
        savedMenu.setName("양념치킨");
        savedMenu.setPrice(BigDecimal.valueOf(17_000));
        savedMenu.setMenuGroupId(savedMenuGroup.getId());
        savedMenu = menuDao.save(savedMenu);

        final Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", 0L);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(1L);

        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        params.put("orderLineItems", orderLineItems);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<Order> foundOrders = orderDao.findAll();
        assertThat(foundOrders).isEmpty();

        final List<OrderLineItem> foundOrderLineItems = orderLineItemDao.findAll();
        assertThat(foundOrderLineItems).isEmpty();
    }

    @DisplayName("생성 - 실패 - DB에서 조회한 OrderTable이 비어있을 때")
    @Test
    void create_Fail_When_OrderTableFromDBIsEmpty() {
        // given
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setNumberOfGuests(0);
        savedOrderTable.setEmpty(true);
        savedOrderTable = orderTableDao.save(savedOrderTable);

        MenuGroup savedMenuGroup = new MenuGroup("추천메뉴");
        savedMenuGroup = menuGroupRepository.save(savedMenuGroup);

        Menu savedMenu = new Menu();
        savedMenu.setName("양념치킨");
        savedMenu.setPrice(BigDecimal.valueOf(17_000));
        savedMenu.setMenuGroupId(savedMenuGroup.getId());
        savedMenu = menuDao.save(savedMenu);

        final Map<String, Object> params = new HashMap<>();
        params.put("orderTableId", savedOrderTable.getId());

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(1L);

        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);
        params.put("orderLineItems", orderLineItems);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<Order> foundOrders = orderDao.findAll();
        assertThat(foundOrders).isEmpty();

        final List<OrderLineItem> foundOrderLineItems = orderLineItemDao.findAll();
        assertThat(foundOrderLineItems).isEmpty();
    }

    @DisplayName("모든 Order들 조회 - 성공")
    @Test
    void findAll_Success() throws Exception {
        // given
        MenuGroup savedMenuGroup = new MenuGroup("추천메뉴");
        savedMenuGroup = menuGroupRepository.save(savedMenuGroup);

        Menu savedMenu = new Menu();
        savedMenu.setName("양념치킨");
        savedMenu.setPrice(BigDecimal.valueOf(17_000));
        savedMenu.setMenuGroupId(savedMenuGroup.getId());
        savedMenu = menuDao.save(savedMenu);

        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setNumberOfGuests(1);
        savedOrderTable.setEmpty(false);
        savedOrderTable = orderTableDao.save(savedOrderTable);

        Order savedOrder = new Order();
        savedOrder.setOrderTableId(savedOrderTable.getId());
        savedOrder.setOrderStatus(OrderStatus.COOKING.name());
        savedOrder.setOrderedTime(LocalDateTime.now());
        savedOrder = orderDao.save(savedOrder);

        OrderLineItem savedOrderLineItem = new OrderLineItem();
        savedOrderLineItem.setOrderId(savedOrder.getId());
        savedOrderLineItem.setMenuId(savedMenu.getId());
        savedOrderLineItem.setQuantity(1L);
        savedOrderLineItem = orderLineItemDao.save(savedOrderLineItem);

        // when
        // then
        mockMvc.perform(get(API_PATH))
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").isNumber())
            .andExpect(jsonPath("$[0].orderTableId").value(savedOrderTable.getId()))
            .andExpect(jsonPath("$[0].orderStatus").value(savedOrder.getOrderStatus()))
            .andExpect(jsonPath("$[0].orderLineItems.length()").value(1))
            .andExpect(jsonPath("$[0].orderLineItems[0].seq").isNumber())
            .andExpect(jsonPath("$[0].orderLineItems[0].orderId").value(savedOrder.getId()))
            .andExpect(jsonPath("$[0].orderLineItems[0].menuId").value(savedMenu.getId()))
            .andExpect(jsonPath("$[0].orderLineItems[0].quantity").value(savedOrderLineItem.getQuantity()))
        ;
    }

    @DisplayName("Order의 OrderStatus 변경 - 성공")
    @CustomParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COMPLETION"})
    void changeOrderStatus_Success(OrderStatus newOrderStatus) throws Exception {
        // given
        MenuGroup savedMenuGroup = new MenuGroup("추천메뉴");
        savedMenuGroup = menuGroupRepository.save(savedMenuGroup);

        Menu savedMenu = new Menu();
        savedMenu.setName("양념치킨");
        savedMenu.setPrice(BigDecimal.valueOf(17_000));
        savedMenu.setMenuGroupId(savedMenuGroup.getId());
        savedMenu = menuDao.save(savedMenu);

        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setNumberOfGuests(1);
        savedOrderTable.setEmpty(false);
        savedOrderTable = orderTableDao.save(savedOrderTable);

        Order savedOrder = new Order();
        savedOrder.setOrderTableId(savedOrderTable.getId());
        savedOrder.setOrderStatus(OrderStatus.COOKING.name());
        savedOrder.setOrderedTime(LocalDateTime.now());
        savedOrder = orderDao.save(savedOrder);

        OrderLineItem savedOrderLineItem = new OrderLineItem();
        savedOrderLineItem.setOrderId(savedOrder.getId());
        savedOrderLineItem.setMenuId(savedMenu.getId());
        savedOrderLineItem.setQuantity(1L);
        savedOrderLineItem = orderLineItemDao.save(savedOrderLineItem);

        final Map<String, Object> params = new HashMap<>();
        params.put("orderStatus", newOrderStatus.name());

        // when
        // then
        mockMvc.perform(put(API_PATH + "/" + savedOrder.getId() + "/order-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.orderTableId").value(savedOrderTable.getId()))
            .andExpect(jsonPath("$.orderStatus").value(newOrderStatus.name()))
            .andExpect(jsonPath("$.orderLineItems.length()").value(1))
            .andExpect(jsonPath("$.orderLineItems[0].seq").isNumber())
            .andExpect(jsonPath("$.orderLineItems[0].orderId").value(savedOrder.getId()))
            .andExpect(jsonPath("$.orderLineItems[0].menuId").value(savedMenu.getId()))
            .andExpect(jsonPath("$.orderLineItems[0].quantity").value(savedOrderLineItem.getQuantity()))
        ;

        final Order foundOrder = orderDao.findById(savedOrder.getId())
            .orElseThrow(() -> new NotFoundException("해당 id의 Order이 존재하지 않습니다."));
        assertThat(foundOrder.getOrderStatus()).isEqualTo(newOrderStatus.name());
    }

    @DisplayName("Order의 OrderStatus 변경 - 실패 - Order이 DB에 존재하지 않을 때")
    @CustomParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"MEAL", "COMPLETION"})
    void changeOrderStatus_Fail_When_OrderNotExistsInDB(OrderStatus newOrderStatus) {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("orderStatus", newOrderStatus.name());

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(put(API_PATH + "/0/order-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }
}
