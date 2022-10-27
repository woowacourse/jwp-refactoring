package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixtures.generateMenu;
import static kitchenpos.application.fixture.MenuGroupFixtures.generateMenuGroup;
import static kitchenpos.application.fixture.MenuProductFixtures.generateMenuProduct;
import static kitchenpos.application.fixture.OrderFixtures.generateOrder;
import static kitchenpos.application.fixture.OrderLineItemFixtures.*;
import static kitchenpos.application.fixture.OrderTableFixtures.generateOrderTable;
import static kitchenpos.application.fixture.ProductFixtures.generateProduct;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.fixture.OrderTableFixtures;
import kitchenpos.dao.fake.FakeMenuDao;
import kitchenpos.dao.fake.FakeMenuGroupDao;
import kitchenpos.dao.fake.FakeOrderDao;
import kitchenpos.dao.fake.FakeOrderLineItemDao;
import kitchenpos.dao.fake.FakeOrderTableDao;
import kitchenpos.dao.fake.FakeProductDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
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
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderServiceTest {

    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderTableDao orderTableDao;
    private final OrderService orderService;

    @Autowired
    public OrderServiceTest(final MenuGroupDao menuGroupDao,
                            final ProductDao productDao,
                            final MenuDao menuDao,
                            final OrderDao orderDao,
                            final OrderTableDao orderTableDao,
                            final OrderService orderService) {
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
        this.orderService = orderService;
    }

    @BeforeEach
    void setUp() {
        FakeMenuGroupDao.deleteAll();
        FakeProductDao.deleteAll();
        FakeMenuDao.deleteAll();
        FakeOrderDao.deleteAll();
        FakeOrderLineItemDao.deleteAll();
        FakeOrderTableDao.deleteAll();
    }

    @Test
    void order를_생성한다() {
        MenuGroup 한마리메뉴 = menuGroupDao.save(generateMenuGroup("한마리메뉴"));
        Product 후라이드 = productDao.save(generateProduct("후라이드"));

        List<MenuProduct> menuProducts = List.of(generateMenuProduct(후라이드.getId(), 1));
        Menu menu = menuDao.save(generateMenu("후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴.getId(), menuProducts));

        OrderTable orderTable = orderTableDao.save(generateOrderTable(0, false));
        OrderLineItem orderLineItem = generateOrderLineItem(menu.getId(), 1);

        Order actual = orderService.create(generateOrder(orderTable.getId(), List.of(orderLineItem)));

        assertAll(() -> {
            assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId());
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            assertThat(actual.getOrderLineItems()).hasSize(1);
        });
    }

    @Test
    void orderLineItems가_비어있는_경우_예외를_던진다() {
        Order order = generateOrder(0L, List.of());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderLineItems의_사이즈가_실제_menu에_포함된_개수가_일치하지_않는_경우_예외를_던진다() {
        MenuGroup 한마리메뉴 = menuGroupDao.save(generateMenuGroup("한마리메뉴"));
        Product 후라이드 = productDao.save(generateProduct("후라이드"));

        List<MenuProduct> menuProducts = List.of(generateMenuProduct(후라이드.getId(), 1));
        Menu menu = menuDao.save(generateMenu("후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴.getId(), menuProducts));

        OrderTable orderTable = orderTableDao.save(generateOrderTable(0, false));
        OrderLineItem orderLineItem = generateOrderLineItem(menu.getId(), 1);

        assertThatThrownBy(
                () -> orderService.create(generateOrder(orderTable.getId(), List.of(orderLineItem, orderLineItem))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderTable이_비어있는_경우_예외를_던진다() {
        MenuGroup 한마리메뉴 = menuGroupDao.save(generateMenuGroup("한마리메뉴"));
        Product 후라이드 = productDao.save(generateProduct("후라이드"));

        List<MenuProduct> menuProducts = List.of(generateMenuProduct(후라이드.getId(), 1));
        Menu menu = menuDao.save(generateMenu("후라이드치킨", BigDecimal.valueOf(16000), 한마리메뉴.getId(), menuProducts));

        OrderTable orderTable = orderTableDao.save(generateOrderTable(0, true));
        OrderLineItem orderLineItem = generateOrderLineItem(menu.getId(), 1);

        assertThatThrownBy(() -> orderService.create(generateOrder(orderTable.getId(), List.of(orderLineItem))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void order_list를_조회한다() {
        OrderTable orderTable = orderTableDao.save(generateOrderTable(0, true));
        orderDao.save(generateOrder(orderTable.getId(), OrderStatus.COOKING, List.of()));
        orderDao.save(generateOrder(orderTable.getId(), OrderStatus.COOKING, List.of()));

        List<Order> actual = orderService.list();

        assertThat(actual).hasSize(2);
    }

    @Test
    void order의_상태를_변경한다() {
        OrderTable orderTable = orderTableDao.save(generateOrderTable(0, true));
        Order order = orderDao.save(generateOrder(orderTable.getId(), OrderStatus.COOKING, List.of()));
        Order changedOrder = generateOrder(orderTable.getId(), OrderStatus.MEAL, List.of());

        Order actual = orderService.changeOrderStatus(order.getId(), changedOrder);

        assertAll(() -> {
            assertThat(actual.getOrderTableId()).isEqualTo(orderTable.getId());
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        });
    }

    @Test
    void order의_상태가_COMPLETION인_경우_예외를_던진다() {
        OrderTable orderTable = orderTableDao.save(generateOrderTable(0, true));
        Order order = orderDao.save(generateOrder(orderTable.getId(), OrderStatus.COMPLETION, List.of()));
        Order changedOrder = generateOrder(orderTable.getId(), OrderStatus.MEAL, List.of());

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changedOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
