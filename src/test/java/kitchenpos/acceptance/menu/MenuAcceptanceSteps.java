package kitchenpos.acceptance.menu;

import static kitchenpos.acceptance.AcceptanceSteps.given;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.assertj.core.data.Percentage;

@SuppressWarnings("NonAsciiCharacters")
public class MenuAcceptanceSteps {

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

    public static List<Menu> 메뉴_조회_요청을_보낸다() {
        var extract = given()
                .get("/api/menus")
                .then()
                .log().all()
                .extract();
        return extract.as(new TypeRef<>() {
        });
    }

    public static Menu 메뉴_정보(Long 메뉴_그룹_ID, String 메뉴_이름, double 메뉴_가격, MenuProduct... 상품들) {
        Menu menu = new Menu();
        menu.setMenuGroupId(메뉴_그룹_ID);
        menu.setName(메뉴_이름);
        menu.setPrice(new BigDecimal(메뉴_가격));
        menu.setMenuProducts(Arrays.asList(상품들));
        return menu;
    }

    public static void 메뉴_조회_요청_결과를_검증한다(
            List<Menu> 응답,
            List<Menu> 예상
    ) {
        assertThat(응답)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .ignoringFields("price")
                .isEqualTo(예상);
        for (int i = 0; i < 응답.size(); i++) {
            assertThat(응답.get(i).getPrice())
                    .isCloseTo(예상.get(i).getPrice(), Percentage.withPercentage(100));
        }
    }
}
