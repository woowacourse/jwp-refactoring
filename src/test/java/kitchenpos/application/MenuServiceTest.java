package kitchenpos.application;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuFixture.메뉴_생성_요청;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_10개_생성;
import static kitchenpos.fixture.ProductFixture.상품_생성_10000원;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends IntegrationTestHelper {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuService menuService;

    private MenuGroup menuGroup;
    private MenuProduct menuProduct;
    private Product product;

    @BeforeEach
    void setup() {
        menuGroup = menuGroupDao.save(MenuGroupFixture.메뉴_그룹_생성());
        product = productDao.save(상품_생성_10000원());
        menuProduct = 메뉴_상품_10개_생성(null, product.getId());
    }

    @Test
    void 메뉴를_생성한다() {
        // given
        Menu menu = 메뉴_생성("상품", BigDecimal.valueOf(10000), menuGroup.getId(), List.of(menuProduct));
        MenuCreateRequest req = 메뉴_생성_요청(menu);

        // when
        Menu result = menuService.create(req);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
            softly.assertThat(result.getPrice().longValue()).isEqualTo(menu.getPrice().longValue());
            softly.assertThat(result.getMenuProducts()).hasSize(1);
        });
    }

    @Test
    void 가격이_0원_이하면_예외를_발생시킨다() {
        // given
        Menu menu = 메뉴_생성("상품", BigDecimal.valueOf(-1), menuGroup.getId(), List.of(menuProduct));
        MenuCreateRequest req = 메뉴_생성_요청(menu);

        // when & then
        assertThatThrownBy(() -> menuService.create(req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹이_존재하지_않으면_예외를_발생시킨다() {
        // given
        Long invalidGroupId = -1L;
        Menu menu = 메뉴_생성("상품", BigDecimal.valueOf(1000), invalidGroupId, List.of(menuProduct));
        MenuCreateRequest req = 메뉴_생성_요청(menu);

        // when & then
        assertThatThrownBy(() -> menuService.create(req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품이_존재하지_않는데_메뉴에_있다면_예외를_발생시킨다() {
        // given
        Long invalidProductId = -1L;
        menuProduct = 메뉴_상품_10개_생성(null, invalidProductId);

        Menu menu = 메뉴_생성("상품", BigDecimal.valueOf(100), menuGroup.getId(), List.of(menuProduct));
        MenuCreateRequest req = 메뉴_생성_요청(menu);

        // when & then
        assertThatThrownBy(() -> menuService.create(req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_메뉴_상품_가격의_합보다_크면_예외를_발생시킨다() {
        // given
        menuProduct = 메뉴_상품_10개_생성(null, 1L);
        Menu menu = 메뉴_생성("상품", BigDecimal.valueOf(1000000000), menuGroup.getId(), List.of(menuProduct));
        MenuCreateRequest req = 메뉴_생성_요청(menu);

        // when & then
        assertThatThrownBy(() -> menuService.create(req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_메뉴들을_조회한다() {
        Menu menu = 메뉴_생성("상품", BigDecimal.valueOf(10000), menuGroup.getId(), List.of(menuProduct));
        MenuCreateRequest req = 메뉴_생성_요청(menu);
        Menu savedMenu = menuService.create(req);

        // when
        List<Menu> result = menuService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).hasSize(1);
            softly.assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(savedMenu);
        });
    }
}
