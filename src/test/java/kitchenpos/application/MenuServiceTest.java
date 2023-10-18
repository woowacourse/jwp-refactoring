package kitchenpos.application;

import kitchenpos.application.menu.MenuService;
import kitchenpos.application.menu.dto.MenuCreateRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.MenuPriceExpensiveThanProductsPriceException;
import kitchenpos.exception.PriceEmptyException;
import kitchenpos.exception.ProductNotFoundException;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.helper.IntegrationTestHelper;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.fixture.MenuFixture.메뉴_생성_요청;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품_10개_생성;
import static kitchenpos.fixture.ProductFixture.상품_생성_10000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends IntegrationTestHelper {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuService menuService;

    private MenuGroup menuGroup;
    private MenuProduct menuProduct;
    private Product product;

    @BeforeEach
    void setup() {
        menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_생성());
        product = productRepository.save(상품_생성_10000원());
        menuProduct = 메뉴_상품_10개_생성(product);
    }

    @Test
    void 메뉴를_생성한다() {
        // given
        MenuCreateRequest req = 메뉴_생성_요청("상품", 10000L, menuGroup, List.of(menuProduct));

        // when
        Menu result = menuService.create(req);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getPrice().longValue()).isEqualTo(req.getPrice().longValue());
            softly.assertThat(result.getMenuProducts()).hasSize(1);
        });
    }

    @Test
    void 가격이_0원_이하면_예외를_발생시킨다() {
        // given
        MenuCreateRequest req = 메뉴_생성_요청("상품", -1L, menuGroup, List.of(menuProduct));

        // when & then
        assertThatThrownBy(() -> menuService.create(req))
                .isInstanceOf(PriceEmptyException.class);
    }


    @Test
    void 메뉴_그룹이_존재하지_않으면_예외를_발생시킨다() {
        // given
        MenuGroup wrong = new MenuGroup(-1L, "그룹");
        MenuCreateRequest req = 메뉴_생성_요청("상품", 1000L, wrong, List.of(menuProduct));

        // when & then
        assertThatThrownBy(() -> menuService.create(req))
                .isInstanceOf(MenuGroupNotFoundException.class);
    }

    @Test
    void 상품이_존재하지_않는데_메뉴에_있다면_예외를_발생시킨다() {
        // given
        Product wrong = new Product(-1L, "상품", 10L);
        menuProduct = 메뉴_상품_10개_생성(wrong);

        MenuCreateRequest req = 메뉴_생성_요청("상품", 100L, menuGroup, List.of(menuProduct));

        // when & then
        assertThatThrownBy(() -> menuService.create(req))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void 메뉴_가격이_메뉴_상품_가격의_합보다_크면_예외를_발생시킨다() {
        // given
        Product wrong = new Product(1L, "상품", 10L);
        menuProduct = 메뉴_상품_10개_생성(wrong);
        MenuCreateRequest req = 메뉴_생성_요청("상품", 1000000000L, menuGroup, List.of(menuProduct));

        // when & then
        assertThatThrownBy(() -> menuService.create(req))
                .isInstanceOf(MenuPriceExpensiveThanProductsPriceException.class);
    }

    @Test
    void 모든_메뉴들을_조회한다() {
        MenuCreateRequest req = 메뉴_생성_요청("상품", 10000L, menuGroup, List.of(menuProduct));
        menuService.create(req);

        // when
        List<Menu> result = menuService.list();

        // then
        assertThat(result).hasSize(1);
    }
}
