package kitchenpos.application;

import static kitchenpos.application.KitchenposFixture.메뉴그룹만들기;
import static kitchenpos.application.KitchenposFixture.메뉴상품만들기;
import static kitchenpos.application.KitchenposFixture.상품만들기;
import static kitchenpos.application.KitchenposFixture.저장할메뉴만들기;
import static kitchenpos.application.KitchenposFixture.주문테이블만들기;
import static kitchenpos.application.KitchenposFixture.주문할메뉴만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.order.application.dto.OrderLineItemsRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.application.validator.OrderCreationOrderTableValidator;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Price;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.order.application.validator.OrderCreationOrderLineItemValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;

@DataJdbcTest
@Import({ProductService.class, MenuService.class,
        MenuGroupService.class, OrderService.class, TableService.class, OrderCreationOrderLineItemValidator.class,
        OrderCreationOrderTableValidator.class})
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
        final MenuResponse savedMenu
                = menuService.create("메뉴!", new Price(new BigDecimal("4000")), savedMenuGroup.getId(),
                List.of(new MenuProductRequest(savedProduct.getId(), 4L)));

        final MenuResponse savedMenu2
                = menuService.create("메뉴 2!", new Price(new BigDecimal("9000")), savedMenuGroup.getId(),
                List.of(new MenuProductRequest(savedProduct.getId(), 4L)));

        // given : 주문 메뉴
        final OrderLineItem orderLineItem = 주문할메뉴만들기(savedMenu.getId(), 4);
        final OrderLineItem orderLineItem2 = 주문할메뉴만들기(savedMenu2.getId(), 3);

        // given : 주문 테이블
        final OrderTable savedOrderTable = 주문테이블만들기(tableService, false);

        // given : 주문
        final OrderResponse savedOrder = orderService.create(
                savedOrderTable.getId(),
                List.of(
                        new OrderLineItemsRequest(orderLineItem.getMenuId(), orderLineItem.getQuantity()),
                        new OrderLineItemsRequest(orderLineItem2.getMenuId(), orderLineItem2.getQuantity())
                )
        );

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
        assertThatThrownBy(() -> orderService.create(savedOrderTable.getId(), List.of())) // 비어있는 OrderLineItems
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
        final OrderLineItem orderLineItem = new OrderLineItem(0L, new Price(new BigDecimal("4000")), "메뉴",
                4); // 존재하지 않는 메뉴

        final OrderLineItem orderLineItem2 = 주문할메뉴만들기(savedMenu2.getId(), 3);

        // given : 주문 테이블
        final OrderTable savedOrderTable = 주문테이블만들기(tableService, false);

        // given : 주문
        assertThatThrownBy(() -> orderService.create(savedOrderTable.getId(), List.of(
                new OrderLineItemsRequest(orderLineItem.getMenuId(), orderLineItem.getQuantity()),
                new OrderLineItemsRequest(orderLineItem2.getMenuId(), orderLineItem2.getQuantity())
        )))
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
        final OrderLineItem orderLineItem = 주문할메뉴만들기(savedMenu.getId(), 4);
        final OrderLineItem orderLineItem2 = 주문할메뉴만들기(savedMenu2.getId(), 3);

        // given : 주문
        assertThatThrownBy(() -> orderService.create(0L, List.of(
                new OrderLineItemsRequest(orderLineItem.getMenuId(), orderLineItem.getQuantity()),
                new OrderLineItemsRequest(orderLineItem2.getMenuId(), orderLineItem2.getQuantity())
        )))
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
        final MenuResponse savedMenu
                = menuService.create("메뉴!", new Price(new BigDecimal("4000")), savedMenuGroup.getId(),
                List.of(new MenuProductRequest(savedProduct.getId(), 4L)));
        final MenuResponse savedMenu2
                = menuService.create("메뉴 2!", new Price(new BigDecimal("9000")), savedMenuGroup.getId(),
                List.of(new MenuProductRequest(savedProduct.getId(), 4L)));

        // given : 주문 메뉴
        final OrderLineItem orderLineItem = 주문할메뉴만들기(savedMenu.getId(), 4);
        final OrderLineItem orderLineItem2 = 주문할메뉴만들기(savedMenu2.getId(), 3);

        // given : 주문 테이블
        final OrderTable savedOrderTable = 주문테이블만들기(tableService, false);

        // given : 주문
        final OrderResponse savedOrder = orderService.create(
                savedOrderTable.getId(),
                List.of(
                        new OrderLineItemsRequest(orderLineItem.getMenuId(), orderLineItem.getQuantity()),
                        new OrderLineItemsRequest(orderLineItem2.getMenuId(), orderLineItem2.getQuantity())
                )
        );

        assertThat(savedOrder).extracting("orderStatus").isEqualTo(OrderStatus.COOKING);
    }
}
