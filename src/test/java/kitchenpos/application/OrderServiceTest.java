package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.fixture.MenuFixture.MENU;
import static kitchenpos.fixture.OrderFixture.ORDER;
import static kitchenpos.fixture.OrderFixture.ORDER_COOKING;
import static kitchenpos.fixture.OrderLineItemFixture.ORDER_LINE_ITEM;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductDao productDao;

    @Test
    void 주문을_생성한다() {

        // given
        Product product = new Product();
        product.setName("짜장면");
        product.setPrice(BigDecimal.valueOf(8000.0));
        Product savedProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(5L);

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("중국식 메뉴 그룹");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        Menu menu = MENU("식사류", 10000L, savedMenuGroup.getId(), List.of(menuProduct));
        Menu savedMenu = menuDao.save(menu);

        OrderTable orderTable = ORDER_TABLE(false, 3);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        OrderLineItem orderLineItem = ORDER_LINE_ITEM(savedMenu.getId(), 1L);

        Order order = ORDER(savedOrderTable.getId(), List.of(orderLineItem));

        // when
        Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder).usingRecursiveComparison()
                .ignoringFields("id", "orderLineItems")
                .isEqualTo(order);

    }

    @Test
    void 모든_주문을_조회한다() {
        // given
        Product product = new Product();
        product.setName("짜장면");
        product.setPrice(BigDecimal.valueOf(8000.0));
        Product savedProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(5L);

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("중국식 메뉴 그룹");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        Menu menu = MENU("식사류", 10000L, savedMenuGroup.getId(), List.of(menuProduct));
        Menu savedMenu = menuDao.save(menu);

        OrderTable orderTable = orderTableDao.save(ORDER_TABLE(false, 1));
        OrderLineItem orderLineItem = ORDER_LINE_ITEM(savedMenu.getId(), 1L);
        Order order = ORDER_COOKING(orderTable.getId(), List.of(orderLineItem));
        Order savedOrder = orderDao.save(order);

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(List.of(savedOrder));
    }

    @Test
    void 주문의_상태를_바꾼다() {
        // given
        Product product = new Product();
        product.setName("짜장면");
        product.setPrice(BigDecimal.valueOf(8000.0));
        Product savedProduct = productDao.save(product);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(1L);
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(5L);

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("중국식 메뉴 그룹");
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);
        Menu menu = MENU("식사류", 10000L, savedMenuGroup.getId(), List.of(menuProduct));
        Menu savedMenu = menuDao.save(menu);

        OrderTable orderTable = orderTableDao.save(ORDER_TABLE(false, 1));
        OrderLineItem orderLineItem = ORDER_LINE_ITEM(savedMenu.getId(), 1L);
        Order order = orderDao.save(ORDER_COOKING(orderTable.getId(), List.of(orderLineItem)));

        // when
        order.setOrderStatus(MEAL.name());
        Order changedOrder = orderService.changeOrderStatus(order.getId(), order);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(MEAL.name());
        assertThat(order.getOrderStatus()).isEqualTo(MEAL.name());
    }
}
