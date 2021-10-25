package kitchenpos.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static kitchenpos.ui.MenuGroupRestControllerTest.postMenuGroup;
import static kitchenpos.ui.MenuRestControllerTest.postMenu;
import static kitchenpos.ui.ProductRestControllerTest.postProduct;
import static kitchenpos.ui.TableRestControllerTest.postOrderTable;
import static org.assertj.core.api.Assertions.assertThat;

class OrderRestControllerTest extends ControllerTest {
    private Order order;

    @BeforeEach
    void setUp() {
        super.setUp();
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("분식");
        MenuGroup savedMenuGroup = postMenuGroup(menuGroup).as(MenuGroup.class);

        Product product = new Product();
        product.setName("떡볶이");
        product.setPrice(BigDecimal.valueOf(10000));
        Product savedProduct = postProduct(product).as(Product.class);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(10);
        menuProduct.setProductId(savedProduct.getId());
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);

        Menu menu = new Menu();
        menu.setName("떡볶이");
        menu.setPrice(BigDecimal.valueOf(3000));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(menuProducts);
        Menu savedMenu = postMenu(menu).as(Menu.class);

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(1L);
        orderLineItem.setQuantity(5);
        orderLineItem.setMenuId(savedMenu.getId());
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = postOrderTable(orderTable).as(OrderTable.class);

        order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderLineItems(orderLineItems);
        order.setOrderTableId(savedOrderTable.getId());
    }

    @Test
    @DisplayName("Order 생성")
    void create() {
        ExtractableResponse<Response> response = postOrder(order);
        Order savedOrder = response.as(Order.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(savedOrder.getId()).isNotNull();
    }

    @Test
    @DisplayName("모든 Order 조회")
    void list() {
        postOrder(order);
        postOrder(order);
        postOrder(order);
        ExtractableResponse<Response> response = getOrders();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().as(List.class)).hasSize(3);
    }

    @Test
    @DisplayName("Order 상태 변경")
    void changeOrderStatus() {
        Order savedOrder = postOrder(order).as(Order.class);
        Order changeOrder = new Order();
        changeOrder.setOrderStatus(OrderStatus.MEAL.name());
        changeOrder.setOrderedTime(savedOrder.getOrderedTime());
        changeOrder.setOrderLineItems(savedOrder.getOrderLineItems());
        changeOrder.setOrderTableId(savedOrder.getOrderTableId());

        ExtractableResponse<Response> response = putOrderStatus(savedOrder.getId(), changeOrder);
        Order changedOrder = response.as(Order.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(changedOrder.getOrderStatus()).isEqualTo(changedOrder.getOrderStatus());
    }

    static ExtractableResponse<Response> postOrder(Order order) {
        return RestAssured
                .given().log().all()
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> getOrders() {
        return RestAssured
                .given().log().all()
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/orders")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> putOrderStatus(long orderId, Order changeOrder) {
        return RestAssured
                .given().log().all()
                .accept("application/json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(changeOrder)
                .when().put("/api/orders/" + orderId + "/order-status")
                .then().log().all().extract();
    }
}
