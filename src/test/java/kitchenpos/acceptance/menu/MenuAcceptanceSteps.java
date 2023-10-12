package kitchenpos.acceptance.menu;

import static kitchenpos.acceptance.AcceptanceSteps.given;
import static kitchenpos.acceptance.AcceptanceSteps.생성된_ID를_추출한다;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@SuppressWarnings("NonAsciiCharacters")
public class MenuAcceptanceSteps {

    public static Long 메뉴_등록후_생성된_ID를_받아온다(
            Long 메뉴_그룹_ID,
            String 메뉴_이름,
            int 메뉴_가격,
            MenuProduct... 상품들
    ) {
        return 생성된_ID를_추출한다(메뉴_등록_요청을_보낸다(메뉴_그룹_ID, 메뉴_이름, 메뉴_가격, 상품들));
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청을_보낸다(
            Long 메뉴_그룹_ID,
            String 메뉴_이름,
            int 메뉴_가격,
            MenuProduct... 상품들
    ) {
        Menu menu = 메뉴(메뉴_그룹_ID, 메뉴_이름, 메뉴_가격, 상품들);
        return given()
                .body(menu)
                .post("/api/menus")
                .then()
                .log().all()
                .extract();
    }

    public static Menu 메뉴(Long 메뉴_그룹_ID, String 메뉴_이름, int 메뉴_가격, MenuProduct... 상품들) {
        Menu menu = new Menu();
        menu.setMenuGroupId(메뉴_그룹_ID);
        menu.setName(메뉴_이름);
        menu.setPrice(new BigDecimal(메뉴_가격));
        menu.setMenuProducts(Arrays.asList(상품들));
        return menu;
    }

    public static MenuProduct 메뉴에_속한_상품(Long 상품_ID, int 수량) {
        MenuProduct menuProduct1 = new MenuProduct();
        menuProduct1.setProductId(상품_ID);
        menuProduct1.setQuantity(수량);
        return menuProduct1;
    }

    public static ExtractableResponse<Response> 메뉴_조회_요청을_보낸다() {
        return given()
                .get("/api/menus")
                .then()
                .log().headers()
                .extract();
    }
}
