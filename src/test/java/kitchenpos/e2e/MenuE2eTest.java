package kitchenpos.e2e;

import static kitchenpos.e2e.E2eTest.AssertionPair.row;
import static kitchenpos.support.AssertionsSupport.assertAll;
import static kitchenpos.support.MenuFixture.createMenuRequest;
import static kitchenpos.support.MenuGroupFixture.단짜_두_마리_메뉴;
import static kitchenpos.support.MenuProductFixture.menuProductOne;
import static kitchenpos.support.MenuProductFixture.menuProducts;
import static kitchenpos.support.ProductFixture.간장_치킨;
import static kitchenpos.support.ProductFixture.양념_치킨;
import static kitchenpos.support.ProductFixture.후라이드_치킨;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.support.MenuFixture.WrapMenu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuE2eTest extends KitchenPosE2eTest {

    private Long 메뉴그룹_ID = null;
    private Long 후라이드_상품_ID = null;
    private Long 간장_상품_ID = null;
    private Long 양념_상품_ID = null;

    private Menu 메뉴_요청바디_1;
    private Menu 메뉴_요청바디_2;

    @BeforeEach
    void setUp_of_MenuE2eTest() {

        메뉴그룹_ID = POST_요청(MENU_GROUP_URL, 단짜_두_마리_메뉴).body().as(MenuGroup.class).getId();
        후라이드_상품_ID = POST_요청(PRODUCT_URL, 후라이드_치킨).body().as(Product.class).getId();
        간장_상품_ID = POST_요청(PRODUCT_URL, 간장_치킨).body().as(Product.class).getId();
        양념_상품_ID = POST_요청(PRODUCT_URL, 양념_치킨).body().as(Product.class).getId();

        메뉴_요청바디_1 = createMenuRequest("후라이드 + 후라이드", 19_000, 메뉴그룹_ID, menuProductOne(후라이드_상품_ID, 2L));
        메뉴_요청바디_2 = createMenuRequest("양념 + 간장", 21_000, 메뉴그룹_ID, menuProducts(후라이드_상품_ID, 2, 간장_상품_ID, 1));
    }

    @Test
    void create() {

        // given :: setUp 메서드

        // when
        ExtractableResponse<Response> 응답 = POST_요청(MENU_URL, 메뉴_요청바디_1);
        WrapMenu 저장된_메뉴 = 응답.body().as(WrapMenu.class);
        MenuProduct 저장된_메뉴상품 = 저장된_메뉴.getMenuProducts().get(0);

        // then
        assertAll(
                HTTP_STATUS_검증(HttpStatus.CREATED, 응답),
                NOT_NULL_검증(저장된_메뉴.id()),
                단일_검증(저장된_메뉴.intPrice(), 19_000),
                단일_검증(저장된_메뉴상품.getProductId(), 후라이드_상품_ID),
                단일_검증(저장된_메뉴상품.getQuantity(), 2L)
        );
    }

    @Test
    void list() {

        // given :: 일부 setUp 메서드
        POST_요청(MENU_URL, 메뉴_요청바디_1);
        POST_요청(MENU_URL, 메뉴_요청바디_2);

        // when
        // TODO TypeRef를 이용해서 Mock객체로 불러오기, 가격정보만 Validation해서 들고오는 순간 체크
        ExtractableResponse<Response> 응답 = GET_요청(MENU_URL);

        List<Menu> 메뉴그룹_리스트 = 응답.body().as(new TypeRef<List<Menu>>() {
        }); // TODO Refactor This Method
        List<MenuProduct> 메뉴상품_리스트_1 = 메뉴그룹_리스트.get(0).getMenuProducts();
        List<MenuProduct> 메뉴상품_리스트_2 = 메뉴그룹_리스트.get(1).getMenuProducts();

        // TODO 가격 추가
        assertAll(
                HTTP_STATUS_검증(HttpStatus.OK, 응답),
                () -> assertThat(메뉴상품_리스트_1).hasSize(1),
                () -> assertThat(메뉴상품_리스트_2).hasSize(2),
                리스트_검증(메뉴그룹_리스트,
                        row("name", "후라이드 + 후라이드", "양념 + 간장"),
                        row("menuGroupId", 메뉴그룹_ID.longValue(), 메뉴그룹_ID.longValue()))
        );
    }
}
