package kitchenpos.acceptance.menu;

import static kitchenpos.acceptance.AcceptanceSteps.given;
import static kitchenpos.acceptance.AcceptanceSteps.생성된_ID를_추출한다;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuCreateRequest.MenuProductInfo;

@SuppressWarnings("NonAsciiCharacters")
public class MenuAcceptanceSteps {

    public static Long 메뉴_등록후_생성된_ID를_받아온다(
            Long 메뉴_그룹_ID,
            String 메뉴_이름,
            int 메뉴_가격,
            MenuProductInfo... 상품들
    ) {
        return 생성된_ID를_추출한다(메뉴_등록_요청을_보낸다(메뉴_그룹_ID, 메뉴_이름, 메뉴_가격, 상품들));
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청을_보낸다(
            Long 메뉴_그룹_ID,
            String 메뉴_이름,
            int 메뉴_가격,
            MenuProductInfo... 상품들
    ) {
        MenuCreateRequest request = 메뉴(메뉴_그룹_ID, 메뉴_이름, 메뉴_가격, 상품들);
        return given()
                .body(request)
                .post("/api/menus")
                .then()
                .log().all()
                .extract();
    }

    public static MenuCreateRequest 메뉴(Long 메뉴_그룹_ID, String 메뉴_이름, int 메뉴_가격, MenuProductInfo... 상품들) {
        return new MenuCreateRequest(메뉴_이름, new BigDecimal(메뉴_가격), 메뉴_그룹_ID, Arrays.asList(상품들));
    }

    public static MenuProductInfo 메뉴에_속한_상품(Long 상품_ID, long 수량) {
        return new MenuProductInfo(상품_ID, 수량);
    }

    public static ExtractableResponse<Response> 메뉴_조회_요청을_보낸다() {
        return given()
                .get("/api/menus")
                .then()
                .log().headers()
                .extract();
    }
}
