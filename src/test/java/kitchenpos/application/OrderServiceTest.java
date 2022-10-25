package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderService orderService;

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void create() {
        Product product = productDao.save(new Product("치킨", BigDecimal.valueOf(10000)));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("1번 메뉴 그룹"));
        Menu menu = menuDao.save(new Menu("1번 메뉴", BigDecimal.valueOf(10000), menuGroup.getId(),
                createMenuProducts(product.getId())));
        OrderTable orderTable = orderTableDao.save(new OrderTable(0, false));

        Order order = orderService.create(new Order(orderTable.getId(), createOrderLineItem(menu.getId())));

        assertThat(order).isNotNull();
    }

    @DisplayName("주문 등록 시 주문 항목이 비어있으면 예외가 발생한다.")
    @Test
    void createWithEmptyOrderLineItem() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(0, false));

        assertThatThrownBy(() -> orderService.create(new Order(orderTable.getId(), new ArrayList<>())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 주문항목의 메뉴에 등록되어 있지 않은 주문 항목이 있으면 예외가 발생한다.")
    @Test
    void createWithInvalidOrderLineItem() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(0, false));

        assertThatThrownBy(() -> orderService.create(new Order(orderTable.getId(), createInvalidOrderLineItem())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 주문에서의 주문 테이블이 존재하지 않는 주문 테이블일 경우 예외가 발생한다.")
    @Test
    void createWithInvalidOrderTable() {
        assertThatThrownBy(() -> orderService.create(new Order(9999L, createOrderLineItem())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시 주문에서의 주문 테이블이 비어있으면 예외가 발생한다.")
    @Test
    void createWithEmptyOrderTable() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(0, true));

        assertThatThrownBy(() -> orderService.create(new Order(orderTable.getId(), createOrderLineItem())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문들을 조회할 수 있다.")
    @Test
    void list() {
        Product product = productDao.save(new Product("치킨", BigDecimal.valueOf(10000)));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("1번 메뉴 그룹"));
        Menu menu = menuDao.save(new Menu("1번 메뉴", BigDecimal.valueOf(10000), menuGroup.getId(),
                createMenuProducts(product.getId())));
        OrderTable orderTable = orderTableDao.save(new OrderTable(0, false));
        orderService.create(new Order(orderTable.getId(), createOrderLineItem(menu.getId())));

        List<Order> orders = orderService.list();

        assertThat(orders.size()).isEqualTo(1L);
    }

    @DisplayName("주문의 상태를 수정할 수 있다.")
    @Test
    void changeOrderStatus() {
        Product product = productDao.save(new Product("치킨", BigDecimal.valueOf(10000)));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("1번 메뉴 그룹"));
        Menu menu = menuDao.save(new Menu("1번 메뉴", BigDecimal.valueOf(10000), menuGroup.getId(),
                createMenuProducts(product.getId())));
        OrderTable orderTable = orderTableDao.save(new OrderTable(0, false));
        Order order = orderDao.save(
                new Order(orderTable.getId(), "COOKING", LocalDateTime.now(), createOrderLineItem(menu.getId())));
        Order changeOrder = createOrderWithMealStatus("MEAL");

        orderService.changeOrderStatus(order.getId(), changeOrder);
        Order foundOrder = orderDao.findById(order.getId()).get();

        assertThat(foundOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 수정 시 존재하지 않는 주문일 경우 예외가 발생한다.")
    @Test
    void changeOrderStatusWithInvalidOrder() {
        Order changeOrder = createOrderWithMealStatus("MEAL");

        assertThatThrownBy(() -> orderService.changeOrderStatus(9999L, changeOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 수정 시 주문 상태가 계산 완료인 경우 예외가 발생한다.")
    @Test
    void changeOrderStatusWithCompletionOrderStatus() {
        Product product = productDao.save(new Product("치킨", BigDecimal.valueOf(10000)));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("1번 메뉴 그룹"));
        Menu menu = menuDao.save(new Menu("1번 메뉴", BigDecimal.valueOf(10000), menuGroup.getId(),
                createMenuProducts(product.getId())));
        OrderTable orderTable = orderTableDao.save(new OrderTable(0, false));
        Order order = orderDao.save(
                new Order(orderTable.getId(), "COOKING", LocalDateTime.now(), createOrderLineItem(menu.getId())));
        Order changeOrder = createOrderWithMealStatus("COMPLETION");

        orderService.changeOrderStatus(order.getId(), changeOrder);

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private List<OrderLineItem> createInvalidOrderLineItem() {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(new OrderLineItem(9999L, 10));
        return orderLineItems;
    }

    private List<OrderLineItem> createOrderLineItem(Long... menuIds) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (Long menuId : menuIds) {
            orderLineItems.add(new OrderLineItem(menuId, 10));
        }
        return orderLineItems;
    }

    private List<MenuProduct> createMenuProducts(Long... productIds) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (Long productId : productIds) {
            menuProducts.add(new MenuProduct(productId, 1L));
        }
        return menuProducts;
    }

    private Order createOrderWithMealStatus(String orderStatus) {
        Order changeOrder = new Order();
        changeOrder.setOrderStatus(orderStatus);
        return changeOrder;
    }
}
