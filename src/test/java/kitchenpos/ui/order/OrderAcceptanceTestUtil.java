package kitchenpos.ui.order;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.menu.MenuGroupService;
import kitchenpos.application.menu.MenuService;
import kitchenpos.application.menu.request.MenuCreateRequest;
import kitchenpos.application.menu.request.MenuGroupCreateRequest;
import kitchenpos.application.menu.request.MenuProductRequest;
import kitchenpos.application.table.TableService;
import kitchenpos.application.order.request.OrderCreateRequest;
import kitchenpos.application.order.request.OrderLineItemCreateRequest;
import kitchenpos.application.order.request.OrderUpdateRequest;
import kitchenpos.application.order.request.TableCreateRequest;
import kitchenpos.application.product.ProductService;
import kitchenpos.application.product.request.ProductCreateRequest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.ControllerAcceptanceTestHelper;
import kitchenpos.ui.order.response.OrderResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class OrderAcceptanceTestUtil extends ControllerAcceptanceTestHelper {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private TableService tableService;

    protected OrderCreateRequest 주문_생성_요청() {
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("커피");
        MenuGroup menuGroup = menuGroupService.create(menuGroupCreateRequest);

        ProductCreateRequest productCreateRequest = new ProductCreateRequest("후라이드", 16000L);
        Product product = productService.create(productCreateRequest);
        MenuProductRequest menuProduct = new MenuProductRequest(product.getId(), 1L);

        MenuCreateRequest menuRequest = new MenuCreateRequest("이달의 커피", 16000L, menuGroup.getId(), List.of(menuProduct));
        Menu menu = menuService.create(menuRequest);

        TableCreateRequest tableCreateRequest = new TableCreateRequest(1, false);
        OrderTable orderTable = tableService.create(tableCreateRequest);

        OrderLineItemCreateRequest orderLineItemRequest = new OrderLineItemCreateRequest(menu.getId(), 1L);
        return new OrderCreateRequest(orderTable.getId(), List.of(orderLineItemRequest));
    }

    protected ExtractableResponse<Response> 주문을_생성한다(OrderCreateRequest 요청) {
        return RestAssured.given().body(요청)
                .contentType(ContentType.JSON)
                .when().post("/api/orders")
                .then()
                .extract();
    }

    protected void 주문이_생성됨(OrderCreateRequest 요청, ExtractableResponse<Response> 응답) {
        OrderResponse response = 응답.as(OrderResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(응답.statusCode()).isEqualTo(201);
            softly.assertThat(response.getId()).isNotNull();
            softly.assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            softly.assertThat(response.getOrderTableId()).isEqualTo(요청.getOrderTableId());
            softly.assertThat(response.getOrderLineItems()).hasSize(1);
        });
    }

    protected ExtractableResponse<Response> 주문목록을_조회한다() {
        return RestAssured.given()
                .when().get("/api/orders")
                .then()
                .extract();
    }

    protected void 주문목록이_조회됨(ExtractableResponse<Response> 응답) {
        List<OrderResponse> responses = 응답.jsonPath().getList(".", OrderResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(응답.statusCode()).isEqualTo(200);
            softly.assertThat(responses).hasSize(1);
        });
    }

    protected Long 주문id_조회(ExtractableResponse<Response> 응답) {
        return 응답.as(OrderResponse.class).getId();
    }

    protected OrderUpdateRequest 주문상태_변경_요청() {
        return new OrderUpdateRequest(OrderStatus.MEAL.name());
    }

    protected ExtractableResponse<Response> 주문_상태를_변경한다(Long orderId, OrderUpdateRequest 변경요청) {
        return RestAssured.given().body(변경요청)
                .contentType(ContentType.JSON)
                .when().put("/api/orders/{orderId}/order-status", orderId)
                .then()
                .extract();
    }

    protected void 주문_상태가_변경됨(OrderUpdateRequest 요청, ExtractableResponse<Response> 응답) {
        OrderResponse response = 응답.as(OrderResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(응답.statusCode()).isEqualTo(200);
            softly.assertThat(response.getOrderStatus()).isEqualTo(요청.getOrderStatus());
        });
    }
}
