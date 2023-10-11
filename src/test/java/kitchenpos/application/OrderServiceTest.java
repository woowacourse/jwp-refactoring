package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderLineItemDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateProductDao;
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
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

@JdbcTest
@Import({ProductService.class, JdbcTemplateProductDao.class, MenuService.class,
        JdbcTemplateMenuDao.class, MenuGroupService.class, JdbcTemplateMenuGroupDao.class,
        JdbcTemplateMenuProductDao.class, JdbcTemplateOrderTableDao.class, OrderService.class,
        JdbcTemplateOrderDao.class, TableService.class,
        JdbcTemplateOrderLineItemDao.class})
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("주문 테이블, 주문하려는 메뉴들의 식별자와 수량을 제공하여 주문할 수 있다.")
    void given(
            @Autowired ProductService productService,
            @Autowired MenuService menuService,
            @Autowired MenuGroupService menuGroupService,
            @Autowired TableService tableService
    ) {
        // given : 상품
        final Product savedProduct = 상품만들기(productService);

        // given : 메뉴 그룹
        final MenuGroup savedMenuGroup = 메뉴그룹만들기(menuGroupService);

        // given : 메뉴
        final MenuProduct menuProduct = 메뉴에담을상품만들기(savedProduct);

        final Menu savedMenu = 메뉴만들기("메뉴!", "4000", menuProduct, savedMenuGroup, menuService);
        final Menu savedMenu2 = 메뉴만들기("메뉴 2!", "9000", menuProduct, savedMenuGroup, menuService);

        // given : 주문 메뉴
        final OrderLineItem orderLineItem = 주문할메뉴만들기(savedMenu, 4);
        final OrderLineItem orderLineItem2 = 주문할메뉴만들기(savedMenu2, 3);

        // given : 주문 테이블
        final OrderTable savedOrderTable = 주문테이블만들기(tableService);

        // given : 주문
        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem, orderLineItem2));
        final Order savedOrder = orderService.create(order);

        assertThat(savedOrder).isNotNull();
    }

    @Test
    @DisplayName("주문하면서 주문할 상품이 없다면(OrderLines가 비어있다면) 주문할 수 없다.")
    void invalidOrderLines(
            @Autowired ProductService productService,
            @Autowired MenuService menuService,
            @Autowired MenuGroupService menuGroupService,
            @Autowired TableService tableService
    ) {
        // given : 상품
        final Product savedProduct = 상품만들기(productService);

        // given : 메뉴 그룹
        final MenuGroup savedMenuGroup = 메뉴그룹만들기(menuGroupService);

        // given : 메뉴
        final MenuProduct menuProduct = 메뉴에담을상품만들기(savedProduct);

        메뉴만들기("메뉴!", "4000", menuProduct, savedMenuGroup, menuService);
        메뉴만들기("메뉴 2!", "9000", menuProduct, savedMenuGroup, menuService);

        // given : 주문 테이블
        final OrderTable savedOrderTable = 주문테이블만들기(tableService);

        // given : 주문
        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(List.of()); // 비어있는 OrderLinesITems
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문하려는 메뉴 중 실재하지 않는 메뉴가 있으면 주문할 수 없다.")
    void notExistingMenu(
            @Autowired ProductService productService,
            @Autowired MenuService menuService,
            @Autowired MenuGroupService menuGroupService,
            @Autowired TableService tableService
    ) {
        // given : 상품
        final Product savedProduct = 상품만들기(productService);

        // given : 메뉴 그룹
        final MenuGroup savedMenuGroup = 메뉴그룹만들기(menuGroupService);

        // given : 메뉴
        final MenuProduct menuProduct = 메뉴에담을상품만들기(savedProduct);
        final Menu savedMenu2 = 메뉴만들기("메뉴 2!", "9000", menuProduct, savedMenuGroup, menuService);

        // given : 주문 메뉴
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(4);
        orderLineItem.setMenuId(0L); // 존재하지 않는 메뉴

        final OrderLineItem orderLineItem2 = 주문할메뉴만들기(savedMenu2, 3);

        // given : 주문 테이블
        final OrderTable savedOrderTable = 주문테이블만들기(tableService);

        // given : 주문
        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem, orderLineItem2));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실재하지 않는 주문 테이블에서는 주문할 수 없다.")
    void notExistingOrderTable(
            @Autowired ProductService productService,
            @Autowired MenuService menuService,
            @Autowired MenuGroupService menuGroupService
    ) {
        // given : 상품
        final Product savedProduct = 상품만들기(productService);

        // given : 메뉴 그룹
        final MenuGroup savedMenuGroup = 메뉴그룹만들기(menuGroupService);

        // given : 메뉴
        final MenuProduct menuProduct = 메뉴에담을상품만들기(savedProduct);
        final Menu savedMenu = 메뉴만들기("메뉴!", "4000", menuProduct, savedMenuGroup, menuService);
        final Menu savedMenu2 = 메뉴만들기("메뉴 2!", "9000", menuProduct, savedMenuGroup, menuService);

        // given : 주문 메뉴
        final OrderLineItem orderLineItem = 주문할메뉴만들기(savedMenu, 4);
        final OrderLineItem orderLineItem2 = 주문할메뉴만들기(savedMenu2, 3);

        // given : 주문
        final Order order = new Order();
        order.setOrderTableId(0L); // 존재하지 않는 주문 테이블
        order.setOrderLineItems(List.of(orderLineItem, orderLineItem2));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문이 정상적으로 들어가면, 주문 상태가 COOKING으로 변한다.")
    void orderStatus(
            @Autowired ProductService productService,
            @Autowired MenuService menuService,
            @Autowired MenuGroupService menuGroupService,
            @Autowired TableService tableService
    ) {
        // given : 상품
        final Product savedProduct = 상품만들기(productService);

        // given : 메뉴 그룹
        final MenuGroup savedMenuGroup = 메뉴그룹만들기(menuGroupService);

        // given : 메뉴
        final MenuProduct menuProduct = 메뉴에담을상품만들기(savedProduct);
        final Menu savedMenu = 메뉴만들기("메뉴!", "4000", menuProduct, savedMenuGroup, menuService);
        final Menu savedMenu2 = 메뉴만들기("메뉴 2!", "9000", menuProduct, savedMenuGroup, menuService);

        // given : 주문 메뉴
        final OrderLineItem orderLineItem = 주문할메뉴만들기(savedMenu, 4);
        final OrderLineItem orderLineItem2 = 주문할메뉴만들기(savedMenu2, 3);

        // given : 주문 테이블
        final OrderTable savedOrderTable = 주문테이블만들기(tableService);

        // given : 주문
        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem, orderLineItem2));
        final Order savedOrder = orderService.create(order);

        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    private static OrderTable 주문테이블만들기(final TableService tableService) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = tableService.create(orderTable);
        return savedOrderTable;
    }

    private static OrderLineItem 주문할메뉴만들기(final Menu savedMenu, final int quantity) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(quantity);
        orderLineItem.setMenuId(savedMenu.getId());
        return orderLineItem;
    }

    private static Menu 메뉴만들기(final String name, final String price, final MenuProduct menuProduct,
                              final MenuGroup savedMenuGroup, final MenuService menuService) {
        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(new BigDecimal(price));
        menu.setMenuProducts(List.of(menuProduct));
        menu.setMenuGroupId(savedMenuGroup.getId());
        return menuService.create(menu);
    }

    private static MenuProduct 메뉴에담을상품만들기(final Product savedProduct) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(4L);
        return menuProduct;
    }

    private static MenuGroup 메뉴그룹만들기(final MenuGroupService menuGroupService) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("메뉴 그룹!");
        return menuGroupService.create(menuGroup);
    }

    private static Product 상품만들기(final ProductService productService) {
        final Product product = new Product();
        product.setName("상품!");
        product.setPrice(new BigDecimal("4000"));
        return productService.create(product);
    }
}
