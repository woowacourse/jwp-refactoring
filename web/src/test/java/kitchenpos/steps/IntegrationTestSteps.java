package kitchenpos.steps;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.service.MenuDto;
import kitchenpos.menugroup.service.MenuGroupDto;
import kitchenpos.service.OrderDto;
import kitchenpos.ordertable.service.OrderTableDto;
import kitchenpos.ordertable.service.TableGroupDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import kitchenpos.product.service.ProductDto;

@Component
public class IntegrationTestSteps {

    private static final String MENU_GROUP_API = "/api/menu-groups";
    private static final String MENU_API = "/api/menus";
    private static final String PRODUCT_API = "/api/products";
    public static final String ORDER_TABLE_API = "/api/tables";

    @Autowired
    SharedContext sharedContext;

    public void createMenuGroup(MenuGroupDto menuGroupDto) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(ContentType.JSON)
                                                        .body(menuGroupDto)
                                                        .when()
                                                        .post(MENU_GROUP_API)
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    public void createMenu(MenuDto menuDto) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(ContentType.JSON)
                                                        .body(menuDto)
                                                        .when()
                                                        .post(MENU_API)
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    public void createProduct(ProductDto productDto) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(ContentType.JSON)
                                                        .body(productDto)
                                                        .when()
                                                        .post(PRODUCT_API)
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    public void createTable(OrderTableDto orderTableDto) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(ContentType.JSON)
                                                        .body(orderTableDto)
                                                        .when()
                                                        .post(ORDER_TABLE_API)
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    public void changeNumberOfGuests(OrderTableDto orderTableDto, int number) {
        orderTableDto.setNumberOfGuests(number);
        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(ContentType.JSON)
                                                        .body(orderTableDto)
                                                        .when()
                                                        .put(ORDER_TABLE_API + "/{orderTableId}/number-of-guests", orderTableDto.getId())
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    public OrderTableDto changeEmpty(Long tableId, OrderTableDto table) {
        return given().log().all()
                      .contentType(ContentType.JSON)
                      .body(table)
                      .when()
                      .put(ORDER_TABLE_API + "/{tableId}/empty", tableId)
                      .then().log().all()
                      .extract().as(OrderTableDto.class);
    }

    public void createOrder(OrderDto orderDto) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(ContentType.JSON)
                                                        .body(orderDto)
                                                        .when()
                                                        .post("/api/orders")
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    public void changeOrderStatus(Long orderId, OrderDto changed) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(ContentType.JSON)
                                                        .body(changed)
                                                        .when()
                                                        .put("/api/orders/{orderId}/order-status", orderId)
                                                        .then().log().all()
                                                        .extract();

        sharedContext.setResponse(response);
    }

    public void createTableGroup(TableGroupDto tableGroupDto) {
        ExtractableResponse<Response> response = given().log().all()
                                                        .contentType(ContentType.JSON)
                                                        .body(tableGroupDto)
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

