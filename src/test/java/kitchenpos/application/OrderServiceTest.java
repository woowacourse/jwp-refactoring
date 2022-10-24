package kitchenpos.application;

import static kitchenpos.fixture.MenuTestFixture.떡볶이;
import static kitchenpos.fixture.ProductFixture.불맛_떡볶이;
import static kitchenpos.fixture.ProductFixture.짜장맛_떡볶이;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private OrderTableDao orderTableDao;

    private Long menuId;
    private Long orderTableId;

    @BeforeEach
    void 메뉴_및_주문_테이블_생성() {
        Long menuGroupId = menuGroupDao.save(MenuGroupFixture.분식.toEntity())
                .getId();

        List<MenuProduct> menuProducts = 메뉴_상품_목록(
                상품_생성(불맛_떡볶이),
                상품_생성(짜장맛_떡볶이)
        );

        Menu menu = 떡볶이.toEntity(menuGroupId, menuProducts);
        Menu savedMenu = menuDao.save(menu);
        menuId = savedMenu.getId();

        for (MenuProduct menuProduct : menu.getMenuProducts()) {
            menuProduct.setMenuId(menuId);
            menuProductDao.save(menuProduct);
        }

        orderTableId = 주문_테이블_생성().getId();
    }

    @Test
    void 주문_정상_생성() {
        // given
        Order order = new Order();
        List<OrderLineItem> orderLineItems = Collections.singletonList(주문_항목());
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTableId);

        // when
        Order savedOrder = orderService.create(order);

        // then
        Optional<Order> actual = orderDao.findById(savedOrder.getId());
        assertThat(actual).isNotEmpty();
    }

    @Test
    void 주문_항목_0개인_경우_실패() {
        // given
        Order order = new Order();
        order.setOrderLineItems(new ArrayList<>());
        order.setOrderTableId(orderTableId);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void menu_id가_null인_주문_항목을_주문하는_경우_실패() {
        // given
        Order order = new Order();
        List<OrderLineItem> orderLineItems = Collections.singletonList(menuId가_null인_주문_항목());
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTableId);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_목록_조회() {
        // given
        Order order = new Order();
        List<OrderLineItem> orderLineItems = Collections.singletonList(주문_항목());
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(orderTableId);
        Order savedOrder = orderService.create(order);

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders)
                .usingRecursiveComparison()
                .ignoringFields("orderLineItems")
                .isEqualTo(Collections.singletonList(savedOrder));

        assertThat(orders.get(0).getOrderLineItems())
                .usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(orderLineItems);
    }

    private OrderLineItem menuId가_null인_주문_항목() {
        return 주문_항목(null);
    }

    private OrderLineItem 주문_항목() {
        return 주문_항목(menuId);
    }

    private OrderLineItem 주문_항목(final Long menuId) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(1);
        return orderLineItem;
    }

    private Product 상품_생성(final ProductFixture productFixture) {
        return productDao.save(productFixture.toEntity());
    }

    private List<MenuProduct> 메뉴_상품_목록(final Product... products) {
        return Arrays.stream(products)
                .map(this::메뉴_상품)
                .collect(Collectors.toList());
    }

    private MenuProduct 메뉴_상품(final Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(null);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(3);
        return menuProduct;
    }

    private OrderTable 주문_테이블_생성() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(3);
        return orderTableDao.save(orderTable);
    }
}
