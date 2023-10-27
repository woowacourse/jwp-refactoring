package kitchenpos.ui.menu;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.MenuGroupService;
import kitchenpos.menu.request.MenuCreateRequest;
import kitchenpos.menugroup.request.MenuGroupCreateRequest;
import kitchenpos.menu.request.MenuProductRequest;
import kitchenpos.product.ProductService;
import kitchenpos.product.request.ProductCreateRequest;
import kitchenpos.menugroup.MenuGroup;
import kitchenpos.product.Product;
import kitchenpos.ui.ControllerAcceptanceTestHelper;
import kitchenpos.ui.menu.response.MenuResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class MenuAcceptanceTestUtil extends ControllerAcceptanceTestHelper {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    protected MenuCreateRequest 메뉴_생성_요청(){
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("치킨");
        MenuGroup menuGroup = menuGroupService.create(menuGroupCreateRequest);

        ProductCreateRequest productCreateRequest = new ProductCreateRequest("후라이드", 16000L);
        Product product = productService.create(productCreateRequest);
        MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 1L);

        return new MenuCreateRequest("후라이드", 16000L, menuGroup.getId(), List.of(menuProductRequest));
    }

    protected ExtractableResponse<Response> 메뉴를_생성한다(MenuCreateRequest menuCreateRequest) {
        return RestAssured.given().body(menuCreateRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/menus")
                .then()
                .extract();
    }

    protected void 메뉴가_생성됨(MenuCreateRequest menuCreateRequest, ExtractableResponse<Response> response1) {
        MenuResponse menuResponse = response1.as(MenuResponse.class);

        SoftAssertions.assertSoftly(softly->{
            softly.assertThat(response1.statusCode()).isEqualTo(201);
            softly.assertThat(menuResponse.getId()).isNotNull();
            softly.assertThat(menuResponse.getName()).isEqualTo(menuCreateRequest.getName());
            softly.assertThat(menuResponse.getPrice()).isEqualByComparingTo(menuCreateRequest.getPrice());
            softly.assertThat(menuResponse.getMenuGroupId()).isEqualTo(menuCreateRequest.getMenuGroupId());
        });
    }

    protected ExtractableResponse<Response> 메뉴_목록을_조회한다() {
        return RestAssured.given()
                .when().get("/api/menus")
                .then()
                .extract();
    }

    protected void 메뉴_목록이_조회됨(ExtractableResponse<Response> 응답) {
        List<MenuResponse> responses = 응답.jsonPath().getList(".", MenuResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(응답.statusCode()).isEqualTo(200);
            softly.assertThat(responses).hasSize(1);
        });
    }
}
