package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
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
import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    void 주문을_생성한다() {
        // given
        Order order = 후라이드_후라이드_주문();

        // when
        Order actual = orderService.create(order);

        // then
        assertAll(
                () -> assertThat(actual.getOrderTableId()).isEqualTo(order.getOrderTableId()),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(actual.getOrderedTime()).isCloseTo(LocalDateTime.now(),
                        new TemporalUnitWithinOffset(1, ChronoUnit.SECONDS)),
                () -> assertThat(actual.getOrderLineItems()).extracting("orderId")
                        .containsExactly(actual.getId())
        );
    }

    @Test
    void 주문항목이_null이면_주문을_생성할_수_없다() {
        // given
        Order order = 주문(null);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문항목이_없으면_주문을_생성할_수_없다() {
        // given
        Order order = 주문(Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문항목에_들어있는_메뉴가_존재하지않으면_주문을_생성할_수_없다() {
        // given
        OrderLineItem invalidOrderItem = new OrderLineItem();
        invalidOrderItem.setMenuId(-1L);
        invalidOrderItem.setQuantity(2L);

        Order order = 주문(Collections.singletonList(invalidOrderItem));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 같은_메뉴가_다른_주문항목에_존재하면_주문을_생성할_수_없다() {
        // given
        Menu menu = 후라이드_세트_메뉴();

        OrderLineItem duplicatedOrderItem1 = new OrderLineItem();
        duplicatedOrderItem1.setMenuId(menu.getId());
        duplicatedOrderItem1.setQuantity(2L);

        OrderLineItem duplicatedOrderItem2 = new OrderLineItem();
        duplicatedOrderItem1.setMenuId(menu.getId());
        duplicatedOrderItem1.setQuantity(1L);
        Order order = 주문(Arrays.asList(duplicatedOrderItem1, duplicatedOrderItem2));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문에_등록된_주문_테이블이_존재하지않으면_주문을_생성할_수_없다() {
        // given
        OrderTable invalidOrderTable = new OrderTable();
        invalidOrderTable.setId(-1L);
        Order order = 주문(invalidOrderTable, 후라이드_세트_메뉴_주문_항목());

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_빈_상태라면_주문을_생성할_수_없다() {
        // given
        Order order = 주문(빈_상태의_주문_테이블(), 후라이드_세트_메뉴_주문_항목());

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_목록을_조회한다() {
        // given
        Order order = 후라이드_후라이드_주문();
        orderService.create(order);

        // when
        List<Order> actual = orderService.list();

        // then
        assertThat(actual).hasSizeGreaterThanOrEqualTo(1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COOKING"})
    void 주문_상태를_변경한다(String orderStatus) {
        // given
        Order order = orderService.create(후라이드_후라이드_주문());

        Order updateOrder = new Order();
        updateOrder.setOrderStatus(orderStatus);

        // when
        Order actual = orderService.changeOrderStatus(order.getId(), updateOrder);

        // then
        assertAll(
                () -> assertThat(actual.getOrderStatus()).isEqualTo(orderStatus),
                () -> assertThat(actual.getOrderLineItems()).usingRecursiveComparison()
                        .isEqualTo(order.getOrderLineItems())
        );
    }

    @Test
    void 주문이_등록되어있지_않으면_주문_상태를_변경할_수_없다() {
        // given
        long invalidOrderId = -1L;

        Order updateOrder = new Order();
        updateOrder.setOrderStatus("MEAL");

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(invalidOrderId, updateOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문이_계산완료_상태이면_주문_상태를_변경할_수_없다() {
        // given
        Order order = orderService.create(후라이드_후라이드_주문());
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(order.getId(), order);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public OrderTable 주문_테이블() {
        return orderTableDao.save(new OrderTable());
    }

    public OrderTable 빈_상태의_주문_테이블() {
        OrderTable emptyOrderTable = new OrderTable();
        emptyOrderTable.setEmpty(true);
        return orderTableDao.save(emptyOrderTable);
    }

    private Order 후라이드_후라이드_주문() {
        return 주문(주문_테이블(), 후라이드_세트_메뉴_주문_항목());
    }

    private Order 주문(List<OrderLineItem> orderLineItems) {
        return 주문(주문_테이블(), orderLineItems);
    }

    private Order 주문(OrderTable orderTable, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(orderLineItems);
        return order;
    }

    private List<OrderLineItem> 후라이드_세트_메뉴_주문_항목() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(후라이드_세트_메뉴().getId());
        orderLineItem.setQuantity(1);
        return Collections.singletonList(orderLineItem);
    }

    public Menu 후라이드_세트_메뉴() {
        Menu menu = new Menu();
        menu.setName("후라이드+후라이드");
        menu.setPrice(BigDecimal.valueOf(19000));
        menu.setMenuGroupId(추천메뉴().getId());
        menu.setMenuProducts(Collections.singletonList(후라이드_세트_메뉴_상품()));

        return menuDao.save(menu);
    }

    public MenuGroup 추천메뉴() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천메뉴");
        return menuGroupDao.save(menuGroup);
    }

    public MenuProduct 후라이드_세트_메뉴_상품() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(후라이드().getId());
        menuProduct.setQuantity(2);

        return menuProduct;
    }


    public Product 후라이드() {
        Product product = new Product();
        product.setName("후라이드");
        product.setPrice(BigDecimal.valueOf(10000));
        return productDao.save(product);
    }
}
