package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@Import({
        OrderService.class,
        JdbcTemplateMenuDao.class,
        JdbcTemplateOrderDao.class,
        JdbcTemplateOrderLineItemDao.class,
        JdbcTemplateOrderTableDao.class,
        JdbcTemplateProductDao.class,
        JdbcTemplateMenuGroupDao.class,
        JdbcTemplateMenuProductDao.class
})
class OrderServiceTest extends ServiceTest {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private Menu 후1양1_메뉴;
    private Menu 간1양1_메뉴;
    private OrderTable 주문테이블;
    private OrderLineItem 후1양1_수량1;
    private OrderLineItem 간1양1_수량1;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("두마리메뉴");
        MenuGroup 두마리메뉴 = menuGroupDao.save(menuGroup);

        Product product1 = new Product();
        product1.setName("후라이드");
        product1.setPrice(BigDecimal.valueOf(16000));
        Product 후라이드 = productDao.save(product1);

        Product product2 = new Product();
        product2.setName("양념치킨");
        product2.setPrice(BigDecimal.valueOf(16000));
        Product 양념치킨 = productDao.save(product2);

        Product product3 = new Product();
        product3.setName("간장치킨");
        product3.setPrice(BigDecimal.valueOf(16000));
        Product 간장치킨 = productDao.save(product3);

        MenuProduct 후라이드_한마리 = new MenuProduct();
        후라이드_한마리.setProductId(후라이드.getId());
        후라이드_한마리.setQuantity(1);

        MenuProduct 양념치킨_한마리 = new MenuProduct();
        양념치킨_한마리.setProductId(양념치킨.getId());
        양념치킨_한마리.setQuantity(1);

        MenuProduct 간장치킨_한마리 = new MenuProduct();
        간장치킨_한마리.setProductId(간장치킨.getId());
        간장치킨_한마리.setQuantity(1);

        Menu menu1 = new Menu();
        menu1.setName("두마리메뉴 - 후1양1");
        menu1.setPrice(BigDecimal.valueOf(32000L));
        menu1.setMenuGroupId(두마리메뉴.getId());
        menu1.setMenuProducts(List.of(후라이드_한마리, 양념치킨_한마리));

        Menu menu2 = new Menu();
        menu2.setName("두마리메뉴 - 간1양1");
        menu2.setPrice(BigDecimal.valueOf(32000L));
        menu2.setMenuGroupId(두마리메뉴.getId());
        menu2.setMenuProducts(List.of(간장치킨_한마리, 양념치킨_한마리));

        후1양1_메뉴 = menuDao.save(menu1);
        간1양1_메뉴 = menuDao.save(menu2);

        OrderTable orderTable = new OrderTable();
        orderTable.changeEmpty(false);
        orderTable.changeNumberOfGuests(0);
        주문테이블 = orderTableDao.save(orderTable);

        후1양1_수량1 = new OrderLineItem();
        후1양1_수량1.setMenuId(후1양1_메뉴.getId());
        후1양1_수량1.setQuantity(1);

