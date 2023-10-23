package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.Test;

class MenuGroupIntegrationTest extends IntegrationTest {

    @Test
    void create_success_duplicate_name() {
        // given
        MenuGroup menuGroup = MenuGroupFixture.computeDefaultMenu(ignored -> {});

        // when
        steps.createMenuGroup(menuGroup);
        steps.createMenuGroup(menuGroup);
        ExtractableResponse<Response> response = sharedContext.getResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(201);
    }

    @Test
    void listProducts_success() {
        // when
        List<Product> actual = RestAssured.given().log().all()
                                          .get("/api/products")
                                          .then().log().all()
                                          .extract()
                                          .jsonPath().getList(".", Product.class);

        // then
        assertThat(actual).isEmpty();
    }
}
