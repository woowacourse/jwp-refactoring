package kitchenpos.steps;

import static kitchenpos.TestAid.toJson;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntegrationTestSteps {

    private static final String MENU_GROUP_API = "/api/menu-groups";
    private static final String MENU_API = "/api/menus";
    private static final String PRODUCT_API = "/api/products";

    @Autowired
    SharedContext sharedContext;

    public void createMenuGroup(MenuGroup menuGroup) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .contentType(ContentType.JSON)
                                                            .body(toJson(menuGroup))
                                                            .when()
                                                            .post(MENU_GROUP_API)
                                                            .then().log().all()
                                                            .extract();

        sharedContext.setResponse(response);
    }

    public void createMenu(Menu menu) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .contentType(ContentType.JSON)
                                                            .body(menu)
                                                            .when()
                                                            .post(MENU_API)
                                                            .then().log().all()
                                                            .extract();

        sharedContext.setResponse(response);
    }

    public void createProduct(Product product) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                                                            .contentType(ContentType.JSON)
                                                            .body(product)
                                                            .when()
                                                            .post(PRODUCT_API)
                                                            .then().log().all()
                                                            .extract();

        sharedContext.setResponse(response);
    }
}

