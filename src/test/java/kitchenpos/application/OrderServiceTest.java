package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.IntegrationTest;
import kitchenpos.common.fixture.TMenu;
import kitchenpos.common.fixture.TMenuGroup;
import kitchenpos.common.fixture.TMenuProduct;
import kitchenpos.common.fixture.TOrder;
import kitchenpos.common.fixture.TOrderLineItem;
import kitchenpos.common.fixture.TOrderTable;
import kitchenpos.common.fixture.TProduct;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends IntegrationTest {

    private OrderTable orderTable;
    private MenuGroup menuGroup;
    private Product product;
    private Menu menu;
    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        orderTable = tableService.create(TOrderTable.builder()
            .numberOfGuests(4)
            .empty(false)
            .build());

        menuGroup = menuGroupService.create(TMenuGroup.builder()
            .name("추천메뉴")
            .build()
        );

        product = productService.create(TProduct.후라이드());
        MenuProduct menuProduct = TMenuProduct.builder()
            .productId(product.getId())
            .quantity(2L)
            .builder();

        menu = menuService.create(TMenu.builder()
            .name("후라이드+후라이드")
            .price(BigDecimal.valueOf(19000))
            .menuGroupId(menuGroup.getId())
            .menuProducts(Collections.singletonList(menuProduct))
            .build());

        orderLineItems = Arrays.asList(
            TOrderLineItem.builder()
                .menuId(menu.getId())
                .quantity(1L)
                .build()
        );
    }

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void create() {
        Order order = TOrder.builder()
            .orderTableId(orderTable.getId())
            .orderLineItems(orderLineItems)
            .build();

        Order savedOrder = orderService.create(order);

        assertThat(savedOrder)
            .usingRecursiveComparison()
            .ignoringFieldsMatchingRegexes(".*seq", ".*id")
            .isEqualTo(order);
    }

    @DisplayName("주문 id는 null일 수 없다.")
    @Test
    void create_orderIdCannotBeNull() {
        Order order = TOrder.builder()
            .orderTableId(null)
            .orderLineItems(orderLineItems)
            .build();

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 id는 null일 수 없다.")
    @Test
    void create_menuIdCannotBeNull() {
        Order order = TOrder.builder()
            .orderTableId(orderTable.getId())
            .orderLineItems(Arrays.asList(
                TOrderLineItem.builder()
                    .menuId(null)
                    .quantity(1L)
                    .build()
            ))
            .build();

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목은 적어도 1개 있어야 한다.")
    @Test
    void create_OrderLineTimeMustExistLeastOne() {
        Order order = TOrder.builder()
            .orderTableId(orderTable.getId())
            .orderLineItems(Collections.emptyList())
            .build();

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목은 중복일 수 없다.")
    @Test
    void create_orderLineItemCannotBeDuplicate() {
        Order order = TOrder.builder()
            .orderTableId(orderTable.getId())
            .orderLineItems(Arrays.asList(TOrderLineItem.builder()
                    .menuId(menu.getId())
                    .quantity(1L)
                    .build(),
                TOrderLineItem.builder()
                    .menuId(menu.getId())
                    .quantity(1L)
                    .build()
                )
            ).build();

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목은 없는 메뉴로 등록할 수 없다.")
    @Test
    void create_NoExistMenu() {
        Order order = TOrder.builder()
            .orderTableId(orderTable.getId())
            .orderLineItems(Collections.singletonList(TOrderLineItem.builder()
                .menuId(999L)
                .quantity(1L)
                .build()
            )).build();

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블은 비어있으면 안된다.")
    @Test
    void create_emptyOrderTable() {
        OrderTable orderTable = tableService.create(TOrderTable.builder()
            .numberOfGuests(4)
            .empty(true)
            .build());

        Order order = TOrder.builder()
            .orderTableId(orderTable.getId())
            .orderLineItems(Collections.singletonList(TOrderLineItem.builder()
                .menuId(menu.getId())
                .quantity(1L)
                .build()
            )).build();

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 초기 상태는 COOKING이다.")
    @Test
    void create_cooking() {
        Order order = TOrder.builder()
            .orderTableId(orderTable.getId())
            .orderLineItems(orderLineItems)
            .build();

        Order savedOrder = orderService.create(order);

        assertThat(savedOrder.getOrderStatus()).isEqualTo("COOKING");
    }

    @DisplayName("주문의 상태를 변경할 수 있다. COOKING -> MEAL")
    @Test
    void changeOrderStatus_cookingToMEAL() {
        Order order = TOrder.builder()
            .orderTableId(orderTable.getId())
            .orderLineItems(orderLineItems)
            .build();

        Order savedOrder = orderService.create(order);
        Order changedOrder = orderService.changeOrderStatus(
            savedOrder.getId(),
            TOrder.builder().orderStatus("MEAL").build()
        );

        assertThat(changedOrder.getOrderStatus()).isEqualTo("MEAL");
    }

    @DisplayName("주문의 상태를 변경할 수 있다. MEAL -> COMPLETION")
    @Test
    void changeOrderStatus_MEALToCOMPLETION() {
        Order order = TOrder.builder()
            .orderTableId(orderTable.getId())
            .orderLineItems(orderLineItems)
            .build();

        Order savedOrder = orderService.create(order);
        orderService.changeOrderStatus(
            savedOrder.getId(),
            TOrder.builder().orderStatus("MEAL").build()
        );

        Order changedOrder = orderService.changeOrderStatus(
            savedOrder.getId(),
            TOrder.builder().orderStatus("COMPLETION").build()
        );

        assertThat(changedOrder.getOrderStatus()).isEqualTo("COMPLETION");
    }

    @DisplayName("주문의 상태가 COMPLETION 경우 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatus() {
        Order order = TOrder.builder()
            .orderTableId(orderTable.getId())
            .orderLineItems(orderLineItems)
            .build();

        Order savedOrder = orderService.create(order);
        orderService.changeOrderStatus(
            savedOrder.getId(),
            TOrder.builder().orderStatus("MEAL").build()
        );

        orderService.changeOrderStatus(
            savedOrder.getId(),
            TOrder.builder().orderStatus("COMPLETION").build()
        );

        assertThatThrownBy(() -> orderService.changeOrderStatus(
            savedOrder.getId(),
            TOrder.builder().orderStatus("MEAL").build()
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        orderService.create(TOrder.builder()
            .orderTableId(orderTable.getId())
            .orderLineItems(orderLineItems)
            .build());

        List<Order> orders = orderService.list();
        assertThat(orders).hasSize(1);
    }
}
