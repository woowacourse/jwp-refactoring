package kitchenpos.tablegroup.acceptance;

import static java.util.stream.Collectors.toList;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.common.exception.ExceptionResponse;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menu.ui.response.MenuResponse;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderRequest;
import kitchenpos.table.ui.request.OrderTableRequest;
import kitchenpos.product.ui.request.ProductRequest;
import kitchenpos.tablegroup.ui.request.TableGroupRequest;
import kitchenpos.order.ui.response.OrderResponse;
import kitchenpos.table.ui.response.OrderTableResponse;
import kitchenpos.product.ui.response.ProductResponse;
import kitchenpos.tablegroup.ui.response.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("단체지정(테이블) 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("POST /api/table-groups - 단체를 지정할 때")
    @Nested
    class Post {

        @DisplayName("주문 테이블 수가 2개 미만일 경우 예외가 발생한다.")
        @Test
        void orderTablesUnderTwoException() {
            // given
            OrderTableResponse orderTable = HTTP_요청을_통해_OrderTable을_생성한다(1);
            TableGroupRequest request = TableGroupRequest을_생성한다(orderTable);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/table-groups")
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("단체의 주문테이블과 실제 저장된 주문테이블이 일치하지 않을 경우 예외가 발생한다.")
        @Test
        void nonMatchOrderTablesException() {
            // given
            OrderTableResponse orderTable1 = HTTP_요청을_통해_OrderTable을_생성한다(1);
            OrderTableResponse orderTable2 = HTTP_요청을_통해_OrderTable을_생성한다(2);
            OrderTableResponse orderTable3 = new OrderTableResponse(-1L, 1L, 5, false);

            TableGroupRequest request = TableGroupRequest을_생성한다(orderTable1, orderTable2, orderTable3);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/table-groups")
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("주문테이블이 비어있는 상태가 아닐 경우 예외가 발생한다.")
        @Test
        void orderTableIsNotEmptyException() {
            // given
            OrderTableResponse orderTable1 = HTTP_요청을_통해_OrderTable을_생성한다(1, false);
            OrderTableResponse orderTable2 = HTTP_요청을_통해_OrderTable을_생성한다(2, false);
            OrderTableResponse orderTable3 = HTTP_요청을_통해_OrderTable을_생성한다(3, false);

            TableGroupRequest request = TableGroupRequest을_생성한다(orderTable1, orderTable2, orderTable3);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/table-groups")
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("주문테이블에 이미 단체가 지정되어 있는 경우 예외가 발생한다.")
        @Test
        void alreadyMappingTableGroupException() {
            // given
            OrderTableResponse orderTable1 = HTTP_요청을_통해_OrderTable을_생성한다(1, true);
            OrderTableResponse orderTable2 = HTTP_요청을_통해_OrderTable을_생성한다(2, true);
            OrderTableResponse orderTable3 = HTTP_요청을_통해_OrderTable을_생성한다(3, true);

            HTTP_요청을_통해_TableGroup을_생성한다(orderTable1, orderTable2);

            TableGroupRequest request = TableGroupRequest을_생성한다(orderTable1, orderTable3);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/table-groups")
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("정상적인 경우 주문테이블들의 단체지정에 성공한다.")
        @Test
        void success() {
            // given
            OrderTableResponse orderTable1 = HTTP_요청을_통해_OrderTable을_생성한다(1, true);
            OrderTableResponse orderTable2 = HTTP_요청을_통해_OrderTable을_생성한다(2, true);
            OrderTableResponse orderTable3 = HTTP_요청을_통해_OrderTable을_생성한다(3, true);

            TableGroupRequest request = TableGroupRequest을_생성한다(orderTable1, orderTable2, orderTable3);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/table-groups")
                .then().log().all()
                .statusCode(CREATED.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(CREATED.value());
            assertThat(response.header("Location")).isNotNull();
            assertThat(response.body()).isNotNull();
        }
    }

    @DisplayName("DELETE /api/table-groups/{tableGroupId} - 단체지정을 해제할 때")
    @Nested
    class Ungroup {

        @DisplayName("조리 중인 주문테이블이 포함된 주문의 경우 예외가 발생한다.")
        @Test
        void existsByOrderTableIdInAndCOOKINGStatusInException() {
            // given
            MenuGroup menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);

            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuResponse menu1 = HTTP_요청을_통해_Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);
            MenuResponse menu2 = HTTP_요청을_통해_Menu를_생성한다("색다른 메뉴", 4_600, menuGroup.getId(), menuProducts);

            OrderTableResponse orderTable1 = HTTP_요청을_통해_OrderTable을_생성한다(1, true);
            OrderTableResponse orderTable2 = HTTP_요청을_통해_OrderTable을_생성한다(2, true);
            TableGroupResponse tableGroupResponse = HTTP_요청을_통해_TableGroup을_생성한다(orderTable1, orderTable2);

            OrderLineItemRequest orderLineItem1 = OrderLineItemRequest를_생성한다(menu1, 1);
            OrderLineItemRequest orderLineItem2 = OrderLineItemRequest를_생성한다(menu2, 1);
            HTTP_요청을_통해_Order를_생성한다(orderTable1, COOKING, Arrays.asList(orderLineItem1, orderLineItem2));

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroupResponse)
                .when().delete(String.format("/api/table-groups/%d", tableGroupResponse.getId()))
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("식사 중인 주문테이블이 포함된 주문의 경우 예외가 발생한다.")
        @Test
        void existsByOrderTableIdInAndMEALStatusInException() {
            // given
            MenuGroup menuGroup = HTTP_요청을_통해_MenuGroup을_생성한다("엄청난 그룹");

            ProductResponse 치즈버거 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 4_000);
            ProductResponse 콜라 = HTTP_요청을_통해_Product를_생성한다("치즈버거", 1_600);

            MenuProductRequest 치즈버거_MenuProduct = MenuProductRequest를_생성한다(치즈버거, 1);
            MenuProductRequest 콜라_MenuProduct = MenuProductRequest를_생성한다(콜라, 1);
            List<MenuProductRequest> menuProducts = Arrays.asList(치즈버거_MenuProduct, 콜라_MenuProduct);

            MenuResponse menu1 = HTTP_요청을_통해_Menu를_생성한다("엄청난 메뉴", 5_600, menuGroup.getId(), menuProducts);
            MenuResponse menu2 = HTTP_요청을_통해_Menu를_생성한다("색다른 메뉴", 4_600, menuGroup.getId(), menuProducts);

            OrderTableResponse orderTable1 = HTTP_요청을_통해_OrderTable을_생성한다(1, true);
            OrderTableResponse orderTable2 = HTTP_요청을_통해_OrderTable을_생성한다(2, true);
            TableGroupResponse tableGroup = HTTP_요청을_통해_TableGroup을_생성한다(orderTable1, orderTable2);

            OrderLineItemRequest orderLineItem1 = OrderLineItemRequest를_생성한다(menu1, 1);
            OrderLineItemRequest orderLineItem2 = OrderLineItemRequest를_생성한다(menu2, 1);
            HTTP_요청을_통해_Order를_생성한다(orderTable1, MEAL, Arrays.asList(orderLineItem1, orderLineItem2));

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().delete(String.format("/api/table-groups/%d", tableGroup.getId()))
                .then().log().all()
                .statusCode(BAD_REQUEST.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
            assertThat(response.as(ExceptionResponse.class)).isNotNull();
        }

        @DisplayName("식사, 조리 중인 주문테이블이 없는 경우 단체지정 해제에 성공한다.")
        @Test
        void success() {
            // given
            OrderTableResponse orderTable1 = HTTP_요청을_통해_OrderTable을_생성한다(1, true);
            OrderTableResponse orderTable2 = HTTP_요청을_통해_OrderTable을_생성한다(2, true);
            TableGroupResponse tableGroupResponse = HTTP_요청을_통해_TableGroup을_생성한다(orderTable1, orderTable2);

            // when
            ExtractableResponse<Response> response = RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroupResponse)
                .when().delete(String.format("/api/table-groups/%d", tableGroupResponse.getId()))
                .then().log().all()
                .statusCode(NO_CONTENT.value())
                .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
        }
    }

    private TableGroupRequest TableGroupRequest을_생성한다(OrderTableResponse... orderTables) {
        List<Long> orderTableIds = Arrays.stream(orderTables)
            .map(OrderTableResponse::getId)
            .collect(toList());

        return new TableGroupRequest(orderTableIds);
    }

    private TableGroupResponse HTTP_요청을_통해_TableGroup을_생성한다(OrderTableResponse... orderTables) {
        List<Long> orderTableIds = Arrays.stream(orderTables)
            .map(OrderTableResponse::getId)
            .collect(toList());

        TableGroupRequest request = new TableGroupRequest(orderTableIds);

        return postRequestWithBody("/api/table-groups", request).as(TableGroupResponse.class);
    }

    private OrderTableResponse HTTP_요청을_통해_OrderTable을_생성한다(int numberOfGuests) {
        return HTTP_요청을_통해_OrderTable을_생성한다(numberOfGuests, false);
    }

    private OrderTableResponse HTTP_요청을_통해_OrderTable을_생성한다(int numberOfGuests, boolean isEmpty) {
        OrderTableRequest request = new OrderTableRequest(numberOfGuests, isEmpty);

        return postRequestWithBody("/api/tables", request).as(OrderTableResponse.class);
    }

    private OrderResponse HTTP_요청을_통해_Order를_생성한다(OrderTableResponse orderTable, OrderStatus orderStatus, List<OrderLineItemRequest> orderLineItems) {
        OrderRequest request = new OrderRequest(orderTable.getId(), orderLineItems);

        return postRequestWithBody("/api/orders", request).as(OrderResponse.class);
    }

    private OrderLineItemRequest OrderLineItemRequest를_생성한다(MenuResponse menu, long quantity) {
        return new OrderLineItemRequest(menu.getId(), quantity);
    }

    private MenuResponse HTTP_요청을_통해_Menu를_생성한다(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        MenuRequest request = new MenuRequest(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);

        return postRequestWithBody("/api/menus", request).as(MenuResponse.class);
    }

    private MenuGroup HTTP_요청을_통해_MenuGroup을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup(name);

        return postRequestWithBody("/api/menu-groups", menuGroup).as(MenuGroup.class);
    }

    private MenuProductRequest MenuProductRequest를_생성한다(ProductResponse product, long quantity) {
        return new MenuProductRequest(product.getId(), quantity);
    }

    private ProductResponse HTTP_요청을_통해_Product를_생성한다(String name, int price) {
        ProductRequest request = new ProductRequest(name, BigDecimal.valueOf(price));

        return postRequestWithBody("/api/products", request).as(ProductResponse.class);
    }
}
