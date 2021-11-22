package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuGroupAcceptanceTest.MENU_GROUP_생성_요청;
import static kitchenpos.acceptance.MenuGroupAcceptanceTest.튀김류;
import static kitchenpos.acceptance.ProductAcceptanceTest.PRODUCT_생성_요청;
import static kitchenpos.acceptance.ProductAcceptanceTest.강정치킨;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.acceptance.util.LocationParser;
import kitchenpos.menu.service.MenuProductRequest;
import kitchenpos.menu.service.MenuRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuAcceptanceTest extends AcceptanceTest {
    private static MenuRequest 더블_강정치킨;

    public static ExtractableResponse<Response> MENU_생성_요청(MenuRequest menuRequest) {
        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuRequest)
                .when()
                .post("/api/menus")
                .then()
                .extract();
    }

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        Long productId = LocationParser.extractCreatedId(PRODUCT_생성_요청(강정치킨));
        Long menuGroupId = LocationParser.extractCreatedId(MENU_GROUP_생성_요청(튀김류));
        더블_강정치킨 = new MenuRequest("더블_강정치킨", BigDecimal.valueOf(25000), menuGroupId,
                Collections.singletonList(new MenuProductRequest(productId, 2L)));
    }

    @Test
    void save() {
        ExtractableResponse<Response> response = MENU_생성_요청(더블_강정치킨);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
