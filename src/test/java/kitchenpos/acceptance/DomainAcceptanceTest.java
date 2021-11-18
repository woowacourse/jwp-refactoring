package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.ui.dto.MenuGroupRequest;
import kitchenpos.menu.ui.dto.MenuGroupResponse;
import kitchenpos.menu.ui.dto.MenuProductRequest;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.menu.ui.dto.MenuResponse;
import kitchenpos.menu.ui.dto.MenuUpdateRequest;
import kitchenpos.order.ui.dto.OrderLineItemRequest;
import kitchenpos.order.ui.dto.OrderRequest;
import kitchenpos.order.ui.dto.OrderResponse;
import kitchenpos.table.ui.dto.OrderTableIdRequest;
import kitchenpos.table.ui.dto.TableGroupRequest;
import kitchenpos.table.ui.dto.TableGroupResponse;
import kitchenpos.table.ui.dto.TableRequest;
import kitchenpos.table.ui.dto.TableResponse;
import kitchenpos.product.ui.dto.ProductRequest;
import kitchenpos.product.ui.dto.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;

import static java.util.Collections.singletonList;

abstract class DomainAcceptanceTest extends AcceptanceTest {
    protected Long POST_SAMPLE_MENU() {
        Long productId = POST_SAMPLE_PRODUCT();
        Long menuGroupId = POST_SAMPLE_MENU_GROUP();

        MenuProductRequest menuProductRequest = MenuProductRequest.from(productId, 2L);
        MenuRequest menuRequest = MenuRequest.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(19000.00),
                menuGroupId,
                singletonList(menuProductRequest)
        );

        // then
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuRequest)
                .when().post("/api/menus")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        MenuResponse menuResponse = response.as(MenuResponse.class);
        return menuResponse.getId();
    }

    protected Long PUT_SAMPLE_MENU(Long menuId, String menuName, BigDecimal price) {
        MenuUpdateRequest menuUpdateRequest = MenuUpdateRequest.of(menuName, price);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuUpdateRequest)
                .when().put("/api/menus/" + menuId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        MenuResponse menuResponse = response.as(MenuResponse.class);
        return menuResponse.getId();
    }

    protected Long POST_SAMPLE_MENU_GROUP() {
        // given -  when
        MenuGroupRequest menuGroupRequest = MenuGroupRequest.from("추천메뉴");

        // then
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroupRequest)
                .when().post("/api/menu-groups")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        MenuGroupResponse menuGroupResponse = response.as(MenuGroupResponse.class);
        return menuGroupResponse.getId();
    }

    protected long POST_SAMPLE_ORDER() {
        Long menuId = POST_SAMPLE_MENU();

        long orderTableId = POST_SAMPLE_ORDER_TABLE(1, false);
        OrderRequest orderRequest = OrderRequest.of(
                orderTableId,
                singletonList(OrderLineItemRequest.of(menuId, 1L))
        );

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(orderRequest)
                .when().post("/api/order")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        OrderResponse orderResponse = response.as(OrderResponse.class);
        return orderResponse.getId();
    }

    protected Long POST_SAMPLE_PRODUCT() {
        // given
        ProductRequest productRequest = ProductRequest.from("강정치킨", BigDecimal.valueOf(17000.0));

        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when().post("/api/products")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        ProductResponse productResponse = response.as(ProductResponse.class);
        return productResponse.getId();
    }

    protected long POST_SAMPLE_ORDER_TABLE(int numberOfGuests, boolean empty) {
        TableRequest tableRequest = TableRequest.of(numberOfGuests, empty);

        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableRequest)
                .when().post("/api/tables")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        TableResponse tableResponse = response.as(TableResponse.class);
        return tableResponse.getId();
    }

    protected long POST_DEFAULT_TABLE_GROUPS() {
        // given
        long orderTableId1 = POST_SAMPLE_ORDER_TABLE(1, true);
        long orderTableId2 = POST_SAMPLE_ORDER_TABLE(1, true);

        TableGroupRequest tableGroupRequest = TableGroupRequest.from(
                Arrays.asList(
                        OrderTableIdRequest.from(orderTableId1),
                        OrderTableIdRequest.from(orderTableId2)
                ));
        // when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroupRequest)
                .when().post("/api/table-groups")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        TableGroupResponse tableGroupResponse = response.as(TableGroupResponse.class);
        return tableGroupResponse.getId();
    }
}
