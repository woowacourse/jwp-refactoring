package kitchenpos.application;

import static kitchenpos.application.kitchenposFixture.메뉴그룹만들기;
import static kitchenpos.application.kitchenposFixture.메뉴상품만들기;
import static kitchenpos.application.kitchenposFixture.상품만들기;
import static kitchenpos.application.kitchenposFixture.저장할메뉴만들기;
import static kitchenpos.application.kitchenposFixture.주문테이블만들기;
import static kitchenpos.application.kitchenposFixture.주문할메뉴만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderLineItemDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
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
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;

@DataJdbcTest
@Import({ProductService.class, MenuService.class,
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
        final Product savedProduct = 상품만들기("상품!", "4000", productService);

        // given : 메뉴 그룹
        final MenuGroup savedMenuGroup = 메뉴그룹만들기(menuGroupService);

        // given : 메뉴
        final MenuProduct menuProduct = 메뉴상품만들기(savedProduct, 4L);

        final Menu savedMenu = menuService.create(저장할메뉴만들기("메뉴!", "4000", savedMenuGroup.getId(), menuProduct));
        final Menu savedMenu2 = menuService.create(저장할메뉴만들기("메뉴 2!", "9000", savedMenuGroup.getId(), menuProduct));

        // given : 주문 메뉴
        final OrderLineItem orderLineItem = 주문할메뉴만들기(savedMenu, 4);
        final OrderLineItem orderLineItem2 = 주문할메뉴만들기(savedMenu2, 3);

        // given : 주문 테이블
        final OrderTable savedOrderTable = 주문테이블만들기(tableService, false);

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
            @Autowired TableService tableService
    ) {
        // given : 주문 테이블
        final OrderTable savedOrderTable = 주문테이블만들기(tableService, false);

        // given : 주문
        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(List.of()); // 비어있는 OrderLineItems
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문하려는 메뉴 중 실재하지 않는 메뉴가 있으면 주문할 수 없다.")
    void notExistingMenu(
            @Autowired ProductService productService,
            @Autowired MenuGroupService menuGroupService,
            @Autowired TableService tableService
    ) {
        // given : 상품
        final Product savedProduct = 상품만들기("상품!", "4000", productService);

        // given : 메뉴 그룹
        final MenuGroup savedMenuGroup = 메뉴그룹만들기(menuGroupService);

        // given : 메뉴
        final MenuProduct menuProduct = 메뉴상품만들기(savedProduct, 4L);
        final Menu savedMenu2 = 저장할메뉴만들기("메뉴 2!", "9000", savedMenuGroup.getId(), menuProduct);

        // given : 주문 메뉴
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(4);
        orderLineItem.setMenuId(0L); // 존재하지 않는 메뉴

        final OrderLineItem orderLineItem2 = 주문할메뉴만들기(savedMenu2, 3);

        // given : 주문 테이블
        final OrderTable savedOrderTable = 주문테이블만들기(tableService, false);

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
            @Autowired MenuGroupService menuGroupService
    ) {
        // given : 상품
        final Product savedProduct = 상품만들기("상품!", "4000", productService);

        // given : 메뉴 그룹
        final MenuGroup savedMenuGroup = 메뉴그룹만들기(menuGroupService);

        // given : 메뉴
        final MenuProduct menuProduct = 메뉴상품만들기(savedProduct, 4L);
        final Menu savedMenu = 저장할메뉴만들기("메뉴!", "4000", savedMenuGroup.getId(), menuProduct);
        final Menu savedMenu2 = 저장할메뉴만들기("메뉴 2!", "9000", savedMenuGroup.getId(), menuProduct);

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
        final Product savedProduct = 상품만들기("상품!", "4000", productService);

        // given : 메뉴 그룹
        final MenuGroup savedMenuGroup = 메뉴그룹만들기(menuGroupService);

        // given : 메뉴
        final MenuProduct menuProduct = 메뉴상품만들기(savedProduct, 4L);
        final Menu savedMenu = menuService.create(저장할메뉴만들기("메뉴!", "4000", savedMenuGroup.getId(), menuProduct));
        final Menu savedMenu2 = menuService.create(저장할메뉴만들기("메뉴 2!", "9000", savedMenuGroup.getId(), menuProduct));

        // given : 주문 메뉴
        final OrderLineItem orderLineItem = 주문할메뉴만들기(savedMenu, 4);
        final OrderLineItem orderLineItem2 = 주문할메뉴만들기(savedMenu2, 3);

        // given : 주문 테이블
        final OrderTable savedOrderTable = 주문테이블만들기(tableService, false);

        // given : 주문
        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem, orderLineItem2));
        final Order savedOrder = orderService.create(order);

        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }
}
