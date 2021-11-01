package kitchenpos.acceptance;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("Order 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    @DisplayName("POST /api/orders")
    @Nested
    class Post {

        @DisplayName("정상적인 경우 상태코드 201이 반환된다.")
        @Test
        void createPost() {
            // given
            OrderTable orderTable = OrderTable을_생성한다(2);
            MenuGroup menuGroup = MenuGroup을_생성한다("엄청난 그룹");

            Product 치즈버거 = Product를_생성한다("치즈버거", 4_000);
            Product 콜라 = Product를_생성한다("치즈버거", 1_600);
            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu1 = Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);
            Menu menu2 = Menu를_생성한다("색다른 메뉴", 4_600, menuGroup.getId(), menuProducts);

            OrderLineItem orderLineItem1 = OrderLineItem을_생성한다(menu1.getId(), 1);
            OrderLineItem orderLineItem2 = OrderLineItem을_생성한다(menu2.getId(), 1);

            Order order = Order를_생성한다(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .statusCode(CREATED.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(CREATED.value());
            assertThat(response.header("Location")).isNotNull();
            assertThat(response.body()).isNotNull();
        }

        @DisplayName("Order의 OrderLineItem이 없다면 상태코드 500이 반환된다.")
        @Test
        void noOrderLineItem() {
            // given
            OrderTable orderTable = OrderTable을_생성한다(2);
            MenuGroup menuGroup = MenuGroup을_생성한다("엄청난 그룹");

            Product 치즈버거 = Product를_생성한다("치즈버거", 4_000);
            Product 콜라 = Product를_생성한다("치즈버거", 1_600);

            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu = Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);

            Order order = Order를_생성한다(orderTable.getId(), new ArrayList<>());

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("동일한 메뉴를 시킬 때 Quantity가 아닌 OrderLineItem을 추가해서 OrderLineItem 개수와 Menu 개수가 다를 경우 상태코드 500이 반환된다.")
        @Test
        void orderLineItemCountNonMatchWithMenu() {
            // given
            OrderTable orderTable = OrderTable을_생성한다(2);
            MenuGroup menuGroup = MenuGroup을_생성한다("엄청난 그룹");

            Product 치즈버거 = Product를_생성한다("치즈버거", 4_000);
            Product 콜라 = Product를_생성한다("치즈버거", 1_600);

            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu = Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);

            OrderLineItem orderLineItem1 = OrderLineItem을_생성한다(menu.getId(), 1);
            OrderLineItem orderLineItem2 = OrderLineItem을_생성한다(menu.getId(), 1);

            Order order = Order를_생성한다(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("orderTable이 실제로 존재하지 않을 경우 상태코드 500이 반환된다.")
        @Test
        void orderTableNotFound() {
            // given
            MenuGroup menuGroup = MenuGroup을_생성한다("엄청난 그룹");

            Product 치즈버거 = Product를_생성한다("치즈버거", 4_000);
            Product 콜라 = Product를_생성한다("치즈버거", 1_600);
            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu1 = Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);
            Menu menu2 = Menu를_생성한다("색다른 메뉴", 4_600, menuGroup.getId(), menuProducts);

            OrderLineItem orderLineItem1 = OrderLineItem을_생성한다(menu1.getId(), 1);
            OrderLineItem orderLineItem2 = OrderLineItem을_생성한다(menu2.getId(), 1);

            Order order = Order를_생성한다(-1L, Arrays.asList(orderLineItem1, orderLineItem2));

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("Order의 OrderTable의 상태가 이미 Empty일 경우 상태코드 500이 반환된다.")
        @Test
        void orderTableStatusEmpty() {
            // given
            OrderTable orderTable = OrderTable을_생성한다(2, true);
            MenuGroup menuGroup = MenuGroup을_생성한다("엄청난 그룹");

            Product 치즈버거 = Product를_생성한다("치즈버거", 4_000);
            Product 콜라 = Product를_생성한다("치즈버거", 1_600);

            MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
            MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
            List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            Menu menu1 = Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);
            Menu menu2 = Menu를_생성한다("색다른 메뉴", 4_600, menuGroup.getId(), menuProducts);

            OrderLineItem orderLineItem1 = OrderLineItem을_생성한다(menu1.getId(), 1);
            OrderLineItem orderLineItem2 = OrderLineItem을_생성한다(menu2.getId(), 1);

            Order order = Order를_생성한다(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }
    }

    @DisplayName("GET /api/orders - 모든 Order들과 상태코드 200이 반환된다.")
    @Test
    void createGet() {
        // when
        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .when().get("/api/orders")
            .then().log().all()
            .statusCode(OK.value())
            .extract();

        List<Order> orders = response.jsonPath().getList(".", Order.class);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(orders).isNotNull();
    }

    @DisplayName("PUT /api/orders/{orderId}/order-status")
    @Nested
    class ChangeOrderStatus {

        @DisplayName("ID에 해당하는 Order가 존재하지 않을 경우 예외가 발생한다.")
        @Test
        void noExistOrderIdException() {
            // given
            Order statusOrder = Status_변화용_Order를_생성한다(COOKING);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(statusOrder)
                .when().put(String.format("/api/orders/%d/order-status", -1L))
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("Order의 상태가 이미 COMPLETION일 경우 예외가 발생한다.")
        @Test
        void alreadyCompletionStatusException() {
            // given
            Order order = httpRequest를_통해_Order를_생성한다();
            Order statusOrder = Status_변화용_Order를_생성한다(COMPLETION);

            putRequestWithBody(String.format("/api/orders/%d/order-status", order.getId()), statusOrder);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(statusOrder)
                .when().put(String.format("/api/orders/%d/order-status", order.getId()))
                .then().log().all()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("Order의 상태가 정상적으로 변경되고 반환된다.")
        @Test
        void success() {
            // given
            Order order = httpRequest를_통해_Order를_생성한다();
            Order statusOrder = Status_변화용_Order를_생성한다(COMPLETION);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(statusOrder)
                .when().put(String.format("/api/orders/%d/order-status", order.getId()))
                .then().log().all()
                .statusCode(OK.value())
                .extract();

            Order changedOrder = response.as(Order.class);

            // then
            assertThat(response.statusCode()).isEqualTo(OK.value());
            assertThat(changedOrder.getId()).isEqualTo(order.getId());
            assertThat(changedOrder.getOrderStatus()).isEqualTo(statusOrder.getOrderStatus());
        }
    }

    private Order httpRequest를_통해_Order를_생성한다() {
        OrderTable orderTable = OrderTable을_생성한다(2);
        MenuGroup menuGroup = MenuGroup을_생성한다("엄청난 그룹");

        Product 치즈버거 = Product를_생성한다("치즈버거", 4_000);
        Product 콜라 = Product를_생성한다("치즈버거", 1_600);
        MenuProduct 치즈버거_MenuProduct = MenuProduct를_생성한다(치즈버거.getId(), 1);
        MenuProduct 콜라_MenuProduct = MenuProduct를_생성한다(콜라.getId(), 1);
        List<MenuProduct> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

        Menu menu1 = Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);
        Menu menu2 = Menu를_생성한다("색다른 메뉴", 4_600, menuGroup.getId(), menuProducts);

        OrderLineItem orderLineItem1 = OrderLineItem을_생성한다(menu1.getId(), 1);
        OrderLineItem orderLineItem2 = OrderLineItem을_생성한다(menu2.getId(), 1);

        Order order = Order를_생성한다(orderTable.getId(), Arrays.asList(orderLineItem1, orderLineItem2));

        return postRequestWithBody("/api/orders", order).as(Order.class);
    }

    private Order Order를_생성한다(Long orderTableId, List<OrderLineItem> orderLineItems) {
        Order order = new Order();
        order.setOrderTableId(orderTableId);
        order.setOrderLineItems(orderLineItems);
        order.setOrderedTime(LocalDateTime.now());

        return order;
    }

    private Order Status_변화용_Order를_생성한다(OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderStatus(orderStatus.name());

        return order;
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests) {
        return OrderTable을_생성한다(numberOfGuests, false);
    }

    private OrderTable OrderTable을_생성한다(int numberOfGuests, boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(isEmpty);

        return postRequestWithBody("/api/tables", orderTable).as(OrderTable.class);
    }

    private OrderLineItem OrderLineItem을_생성한다(Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);

        return orderLineItem;
    }

    private Menu Menu를_생성한다(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return postRequestWithBody("/api/menus", menu).as(Menu.class);
    }

    private MenuGroup MenuGroup을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return postRequestWithBody("/api/menu-groups", menuGroup).as(MenuGroup.class);
    }

    private MenuProduct MenuProduct를_생성한다(Long productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    private Product Product를_생성한다(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return postRequestWithBody("/api/products", product).as(Product.class);
    }
}
