package com.kitchenpos.ui.menugroup;

import com.kitchenpos.domain.MenuGroup;
import com.kitchenpos.domain.MenuGroupRepository;
import com.kitchenpos.helper.IntegrationTestHelper;
import com.kitchenpos.ui.dto.MenuGroupResponse;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
class MenuGroupRestControllerAcceptanceTestFixture extends IntegrationTestHelper {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    protected <T> ExtractableResponse 메뉴_그룹을_생성한다(final String url, final T request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    protected ExtractableResponse 메뉴_그룹을_전체_조회한다(final String url) {
        return RestAssured.given().log().all()
                .when()
                .get(url)
                .then().log().all()
                .extract();
    }

    protected void 메뉴_그룹이_성공적으로_생성된다(final ExtractableResponse response, final MenuGroup menuGroup) {
        MenuGroup result = response.as(MenuGroup.class);

        assertThat(result.getName()).isEqualTo(menuGroup.getName());
    }

    protected void 메뉴_그룹들이_성공적으로_생성된다(final ExtractableResponse response, final MenuGroup menuGroup) {
        List<MenuGroupResponse> result = response.jsonPath()
                .getList("", MenuGroupResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(1);
            softly.assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(menuGroup);
        });
    }

    protected MenuGroup 메뉴_그룹_데이터_생성() {
        return menuGroupRepository.save(메뉴_그룹_생성());
    }
}
