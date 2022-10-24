package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void 주문을_생성할_수_있다() {
        Product product1 = productDao.save(new Product("상품1", new BigDecimal(10000)));
        Product product2 = productDao.save(new Product("상품2", new BigDecimal(20000)));

        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹1"));

        MenuProduct menuProduct1 = new MenuProduct(1L, null, product1.getId(), 1);
        MenuProduct menuProduct2 = new MenuProduct(1L, null, product2.getId(), 2);

        Menu menu1 = menuDao.save(new Menu("메뉴1", new BigDecimal(40000), menuGroup.getId(),
                new ArrayList<>(Arrays.asList(menuProduct1, menuProduct2))));
        Menu menu2 = menuDao.save(new Menu("메뉴2", new BigDecimal(40000), menuGroup.getId(),
                new ArrayList<>(Arrays.asList(menuProduct1, menuProduct2))));

        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 5, false));

        OrderLineItem orderLineItem1 = new OrderLineItem(null, menu1.getId(), 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, menu2.getId(), 2);

        Order order = new Order(orderTable.getId(), null,
                new ArrayList<>(Arrays.asList(orderLineItem1, orderLineItem2)));

        Order actual = orderService.create(order);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getOrderStatus()).isEqualTo("COOKING");
            assertThat(actual.getOrderLineItems()).hasSize(2);
        });
    }

    @Test
    void 주문_항목이_비어있는_경우_주문을_생성할_수_없다() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 5, false));

        Order order = new Order(orderTable.getId(), null, new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_빈_테이블인_경우_주문을_생성할_수_없다() {
        Product product1 = productDao.save(new Product("상품1", new BigDecimal(10000)));
        Product product2 = productDao.save(new Product("상품2", new BigDecimal(20000)));

        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("메뉴 그룹1"));

        MenuProduct menuProduct1 = new MenuProduct(1L, null, product1.getId(), 1);
        MenuProduct menuProduct2 = new MenuProduct(1L, null, product2.getId(), 2);

        Menu menu1 = menuDao.save(new Menu("메뉴1", new BigDecimal(40000), menuGroup.getId(),
                new ArrayList<>(Arrays.asList(menuProduct1, menuProduct2))));
        Menu menu2 = menuDao.save(new Menu("메뉴2", new BigDecimal(40000), menuGroup.getId(),
                new ArrayList<>(Arrays.asList(menuProduct1, menuProduct2))));

        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 5, true));

        OrderLineItem orderLineItem1 = new OrderLineItem(null, menu1.getId(), 1);
        OrderLineItem orderLineItem2 = new OrderLineItem(null, menu2.getId(), 2);

        Order order = new Order(orderTable.getId(), null,
                new ArrayList<>(Arrays.asList(orderLineItem1, orderLineItem2)));

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_주문_목록을_조회할_수_있다() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 5, false));

        Order order1 = new Order(orderTable.getId(), "COOKING", new ArrayList<>());
        Order order2 = new Order(orderTable.getId(), "COOKING", new ArrayList<>());

        orderDao.save(order1);
        orderDao.save(order2);

        List<Order> actual = orderService.list();

        assertThat(actual).hasSize(2);
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 5, false));

        Order order = orderDao.save(new Order(orderTable.getId(), "COOKING", new ArrayList<>()));
        Order newOrder = orderDao.save(new Order(orderTable.getId(), "MEAL", new ArrayList<>()));

        Order actual = orderService.changeOrderStatus(order.getId(), newOrder);

        assertThat(actual.getOrderStatus()).isEqualTo("MEAL");
    }

    @Test
    void 기존_주문_상태가_계산_완료_상태인_경우_상태를_변경할_수_없다() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 5, false));

        Order order = orderDao.save(new Order(orderTable.getId(), "COMPLETION", new ArrayList<>()));
        Order newOrder = orderDao.save(new Order(orderTable.getId(), "MEAL", new ArrayList<>()));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), newOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
