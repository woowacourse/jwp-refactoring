package kitchenpos.application;

import com.sun.tools.javac.util.List;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql({"/h2-truncate.sql"})
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private Menu savedMenu;
    private MenuProduct savedMenuProduct;
    private Product savedProduct;
    private MenuGroup savedMenuGroup;

    @BeforeEach
    void setup() {
        Product product = new Product.Builder()
                .name("치킨")
                .price(BigDecimal.valueOf(10000L))
                .build();

        savedProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(2);
        menuProduct.setProductId(savedProduct.getId());

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("즐겨찾는 음식");
        savedMenuGroup = menuGroupDao.save(menuGroup);

        Menu menu = new Menu();
        menu.setName("두마리치킨");
        menu.setPrice(BigDecimal.valueOf(20000));
        menu.setMenuProducts(List.of(savedMenuProduct));
        menu.setMenuGroupId(savedMenuGroup.getId());
        savedMenu = menuDao.save(menu);
    }

    @Test
    @DisplayName("주문 등록에 성공한다.")
    void succeedInRegisteringOrder() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(2);
        orderLineItem.setMenuId(savedMenu.getId());

        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem));

        // when
        Order savedOrder = orderService.create(order);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedOrder.getId()).isNotNull();
            softly.assertThat(savedOrder.getOrderLineItems()).hasSize(1);
        });
    }

    @Test
    @DisplayName("주문 항목이 없으면 주문 등록 시 예외가 발생한다.")
    void failToRegisterOrderWithNonExistOrderLine() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되지 않은 메뉴 항목을 사용하여 주문 등록 시 예외가 발생한다.")
    void failToRegisterOrderWithNonRegisteredOrderLine() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        Menu menu = new Menu();
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(2);

        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록된 테이블이 존재하지 않으면 주문 등록 시 예외가 발생한다.")
    void failToRegisterOrderWithNonRegisteredTable() {
        // given
        OrderTable orderTable = new OrderTable();

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(2);
        orderLineItem.setMenuId(savedMenu.getId());

        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록된 테이블이 비어있을 경우 주문 등록 시 예외가 발생한다.")
    void failToRegisterOrderWithNonEmptyTable() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(4);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(2);
        orderLineItem.setMenuId(savedMenu.getId());

        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 내역을 조회하는데 성공한다.")
    void succeedInSearchingOrderList() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(2);
        orderLineItem.setMenuId(savedMenu.getId());

        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        Order savedOrder = orderService.create(order);

        // when & then
        assertThat(orderService.list()).hasSize(1);
    }

    @Test
    @DisplayName("주문 상태 변경에 성공한다.")
    void succeedInChangingOrderStatus() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(2);
        orderLineItem.setMenuId(savedMenu.getId());

        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        Order savedOrder = orderService.create(order);

        // when
        Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.MEAL.name());

        Order chagedOrder = orderService.changeOrderStatus(savedOrder.getId(), newOrder);

        // then
        assertThat(chagedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("등록된 주문이 존재하지 않은 상태에서 주문 상태 변경 시 예외가 발생한다.")
    void failToChangeOrderStatusWithNonRegisteredOrder() {
        // given
        Order order = new Order();

        // when
        Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.MEAL.name());

        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), newOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태가 'COMPLETION'인 경우 주문 상태 변경 시 예외가 발생한다.")
    void failToChangeOrderStatusWithCompletionOrderStatus() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(4);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(2);
        orderLineItem.setMenuId(savedMenu.getId());

        Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());
        Order savedOrder = orderDao.save(order);

        // when
        Order newOrder = new Order();
        newOrder.setOrderStatus(OrderStatus.MEAL.name());

        // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), newOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
