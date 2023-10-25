package kitchenpos.ui;

import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_생성_요청;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_응답;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.domain.menu_group.MenuGroup;
import kitchenpos.repositroy.MenuGroupRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupRestControllerTest extends ControllerTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴_그룹을_등록한다() {
        // given
        final MenuGroupCreateRequest menuGroupRequest = 메뉴_그룹_생성_요청("메뉴 그룹");
        final RequestSpecification given = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(menuGroupRequest);

        // when
        final ExtractableResponse<Response> result = given
                .when()
                .post("/api/menu-groups")
                .then().log().all()
                .extract();
        final MenuGroupResponse responseBody = result.as(MenuGroupResponse.class);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result.statusCode()).isEqualTo(201);
            softly.assertThat(responseBody.getId()).isNotNull();
            softly.assertThat(responseBody.getName()).isEqualTo("메뉴 그룹");
        });
    }

    @Test
    void 메뉴_그룹을_조회한다() {
        // given
        final MenuGroup menuGroup1 = menuGroupRepository.save(메뉴_그룹("메뉴 그룹1"));
        final MenuGroup menuGroup2 = menuGroupRepository.save(메뉴_그룹("메뉴 그룹2"));

        // when
        final ExtractableResponse<Response> result = RestAssured
                .given().log().all()
                .when()
                .get("/api/menu-groups")
                .then().log().all()
                .extract();
        List<MenuGroupResponse> responses = result.jsonPath().getList(".", MenuGroupResponse.class);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result.statusCode()).isEqualTo(200);
            softly.assertThat(responses)
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(
                            메뉴_그룹_응답(menuGroup1),
                            메뉴_그룹_응답(menuGroup2))
                    );
        });
    }
}
