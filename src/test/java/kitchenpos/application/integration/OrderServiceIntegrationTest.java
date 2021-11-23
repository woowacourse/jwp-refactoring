package kitchenpos.application.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableService;
import kitchenpos.application.common.factory.MenuFactory;
import kitchenpos.application.common.factory.MenuGroupFactory;
import kitchenpos.application.common.factory.MenuProductFactory;
import kitchenpos.application.common.factory.OrderFactory;
import kitchenpos.application.common.factory.OrderLineItemFactory;
import kitchenpos.application.common.factory.OrderTableFactory;
import kitchenpos.application.common.factory.ProductFactory;
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

public class OrderServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("생성 - 주문을 생성(등록)할 수 있다.")
    @Test
    void create_success() {
        // given
        OrderTable savedOrderTable = tableService.create(
            OrderTableFactory.create(5, false)
        );
        Product savedProduct = productService.create(
            ProductFactory.create("후라이드", BigDecimal.valueOf(16_000))
        );
        MenuProduct menuProduct = MenuProductFactory.create(savedProduct, 4);

        MenuGroup savedMenuGroup = menuGroupService.create(MenuGroupFactory.create("두마리메뉴"));

        Menu savedMenu = menuService.create(MenuFactory.create(
            "후라이드치킨",
            BigDecimal.valueOf(16_000),
            savedMenuGroup,
            Arrays.asList(menuProduct))
        );

        OrderLineItem orderLineItem = OrderLineItemFactory.create(savedMenu, 4);

        // when
        Order order = OrderFactory.create(savedOrderTable, Arrays.asList(orderLineItem));
        Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(savedOrder.getOrderTableId()).isEqualTo(savedOrderTable.getId());
        assertThat(savedOrder.getOrderedTime()).isBefore(LocalDateTime.now());
        assertThat(savedOrder.getOrderLineItems())
            .hasSize(1);
    }

    @DisplayName("생성 - 주문 메뉴(OrderLineItem)는 적어도 1개 이상이어야 한다.")
    @Test
    void create_orderLineItemLessThanOne_fail() {
        // given
        OrderTable savedOrderTable = tableService.create(
            OrderTableFactory.create(5, false)
        );

        // when, then
        Order order = OrderFactory.create(savedOrderTable, Collections.emptyList());
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 주문 메뉴는 적어도 1개 이상이어야 한다.");
    }

    @DisplayName("생성 - 존재하지 않는 테이블에 주문을 생성할 수 없다.")
    @Test
    void create_orderTableNonExists_fail() {
        // given
        Product savedProduct = productService.create(
            ProductFactory.create("후라이드", BigDecimal.valueOf(16_000))
        );
        MenuProduct menuProduct = MenuProductFactory.create(savedProduct, 4);

        MenuGroup savedMenuGroup = menuGroupService.create(MenuGroupFactory.create("두마리메뉴"));

        Menu savedMenu = menuService.create(MenuFactory.create(
            "후라이드치킨",
            BigDecimal.valueOf(16_000),
            savedMenuGroup,
            Arrays.asList(menuProduct))
        );

        OrderLineItem orderLineItem = OrderLineItemFactory.create(savedMenu, 4);
        OrderTable nonSavedOrderTable = OrderTableFactory.create(5, false);

        // when, then
        Order order = OrderFactory.create(nonSavedOrderTable, Arrays.asList(orderLineItem));
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 존재하지 않는 테이블에 주문을 생성할 수 없다.");
    }

    @DisplayName("생성 - 주문이 속한 테이블은 비어있으면 안된다.")
    @Test
    void create_orderTAbleEmpty_fail() {
        // given
        Product savedProduct = productService.create(
            ProductFactory.create("후라이드", BigDecimal.valueOf(16_000))
        );
        MenuProduct menuProduct = MenuProductFactory.create(savedProduct, 4);

        MenuGroup savedMenuGroup = menuGroupService.create(MenuGroupFactory.create("두마리메뉴"));

        Menu savedMenu = menuService.create(MenuFactory.create(
            "후라이드치킨",
            BigDecimal.valueOf(16_000),
            savedMenuGroup,
            Arrays.asList(menuProduct))
        );

        OrderLineItem orderLineItem = OrderLineItemFactory.create(savedMenu, 4);
        OrderTable savedOrderTable = tableService.create(OrderTableFactory.create(0, true));

        // when, then
        Order order = OrderFactory.create(savedOrderTable, Arrays.asList(orderLineItem));
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 주문이 속한 테이블은 비어있으면 안된다.");
    }
    
    @DisplayName("조회 - 전체 주문을 조회할 수 있다.")
    @Test
    void list_success() {
        // given
        Product savedProduct = productService.create(
            ProductFactory.create("후라이드", BigDecimal.valueOf(16_000))
        );
        MenuProduct menuProduct = MenuProductFactory.create(savedProduct, 4);

        MenuGroup savedMenuGroup = menuGroupService.create(MenuGroupFactory.create("두마리메뉴"));

        Menu savedMenu = menuService.create(MenuFactory.create(
            "후라이드치킨",
            BigDecimal.valueOf(16_000),
            savedMenuGroup,
            Arrays.asList(menuProduct))
        );

        OrderLineItem orderLineItem = OrderLineItemFactory.create(savedMenu, 4);
        OrderTable savedOrderTable = tableService.create(OrderTableFactory.create(5, false));
        
        // when
        Order savedOrder1 = orderService.create(OrderFactory.create(savedOrderTable, Arrays.asList(orderLineItem)));
        Order savedOrder2 = orderService.create(OrderFactory.create(savedOrderTable, Arrays.asList(orderLineItem)));
        List<Order> orders = orderService.list();
        
        // then
        assertThat(orders)
            .hasSize(2)
            .containsExactlyInAnyOrder(savedOrder1, savedOrder2);
    }

    @DisplayName("변경 - 주문 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus_success() {
        // given
        Product savedProduct = productService.create(
            ProductFactory.create("후라이드", BigDecimal.valueOf(16_000))
        );
        MenuProduct menuProduct = MenuProductFactory.create(savedProduct, 4);

        MenuGroup savedMenuGroup = menuGroupService.create(MenuGroupFactory.create("두마리메뉴"));

        Menu savedMenu = menuService.create(MenuFactory.create(
            "후라이드치킨",
            BigDecimal.valueOf(16_000),
            savedMenuGroup,
            Arrays.asList(menuProduct))
        );

        OrderLineItem orderLineItem = OrderLineItemFactory.create(savedMenu, 4);
        OrderTable savedOrderTable = tableService.create(OrderTableFactory.create(5, false));

        // when
        Order savedOrder = orderService.create(OrderFactory.create(savedOrderTable, Arrays.asList(orderLineItem)));
        savedOrder.setOrderStatus(OrderStatus.COMPLETION);
        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @DisplayName("변경 - 존재하지 않는 주문은 변경할 수 없다.")
    @Test
    void changeOrderStatus_nonExistsOrder_fail() {
        // given
        OrderTable savedOrderTable = tableService.create(OrderTableFactory.create(5, false));
        Order nonSavedOrder = OrderFactory.create(savedOrderTable, null);

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(nonSavedOrder.getId(), nonSavedOrder))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 존재하지 않는 주문은 변경할 수 없다.");
    }

    @DisplayName("변경 - COMPLETION인 주문은 변경할 수 없다.")
    @Test
    void changeOrderStatus_statusCompletion_fail() {
        // given
        Product savedProduct = productService.create(
            ProductFactory.create("후라이드", BigDecimal.valueOf(16_000))
        );
        MenuProduct menuProduct = MenuProductFactory.create(savedProduct, 4);

        MenuGroup savedMenuGroup = menuGroupService.create(MenuGroupFactory.create("두마리메뉴"));

        Menu savedMenu = menuService.create(MenuFactory.create(
            "후라이드치킨",
            BigDecimal.valueOf(16_000),
            savedMenuGroup,
            Arrays.asList(menuProduct))
        );

        OrderLineItem orderLineItem = OrderLineItemFactory.create(savedMenu, 4);
        OrderTable savedOrderTable = tableService.create(OrderTableFactory.create(5, false));
        Order savedOrder = orderService.create(OrderFactory.create(savedOrderTable, Arrays.asList(orderLineItem)));
        savedOrder.setOrderStatus(OrderStatus.COMPLETION);
        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        // when, then
        changedOrder.setOrderStatus(OrderStatus.COOKING);
        assertThatThrownBy(() -> orderService.changeOrderStatus(changedOrder.getId(), changedOrder))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] COMPLETION인 주문은 상태를 변경할 수 없다.");
    }
}
