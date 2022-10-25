package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
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
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;

    @BeforeEach
    void setUp() {
        Product product = productDao.save(new Product("상품1", new BigDecimal(2500)));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹1"));
        Menu menu1 = menuDao.save(new Menu("메뉴1", new BigDecimal(5000), menuGroup.getId(),
                List.of(new MenuProduct(product.getId(), 1L))));
        Menu menu2 = menuDao.save(new Menu("메뉴2", new BigDecimal(4500), menuGroup.getId(),
                List.of(new MenuProduct(product.getId(), 1L))));
        orderLineItem1 = new OrderLineItem(menu1.getId(), 2);
        orderLineItem2 = new OrderLineItem(menu2.getId(), 1);
    }

    @DisplayName("Order를 등록할 수 있다.")
    @Test
    void create() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 3, false));
        Order order = new Order(orderTable.getId(), "COOKING", List.of(orderLineItem1, orderLineItem2));

        orderService.create(order);

        assertThat(orderService.list()).hasSize(1);
    }

    @DisplayName("Menu 없이 Order를 등록하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_EmptyMenu() {
        OrderTable emptyOrderTable = orderTableDao.save(new OrderTable(null, 3, true));
        Order order = new Order(emptyOrderTable.getId(), "COOKING", Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블에 주문을 등록할 수 없습니다.");
    }

    @DisplayName("존재하지 않는 Menu로 Order를 등록하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_NotFoundMenu() {
        OrderLineItem notFoundOrderLineItem = new OrderLineItem(1000L, 2);
        OrderTable emptyOrderTable = orderTableDao.save(new OrderTable(null, 3, true));
        Order order = new Order(emptyOrderTable.getId(), "COOKING", List.of(notFoundOrderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 메뉴로 주문을 등록할 수 없습니다.");
    }

    @DisplayName("empty인 OrderTable에 해당하는 Order를 등록하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_EmptyOrderTable() {
        OrderTable emptyOrderTable = orderTableDao.save(new OrderTable(null, 3, true));
        Order order = new Order(emptyOrderTable.getId(), "COOKING", List.of(orderLineItem1, orderLineItem2));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블에 주문을 등록할 수 없습니다.");
    }

    @DisplayName("주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 3, false));
        Order order = orderService.create(
                new Order(orderTable.getId(), "COOKING", List.of(orderLineItem1, orderLineItem2)));

        orderService.changeOrderStatus(
                order.getId(), new Order(orderTable.getId(), "MEAL", Collections.emptyList()));

        Order changedOrder = orderService.list().get(0);
        assertThat(changedOrder.getOrderStatus()).isEqualTo("MEAL");
    }

    @DisplayName("주문 상태가 COMPLETION인 주문의 상태를 변경하려고 하면 예외를 발생시킨다.")
    @Test
    void changeOrderStatus_Exception_AlreadyCompletionOrder() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 3, false));
        Order order = orderDao.save(
                new Order(orderTable.getId(), "COMPLETION", List.of(orderLineItem1, orderLineItem2)));

        assertThatThrownBy(() -> orderService.changeOrderStatus(
                order.getId(), new Order(orderTable.getId(), "MEAL", Collections.emptyList())))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 완료된 주문의 상태는 변경할 수 없습니다.");
    }
}