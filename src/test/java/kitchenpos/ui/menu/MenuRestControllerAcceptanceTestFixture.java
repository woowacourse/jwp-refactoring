package kitchenpos.ui.menu;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import kitchenpos.application.menu.MenuService;
import kitchenpos.application.menu.dto.MenuCreateRequest;
import kitchenpos.application.menu.dto.MenuProductCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.helper.IntegrationTestHelper;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.menu.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.fixture.MenuFixture.메뉴_생성_요청;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_10개_생성;
import static kitchenpos.fixture.ProductFixture.상품_생성_10000원;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
class MenuRestControllerAcceptanceTestFixture extends IntegrationTestHelper {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    protected MenuGroup menuGroup;
    protected MenuProduct menuProduct;
    protected Product product;
    protected Menu menu;

    @BeforeEach
    void initAcceptanceData() {
        menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_생성());
        product = productRepository.save(상품_생성_10000원());
        MenuCreateRequest req = new MenuCreateRequest("메뉴", 10000L, menuGroup.getId(), List.of(
                new MenuProductCreateRequest(product.getId(), 1)
        ));
        menu = menuService.create(req);
        menuProduct = menuProductRepository.save(메뉴_상품_10개_생성(product));
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

    protected ExtractableResponse 메뉴를_전체_조회한다(final String url) {
        return RestAssured.given().log().all()
                .when()
                .get(url)
                .then().log().all()
                .extract();
    }

    protected void 메뉴가_성공적으로_생성된다(final ExtractableResponse response, final MenuCreateRequest request) {
        MenuResponse result = response.as(MenuResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result.getMenuGroupId()).isEqualTo(request.getMenuGroupId());
            softly.assertThat(result.getPrice().longValue()).isEqualTo(request.getPrice().longValue());
            softly.assertThat(result.getMenuProducts()).hasSize(1);
        });
    }

    protected void 메뉴들이_성공적으로_생성된다(final ExtractableResponse response, final Menu menu) {
        List<MenuResponse> result = response.jsonPath()
                .getList("", MenuResponse.class);

        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(2);
            softly.assertThat(result.get(0).getMenuGroupId()).isEqualTo(menu.getMenuGroup().getId());
            softly.assertThat(result.get(0).getPrice().longValue()).isEqualTo(menu.getPrice().longValue());
            softly.assertThat(result.get(0).getMenuProducts()).hasSize(1);
        });
    }

    protected Menu 메뉴_데이터를_생성한다() {
        MenuCreateRequest req = 메뉴_생성_요청("상품", 10000L, menuGroup, List.of(menuProduct));

        return menuService.create(req);
    }
}
