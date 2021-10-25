package kitchenpos.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("classpath:truncate.sql")
@Transactional
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected ExtractableResponse<Response> makeResponse(String url, TestMethod testMethod) {
        return makeResponse(url, testMethod, null);
    }

    protected ExtractableResponse<Response> makeResponse(String url, TestMethod testMethod, Object requestBody) {
        RequestSpecification request = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE);

        if (Objects.nonNull(requestBody)) {
            request = request.body(requestBody);
        }
        return testMethod.extractedResponse(request, url);
    }

    protected Order order() {
        Menu menu = makeResponse("/api/menus", TestMethod.POST, menu())
            .as(Menu.class);
        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 2);
        List<OrderLineItem> items = new ArrayList<>();
        items.add(orderLineItem);
        OrderTable orderTable = new OrderTable(2, false);
        OrderTable createdOrderTable = makeResponse("/api/tables", TestMethod.POST, orderTable)
            .as(OrderTable.class);

        return new Order(createdOrderTable.getId(), items);
    }

    protected Menu menu() {
        MenuGroup menuGroup = new MenuGroup("menuGroup");
        MenuGroup createdMenuGroup = makeResponse("/api/menu-groups", TestMethod.POST, menuGroup)
            .as(MenuGroup.class);
        Product product = new Product("product", BigDecimal.valueOf(1000));
        Product createdProduct = makeResponse("/api/products", TestMethod.POST, product)
            .as(Product.class);
        MenuProduct menuProduct = new MenuProduct(createdProduct.getId(), 10);
        List<MenuProduct> menuProducts = Collections.singletonList(menuProduct);

        return new Menu("menu", BigDecimal.valueOf(5000), createdMenuGroup.getId(), menuProducts);
    }
}
