package kitchenpos.acceptance.menu;

import static kitchenpos.acceptance.AcceptanceSteps.given;
import static kitchenpos.acceptance.AcceptanceSteps.생성된_ID를_추출한다;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupAcceptanceSteps {

    public static Long 메뉴_그릅_등록후_생성된_ID를_가져온다(String 메뉴_그룹_이름) {
        return 생성된_ID를_추출한다(메뉴_그릅_등록_요청을_보낸다(메뉴_그룹_이름));
    }

    public static ExtractableResponse<Response> 메뉴_그릅_등록_요청을_보낸다(String 메뉴_그룹_이름) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(메뉴_그룹_이름);
        return given()
                .body(menuGroup)
                .post("/api/menu-groups")
                .then()
                .log().all()
                .extract();
    }

    public static void 메뉴_그룹_조회_결과를_검증한다(List<MenuGroup> 응답, List<MenuGroup> 예상) {
        assertThat(응답)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(예상);
    }

    public static MenuGroup 메뉴_그룹_정보(String 메뉴_그룹_이름) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(메뉴_그룹_이름);
        return menuGroup;
    }

    public static List<MenuGroup> 메뉴_그룹_조회_요청을_보낸다() {
        ExtractableResponse<Response> extract = given()
                .get("/api/menu-groups")
                .then().log().all()
                .extract();
        return extract.as(new TypeRef<>() {
        });
    }
}
