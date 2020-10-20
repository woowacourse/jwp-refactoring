package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.*;
import kitchenpos.utils.KitchenposClassCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.application.MenuServiceTest.*;
import static kitchenpos.application.ProductServiceTest.TEST_PRODUCT_NAME_1;
import static kitchenpos.application.ProductServiceTest.TEST_PRODUCT_PRICE_1;
import static kitchenpos.application.TableServiceTest.TEST_ORDER_TABLE_NUMBER_OF_GUESTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@Sql("/truncate.sql")
@SpringBootTest
public class OrderServiceTest {
    public static final long INVALID_MENU_ID = 10000L;
    public static final long INVALID_ORDER_TABLE_ID = 10000L;
    public static final long INVALID_ORDER_ID = 10000L;
    private static final String TEST_ORDER_STATUS_COOKING = OrderStatus.COOKING.name();
    private static final String TEST_ORDER_STATUS_COMPLETION = OrderStatus.COMPLETION.name();
    OrderTable orderTable;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private OrderService orderService;
    private Order order;
    private Menu menu;

    @BeforeEach
    void setUp() {
        Product product = KitchenposClassCreator.createProduct(TEST_PRODUCT_NAME_1, TEST_PRODUCT_PRICE_1);
        product = productDao.save(product);
        MenuGroup menuGroup = KitchenposClassCreator.createMenuGroup(TEST_MENU_GROUP_NAME);
        menuGroup = menuGroupDao.save(menuGroup);
        MenuProduct menuProduct = KitchenposClassCreator.createMenuProduct(product, TEST_MENU_PRODUCT_QUANTITY);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct);

        menu = KitchenposClassCreator.createMenu(TEST_MENU_NAME, menuGroup, TEST_MENU_PRICE, menuProducts);
        menu = menuDao.save(menu);

        orderTable = KitchenposClassCreator.createOrderTable(TEST_ORDER_TABLE_NUMBER_OF_GUESTS, false);
        orderTable = orderTableDao.save(orderTable);
        OrderLineItem orderLineItem = KitchenposClassCreator.createOrderLineItem(menu, TEST_MENU_PRODUCT_QUANTITY);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        order = KitchenposClassCreator.createOrder(orderTable, orderLineItems, TEST_ORDER_STATUS_COOKING);
    }

    @DisplayName("Order 생성이 올바르게 수행된다.")
    @Test
    void createTest() {
        Order savedOrder = orderService.create(order);

        assertEquals(savedOrder.getOrderTableId(), order.getOrderTableId());
        assertEquals(savedOrder.getOrderStatus(), TEST_ORDER_STATUS_COOKING);
        assertThat(savedOrder.getOrderLineItems())
                .hasSize(1)
                .extracting("menuId")
                .containsOnly(menu.getId());
    }

    @DisplayName("예외 테스트 : Order 생성 중 빈 OrderLineItems를 보내면, 예외가 발생한다.")
    @Test
    void createWithEmptyOrderLineItemsExceptionTest() {
        order.setOrderLineItems(Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : Order 생성 중 잘못된 OrderLineItems를 보내면, 예외가 발생한다.")
    @Test
    void createWithInvalidOrderLineItemsExceptionTest() {
        OrderLineItem invalidOrderLineItem = KitchenposClassCreator.createOrderLineItem(menu, TEST_MENU_PRODUCT_QUANTITY);
        invalidOrderLineItem.setMenuId(INVALID_MENU_ID);
        List<OrderLineItem> orderLineItems = Arrays.asList(invalidOrderLineItem);
        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : Order 생성 중 잚못된 OrderTable을 보내면, 예외가 발생한다.")
    @Test
    void createWithInvalidOrderTableItemsExceptionTest() {
        order.setOrderTableId(INVALID_ORDER_TABLE_ID);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : Order 생성 중 비어있는 OrderTable을 보내면, 예외가 발생한다.")
    @Test
    void createWithEmptyOrderTableItemsExceptionTest() {
        OrderTable emptyOrderTable = KitchenposClassCreator
                .createOrderTable(TEST_ORDER_TABLE_NUMBER_OF_GUESTS, true);
        emptyOrderTable = orderTableDao.save(emptyOrderTable);
        order.setOrderTableId(emptyOrderTable.getId());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order 전체 목록을 요청 시 올바른 값이 반환된다.")
    @Test
    void listTest() {
        Order savedOrder = orderService.create(order);

        List<Order> foundOrders = orderService.list();
        Order foundOrder = foundOrders.get(0);

        assertThat(foundOrders).hasSize(1);
        assertEquals(foundOrder.getId(), savedOrder.getId());
        assertEquals(foundOrder.getOrderStatus(), savedOrder.getOrderStatus());
        assertEquals(foundOrder.getOrderedTime(), savedOrder.getOrderedTime());
        assertEquals(foundOrder.getOrderTableId(), savedOrder.getOrderTableId());
    }

    @DisplayName("Order의 상태를 변경 시 올바르게 변경된다.")
    @Test
    void changeOrderStatusTest() {
        Order savedOrder = orderService.create(order);
        Order statusOrder = KitchenposClassCreator.createOrder(orderTable, Collections.emptyList(), TEST_ORDER_STATUS_COMPLETION);

        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), statusOrder);

        assertEquals(changedOrder.getOrderStatus(), TEST_ORDER_STATUS_COMPLETION);
        assertEquals(changedOrder.getId(), savedOrder.getId());
        assertEquals(changedOrder.getOrderedTime(), savedOrder.getOrderedTime());
        assertEquals(changedOrder.getOrderTableId(), savedOrder.getOrderTableId());
    }

    @DisplayName("예외 테스트 : 잘못된 Order의 ID를 전달 시, 예외가 발생한다.")
    @Test
    void changeOrderStatusInvalidIdExceptionTest() {
        Order statusOrder = KitchenposClassCreator.createOrder(orderTable, Collections.emptyList(), TEST_ORDER_STATUS_COMPLETION);

        assertThatThrownBy(() -> orderService.changeOrderStatus(INVALID_ORDER_ID, statusOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : 완료된 Order의 상태를 변경 시, 예외가 발생한다.")
    @Test
    void changeOrderStatusCompletionExceptionTest() {
        Order savedOrder = orderService.create(order);
        Order statusOrder = KitchenposClassCreator.createOrder(orderTable, Collections.emptyList(), TEST_ORDER_STATUS_COMPLETION);

        assertThatThrownBy(() -> {
            Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), statusOrder);
            orderService.changeOrderStatus(changedOrder.getId(), statusOrder);
        })
                .isInstanceOf(IllegalArgumentException.class);
    }
}
