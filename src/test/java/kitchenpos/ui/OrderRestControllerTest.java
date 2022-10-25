package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class OrderRestControllerTest extends ControllerTest {

    @Autowired
    private OrderRestController orderRestController;

    @Test
    void create() {
        Product product = createProduct("강정치킨", 18000);
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);

        MenuGroup menuGroup = createMenuGroup("추천 메뉴");
        Menu menu = createMenu("강정치킨", 18000, menuGroup.getId(), List.of(menuProduct));

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(1);

        OrderTable orderTable = createOrderTable(2, false);
        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem));
        ResponseEntity<Order> response = orderRestController.create(order);

        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getStatusCodeValue()).isNotNull();
        assertThat(response.getBody().getOrderStatus()).isEqualTo("COOKING");
        assertThat(response.getBody().getOrderLineItems()).isNotNull();
    }

    @Test
    void createOrderLineEmpty() {
        OrderTable orderTable = createOrderTable(2, false);

        Product product = createProduct("강정치킨", 18000);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);

        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(Collections.emptyList());

        assertThatThrownBy(() -> orderRestController.create(order))
                .hasMessage("주문 항목이 비어있습니다.");
    }

    @Test
    void createInvalidOrderLine() {
        OrderTable orderTable = createOrderTable(2, false);

        Product product = createProduct("강정치킨", 18000);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);

        MenuGroup menuGroup = createMenuGroup("추천 메뉴");
        Menu menu = createMenu("강정치킨", 18000, menuGroup.getId(), List.of(menuProduct));

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(1);

        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem, orderLineItem));

        assertThatThrownBy(() -> orderRestController.create(order))
                .hasMessage("주문항목의 수와 메뉴의 수가 일치하지 않습니다.");
    }

    @Test
    void createNotFoundOrderTable() {
        Product product = createProduct("강정치킨", 18000);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);

        MenuGroup menuGroup = createMenuGroup("추천 메뉴");
        Menu menu = createMenu("강정치킨", 18000, menuGroup.getId(), List.of(menuProduct));

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(1);

        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(List.of(orderLineItem));

        assertThatThrownBy(() -> orderRestController.create(order))
                .hasMessage("주문 테이블이 존재하지 않습니다.");
    }

    @Test
    void createOrderTableIsEmpty() {
        OrderTable orderTable = createOrderTable(2, true);
        Product product = createProduct("강정치킨", 18000);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);

        MenuGroup menuGroup = createMenuGroup("추천 메뉴");
        Menu menu = createMenu("강정치킨", 18000, menuGroup.getId(), List.of(menuProduct));

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(1);

        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(List.of(orderLineItem));

        assertThatThrownBy(() -> orderRestController.create(order))
                .hasMessage("빈 테이블입니다.");
    }

    @Test
    void list() {
        OrderLineItem orderLineItem = getOrderLineItem();
        OrderTable orderTable = createOrderTable(2, false);

        createOrder(orderTable.getId(), List.of(orderLineItem));
        createOrder(orderTable.getId(), List.of(orderLineItem));
        createOrder(orderTable.getId(), List.of(orderLineItem));

        ResponseEntity<List<Order>> response = orderRestController.list();

        assertThat(response.getBody()).hasSize(3);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
    }

    private OrderLineItem getOrderLineItem() {
        MenuGroup menuGroup = createMenuGroup("추천 메뉴");
        Product product = createProduct("강정치킨", 18000);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);

        Menu menu = createMenu("강정치킨", 18000, menuGroup.getId(), List.of(menuProduct));

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(1);
        return orderLineItem;
    }

}
