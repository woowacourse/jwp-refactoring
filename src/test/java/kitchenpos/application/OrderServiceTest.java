package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.*;
import kitchenpos.utils.KitchenPosClassCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.application.TableServiceTest.테이블_사람_4명;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@Sql("/truncate.sql")
@SpringBootTest
public class OrderServiceTest {
    private static final long 메뉴_잘못된_ID = -1L;
    private static final long 주문_테이블_잘못된_ID = -1L;
    private static final long 주문_잘못된_ID = -1L;
    private static final String 두마리_세트 = "두마리 세트";
    private static final String 메뉴_후라이드_치킨 = "후라이트 치킨";
    private static final long 메뉴_1개 = 1L;
    private static final BigDecimal 메뉴_16000원 = new BigDecimal("16000.00");
    private static final BigDecimal 가격_15000원 = new BigDecimal("15000.00");
    private static final String 상태_조리중 = OrderStatus.COOKING.name();
    private static final String 상태_완료 = OrderStatus.COMPLETION.name();
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
        Product product = KitchenPosClassCreator.createProduct(메뉴_후라이드_치킨, 가격_15000원);
        product = productDao.save(product);
        MenuGroup menuGroup = KitchenPosClassCreator.createMenuGroup(두마리_세트);
        menuGroup = menuGroupDao.save(menuGroup);
        MenuProduct menuProduct = KitchenPosClassCreator.createMenuProduct(product, 메뉴_1개);
        List<MenuProduct> menuProducts = Arrays.asList(menuProduct);

        menu = KitchenPosClassCreator.createMenu(메뉴_후라이드_치킨, menuGroup, 메뉴_16000원, menuProducts);
        menu = menuDao.save(menu);

        orderTable = KitchenPosClassCreator.createOrderTable(테이블_사람_4명, false);
        orderTable = orderTableDao.save(orderTable);
        OrderLineItem orderLineItem = KitchenPosClassCreator.createOrderLineItem(menu, 메뉴_1개);
        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

        order = KitchenPosClassCreator.createOrder(orderTable, orderLineItems, 상태_조리중);
    }

    @DisplayName("Order 생성이 올바르게 수행된다.")
    @Test
    void createTest() {
        Order savedOrder = orderService.create(order);

        assertEquals(savedOrder.getOrderTableId(), order.getOrderTableId());
        assertEquals(상태_조리중, savedOrder.getOrderStatus());
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
        OrderLineItem invalidOrderLineItem = KitchenPosClassCreator.createOrderLineItem(menu, 메뉴_1개);
        invalidOrderLineItem.setMenuId(메뉴_잘못된_ID);
        List<OrderLineItem> orderLineItems = Arrays.asList(invalidOrderLineItem);
        order.setOrderLineItems(orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : Order 생성 중 잚못된 OrderTable을 보내면, 예외가 발생한다.")
    @Test
    void createWithInvalidOrderTableItemsExceptionTest() {
        order.setOrderTableId(주문_테이블_잘못된_ID);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : Order 생성 중 비어있는 OrderTable을 보내면, 예외가 발생한다.")
    @Test
    void createWithEmptyOrderTableItemsExceptionTest() {
        OrderTable emptyOrderTable = KitchenPosClassCreator
                .createOrderTable(테이블_사람_4명, true);
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
        Order statusOrder = KitchenPosClassCreator.createOrder(orderTable, Collections.emptyList(), 상태_완료);

        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), statusOrder);

        assertEquals(상태_완료, changedOrder.getOrderStatus());
        assertEquals(changedOrder.getId(), savedOrder.getId());
        assertEquals(changedOrder.getOrderedTime(), savedOrder.getOrderedTime());
        assertEquals(changedOrder.getOrderTableId(), savedOrder.getOrderTableId());
    }

    @DisplayName("예외 테스트 : 잘못된 Order의 ID를 전달 시, 예외가 발생한다.")
    @Test
    void changeOrderStatusInvalidIdExceptionTest() {
        Order statusOrder = KitchenPosClassCreator.createOrder(orderTable, Collections.emptyList(), 상태_완료);

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문_잘못된_ID, statusOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트 : 완료된 Order의 상태를 변경 시, 예외가 발생한다.")
    @Test
    void changeOrderStatusCompletionExceptionTest() {
        Order savedOrder = orderService.create(order);
        Order statusOrder = KitchenPosClassCreator.createOrder(orderTable, Collections.emptyList(), 상태_완료);

        assertThatThrownBy(() -> {
            Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), statusOrder);
            orderService.changeOrderStatus(changedOrder.getId(), statusOrder);
        })
                .isInstanceOf(IllegalArgumentException.class);
    }
}
