package kitchenpos.ui.menu;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import kitchenpos.application.menu.MenuService;
import kitchenpos.application.menu.dto.MenuCreateRequest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.helper.IntegrationTestHelper;
import kitchenpos.ui.menu.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.메뉴_생성_요청;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_10개_생성;
import static kitchenpos.fixture.ProductFixture.상품_생성_10000원;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuRestControllerAcceptanceTestFixture extends IntegrationTestHelper {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    protected MenuGroup menuGroup;
    protected MenuProduct menuProduct;
    protected Product product;

    @BeforeEach
    void initAcceptanceData() {
        menuGroup = menuGroupDao.save(MenuGroupFixture.메뉴_그룹_생성());
        product = productDao.save(상품_생성_10000원());
        menuProduct = 메뉴_상품_10개_생성(null, product.getId());
    }

    protected <T> ExtractableResponse 메뉴를_생성한다(final String url, final T request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post(url)
                .then().log().all()
                .extract();
    }

    protected <T> ExtractableResponse 메뉴를_전체_조회한다(final String url) {
        return RestAssured.given().log().all()
                .when()
                .get(url)
                .then().log().all()
                .extract();
    }

    protected void 메뉴가_성공적으로_생성된다(final ExtractableResponse response, final Menu menu) {
        MenuResponse result = response.as(MenuResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
            softly.assertThat(result.getPrice().longValue()).isEqualTo(menu.getPrice().longValue());
            softly.assertThat(result.getMenuProducts()).hasSize(1);
        });
    }

    protected void 메뉴들이_성공적으로_생성된다(final ExtractableResponse response, final Menu menu) {
        List<MenuResponse> result = response.jsonPath()
                .getList("", MenuResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(1);
            softly.assertThat(result.get(0).getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
            softly.assertThat(result.get(0).getPrice().longValue()).isEqualTo(menu.getPrice().longValue());
            softly.assertThat(result.get(0).getMenuProducts()).hasSize(1);
        });
    }

    protected Menu 메뉴_데이터를_생성한다() {
        Menu menu = new Menu("상품", BigDecimal.valueOf(10000), menuGroup.getId(), List.of(menuProduct));
        MenuCreateRequest req = 메뉴_생성_요청(menu);

        return menuService.create(req);
    }
}
