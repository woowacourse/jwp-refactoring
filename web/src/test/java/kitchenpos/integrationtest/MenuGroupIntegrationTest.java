package kitchenpos.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.menugroup.service.MenuGroupDto;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import kitchenpos.product.service.ProductDto;

class MenuGroupIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("이름이 중복인 메뉴 그룹을 등록할 수 있다.")
    void create_success_duplicate_name() {
        // given
        MenuGroupDto menuGroupDto = MenuGroupFixture.computeDefaultMenuDto(ignored -> {});

        // when
        steps.createMenuGroup(menuGroupDto);
        steps.createMenuGroup(menuGroupDto);
        ExtractableResponse<Response> response = sharedContext.getResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(201);
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    void listProducts_success() {
        // when
        List<ProductDto> actual = RestAssured.given().log().all()
                                             .get("/api/products")
                                             .then().log().all()
                                             .extract()
                                             .jsonPath().getList(".", ProductDto.class);

        // then
        assertThat(actual).isEmpty();
    }
}