        간1양1_수량1 = new OrderLineItem();
        간1양1_수량1.setMenuId(간1양1_메뉴.getId());
        간1양1_수량1.setQuantity(1);
    }

    @DisplayName("주문을 정상적으로 등록할 수 있다.")
    @Test
    void create() {
        // given
        Order expected = new Order();
        expected.setOrderTableId(주문테이블.getId());
        expected.setOrderLineItems(List.of(후1양1_수량1, 간1양1_수량1));

        // when
        Order actual = orderService.create(expected);

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderDao.findById(actual.getId())).isPresent();
            softly.assertThat(actual.getOrderLineItems()).hasSize(2);
            softly.assertThat(actual.getOrderLineItems()).extracting("orderId")
                    .containsExactly(actual.getId(), actual.getId());
            softly.assertThat(actual.getOrderLineItems()).extracting("menuId")
                    .contains(후1양1_메뉴.getId(), 간1양1_메뉴.getId());
            softly.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        });
    }

    @DisplayName("주문 등록 시, 주문에 속한 수량이 있는 메뉴가 없을 경우 예외가 발생한다.")
    @Test
    void create_FailWithEmptyOrderLineItems() {
        // given
        Order expected = new Order();
        expected.setOrderTableId(주문테이블.getId());
        expected.setOrderLineItems(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> orderService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시, 주문에 속한 수량이 있는 메뉴가 중복될 경우 예외가 발생한다.")
    @Test
    void create_FailWithDuplicatedOrderLineItems() {
        // given
        Order expected = new Order();
        expected.setOrderTableId(주문테이블.getId());
        expected.setOrderLineItems(List.of(후1양1_수량1, 후1양1_수량1));

        // when & then
        assertThatThrownBy(() -> orderService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시, 주문 테이블이 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void create_FailWithInvalidOrderTableId() {
        // given
        Long invalidOrderTableId = 1000L;
        Order expected = new Order();
        expected.setOrderTableId(invalidOrderTableId);
        expected.setOrderLineItems(List.of(후1양1_수량1, 간1양1_수량1));

        // when & then
        assertThatThrownBy(() -> orderService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 등록 시, 주문 테이블이 비어있을 경우 예외가 발생한다.")
    @Test
    void create_FailWithEmptyOrderTable() {
        // given
        주문테이블.changeEmpty(true);
        OrderTable 비어있는_주문테이블 = orderTableDao.save(주문테이블);

        Order expected = new Order();
        expected.setOrderTableId(비어있는_주문테이블.getId());
        expected.setOrderLineItems(List.of(후1양1_수량1, 간1양1_수량1));

        // when & then
        assertThatThrownBy(() -> orderService.create(expected))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        Order order1 = new Order();
        order1.setOrderTableId(주문테이블.getId());
        order1.setOrderLineItems(List.of(후1양1_수량1, 간1양1_수량1));
        Order 두마리치킨_2개_주문 = orderService.create(order1);

        Order order2 = new Order();
        order2.setOrderTableId(주문테이블.getId());
        order2.setOrderLineItems(List.of(후1양1_수량1));
        Order 두마리치킨_1개_주문 = orderService.create(order2);

        // when
        List<Order> list = orderService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(list).hasSize(2);
            softly.assertThat(list).usingRecursiveComparison()
                    .isEqualTo(List.of(두마리치킨_2개_주문, 두마리치킨_1개_주문));
        });
    }

    @DisplayName("주문 상태를 정상적으로 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        Order order = new Order();
        order.setOrderTableId(주문테이블.getId());
        order.setOrderLineItems(List.of(후1양1_수량1, 간1양1_수량1));

        Order 주문 = orderService.create(order);
        OrderStatus 변경할_주문_상태 = OrderStatus.MEAL;
        주문.changeOrderStatus(변경할_주문_상태.name());

        // when
        Order 주문상태가_변경된_주문 = orderService.changeOrderStatus(주문.getId(), 주문);

        // then
        assertThat(주문상태가_변경된_주문.getOrderStatus()).isEqualTo(변경할_주문_상태.name());
    }

    @DisplayName("주문 상태 변경 시, 존재하지 않는 주문일 경우 예외가 발생한다.")
    @Test
    void changeOrderStatus_FailWithInvalidOrderId() {
        // given
        Order order = new Order();
        order.setOrderTableId(주문테이블.getId());
        order.setOrderLineItems(List.of(후1양1_수량1, 간1양1_수량1));

        Order 주문 = orderService.create(order);
        OrderStatus 변경할_주문_상태 = OrderStatus.MEAL;
        주문.changeOrderStatus(변경할_주문_상태.name());

        Long invalidOrderId = 100L;

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(invalidOrderId, 주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경 시, 주문 상태가 COMPLETION인 경우 예외가 발생한다.")
    @Test
    void changeOrderStatus_FailWithInvalidOrderStatusRequest() {
        // given
        Order order = new Order();
        order.setOrderTableId(주문테이블.getId());
        order.setOrderLineItems(List.of(후1양1_수량1, 간1양1_수량1));

        OrderStatus 유효하지_않은_주문_상태 = OrderStatus.COMPLETION;
        order.changeOrderStatus(유효하지_않은_주문_상태.name());
        order.setOrderedTime(LocalDateTime.now());

        Order 주문 = orderDao.save(order);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
