package kitchenpos.application;

import static kitchenpos.data.TestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderLineItemDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateProductDao;
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

@SpringBootTest(classes = {
        OrderService.class,
        JdbcTemplateMenuDao.class,
        JdbcTemplateOrderTableDao.class,
        JdbcTemplateOrderDao.class,
        JdbcTemplateOrderLineItemDao.class,
        JdbcTemplateProductDao.class,
        JdbcTemplateMenuDao.class,
        JdbcTemplateMenuGroupDao.class,
        JdbcTemplateMenuProductDao.class
})
@Transactional
class OrderServiceTest extends ServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @DisplayName("list: 전체 주문 목록을 조회한다.")
    @Test
    void list() {
        OrderTable nonEmptyTable = orderTableDao.save(createTable(null, 5, false));
        Product product = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(15_000)));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup("단품 그룹"));
        Menu menu = menuDao.save(createMenu("치킨 세트", BigDecimal.valueOf(15_000), menuGroup.getId(), null));
        menuProductDao.save(createMenuProduct(menu.getId(), product.getId(), 1));

        OrderLineItem firstOrderLineItem = createOrderLineItem(null, menu.getId(), 1);
        Order order = createOrder(nonEmptyTable.getId(), null, OrderStatus.COOKING, Lists.list(firstOrderLineItem));
        orderService.create(order);

        final List<Order> list = orderService.list();

        assertThat(list).hasSize(1);
    }

    @DisplayName("create: 점유중인 테이블에서 메뉴 중복이 없는 하나 이상의 상품 주문시, 주문 추가 후, 생성된 주문 객체를 반환한다.")
    @Test
    void create() {
        OrderTable nonEmptyTable = orderTableDao.save(createTable(null, 5, false));
        Product product = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(15_000)));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup("단품 그룹"));
        Menu menu = menuDao.save(createMenu("치킨 세트", BigDecimal.valueOf(15_000), menuGroup.getId(), null));
        menuProductDao.save(createMenuProduct(menu.getId(), product.getId(), 1));

        OrderLineItem firstOrderLineItem = createOrderLineItem(null, menu.getId(), 1);
        Order order = createOrder(nonEmptyTable.getId(), null, OrderStatus.COOKING, Lists.list(firstOrderLineItem));
        Order newOrder = orderService.create(order);

        assertAll(
                () -> assertThat(newOrder.getId()).isNotNull(),
                () -> assertThat(newOrder.getOrderedTime()).isNotNull(),
                () -> assertThat(newOrder.getOrderStatus()).isEqualTo("COOKING"),
                () -> assertThat(newOrder.getOrderTableId()).isEqualTo(nonEmptyTable.getId()),
                () -> assertThat(newOrder.getOrderLineItems()).hasSize(1)
        );
    }

    @DisplayName("create: 점유중인 테이블에서 주문 상품이 없는 경우, 주문 추가 실패 후, IllegalArgumentException 발생")
    @Test
    void create_fail_if_order_contains_no_order_line_item() {
        OrderTable nonEmptyTable = orderTableDao.save(createTable(null, 5, false));
        Order orderNotContainsAnyProduct = createOrder(nonEmptyTable.getId(), null, OrderStatus.COOKING, Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(orderNotContainsAnyProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 점유중이 아닌 테이블에서 주문 요청시, 주문 추가 실패 후, IllegalArgumentException 발생")
    @Test
    void create_fail_if_table_is_empty() {
        OrderTable emptyTable = orderTableDao.save(createTable(null, 0, true));
        Product product = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(15_000)));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup("단품 그룹"));
        Menu menu = menuDao.save(createMenu("치킨 세트", BigDecimal.valueOf(15_000), menuGroup.getId(), null));
        menuProductDao.save(createMenuProduct(menu.getId(), product.getId(), 1));

        OrderLineItem firstOrderLineItem = createOrderLineItem(null, menu.getId(), 1);
        Order newOrder = createOrder(emptyTable.getId(), null, OrderStatus.COOKING, Lists.list(firstOrderLineItem));

        assertThatThrownBy(() -> orderService.create(newOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 점유중인 테이블에서 중복된 중복 메뉴를 포함한 상품 들 주문 요청시, 주문 추가 실패 후, IllegalArgumentException 발생")
    @Test
    void create_fail_if_order_line_item_contains_duplicate_menu() {
        OrderTable notEmptyTable = orderTableDao.save(createTable(null, 5, false));
        Product product = productDao.save(createProduct("후라이드 치킨", BigDecimal.valueOf(15_000)));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup("단품 그룹"));
        Menu menu = menuDao.save(createMenu("치킨 세트", BigDecimal.valueOf(15_000), menuGroup.getId(), null));
        menuProductDao.save(createMenuProduct(menu.getId(), product.getId(), 1));

        OrderLineItem firstOrderLineItem = createOrderLineItem(null, menu.getId(), 1);
        OrderLineItem secondOrderLineItem = createOrderLineItem(null, menu.getId(), 2);
        Order newOrder = createOrder(notEmptyTable.getId(), null, OrderStatus.COOKING, Lists.list(firstOrderLineItem, secondOrderLineItem));

        assertThatThrownBy(() -> orderService.create(newOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeOrderStatus: 완료 되지 않는 주문의 경우, 주문 상태의 변경 요청시, 상태 변경 후, 변경된 주문 객체를 반환한다.")
    @Test
    void changeOrderStatus() {
        OrderTable notEmptyTable = orderTableDao.save(createTable(null, 5, false));
        Order nonCompletedOrder = orderDao.save(
                createOrder(notEmptyTable.getId(), LocalDateTime.of(2020, 10, 10, 20, 40), OrderStatus.COOKING, null));

        Order order = createOrder(null, null, OrderStatus.MEAL, null);
        Long savedOrderId = nonCompletedOrder.getId();
        Order updatedOrder = orderService.changeOrderStatus(savedOrderId, order);

        assertAll(
                () -> assertThat(updatedOrder.getId()).isEqualTo(savedOrderId),
                () -> assertThat(updatedOrder.getOrderedTime()).isNotNull(),
                () -> assertThat(updatedOrder.getOrderStatus()).isEqualTo("MEAL"),
                () -> assertThat(updatedOrder.getOrderTableId()).isEqualTo(notEmptyTable.getId())
        );
    }

    @DisplayName("changeOrderStatus: 이미 완료한 주문의 상태의 변경 요청시, 상태 변경 실패 후, IllegalArgumentException 발생.")
    @Test
    void changeOrderStatus_fail_if_order_status_is_completion() {
        OrderTable notEmptyTable = orderTableDao.save(createTable(null, 5, false));
        Order completedOrder = orderDao.save(
                createOrder(notEmptyTable.getId(), LocalDateTime.of(2020, 10, 10, 20, 40), OrderStatus.COMPLETION, null));
        Long savedOrderId = completedOrder.getId();
        Order nonCompletedOrder = createOrder(null, null, OrderStatus.MEAL, null);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrderId, nonCompletedOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}