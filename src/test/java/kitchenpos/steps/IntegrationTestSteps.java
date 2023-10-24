package kitchenpos.steps;

import static io.restassured.RestAssured.given;
import static kitchenpos.TestAid.toJson;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntegrationTestSteps {

    private static final String MENU_GROUP_API = "/api/menu-groups";
    private static final String MENU_API = "/api/menus";
    private static final String PRODUCT_API = "/api/products";
    public static final String ORDER_TABLE_API = "/api/tables";

    @Autowired
    SharedContext sharedContext;

    public void createMenuGroup(MenuGroup menuGroup) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(ContentType.JSON)
                                                        .body(toJson(menuGroup))
                                                        .when()
                                                        .post(MENU_GROUP_API)
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    public void createMenu(Menu menu) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(ContentType.JSON)
                                                        .body(menu)
                                                        .when()
                                                        .post(MENU_API)
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    public void createProduct(Product product) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(ContentType.JSON)
                                                        .body(product)
                                                        .when()
                                                        .post(PRODUCT_API)
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    public void createTable(OrderTable orderTable) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(ContentType.JSON)
                                                        .body(orderTable)
                                                        .when()
                                                        .post(ORDER_TABLE_API)
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    public void changeNumberOfGuests(OrderTable orderTable, int number) {
        orderTable.setNumberOfGuests(number);
        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(ContentType.JSON)
                                                        .body(orderTable)
                                                        .when()
                                                        .put(ORDER_TABLE_API + "/"
                                                            + orderTable.getId()
                                                            + "/number-of-guests")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    public OrderTable changeEmpty(Long tableId, OrderTable table) {
        return given().log().all()
                      .contentType(ContentType.JSON)
                      .body(table)
                      .when()
                      .put(ORDER_TABLE_API + "/" + tableId + "/empty")
                      .then().log().all()
                      .extract().as(OrderTable.class);
    }

    public void createOrder(Order order) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(ContentType.JSON)
                                                        .body(order)
                                                        .when()
                                                        .post("/api/orders")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    public void changeOrderStatus(Long orderId, Order changed) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(ContentType.JSON)
                                                        .body(changed)
                                                        .when()
                                                        .put("/api/orders/" + orderId + "/order-status")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    public void createTableGroup(TableGroup tableGroup) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(ContentType.JSON)
                                                        .body(tableGroup)
                                                        .when()
                                                        .post("/api/table-groups")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    public void upgroup(Long id) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .when()
                                                        .delete("/api/table-groups/" + id)
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }
}

