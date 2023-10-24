package kitchenpos.application;

import kitchenpos.common.ServiceTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.menu.MenuProductDto;
import kitchenpos.ui.dto.menu.MenuRequest;
import kitchenpos.ui.dto.menu.MenuResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("메뉴 서비스 테스트")
class MenuServiceTest extends ServiceTestConfig {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    class 메뉴_등록 {

        private MenuGroup menuGroup;
        private List<Product> products;
        private Menu menu;
        private List<MenuProduct> menuProducts;
        private List<MenuProductDto> menuProductDtos;

        @BeforeEach
        void setUp() {
            menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_생성());
            products = productRepository.saveAll(ProductFixture.상품_엔티티들_생성(2));
            menu = menuRepository.save(MenuFixture.메뉴_엔티티_생성(menuGroup, products));
            final MenuProduct menuProduct1 = menuProductRepository.save(MenuProductFixture.메뉴_상품_생성(products.get(0), menu));
            final MenuProduct menuProduct2 = menuProductRepository.save(MenuProductFixture.메뉴_상품_생성(products.get(1), menu));
            menuProducts = List.of(menuProduct1, menuProduct2);
            menuProductDtos = products.stream()
                                      .map(product -> new MenuProductDto(product.getId(), 1L))
                                      .collect(Collectors.toList());
        }

        @Test
        void 메뉴를_등록한다() {
            // given
            final MenuRequest menuRequest = MenuFixture.메뉴_요청_dto_생성(menuGroup, products);

            // when
            final MenuResponse actual = menuService.create(menuRequest);

            // then
            assertThat(actual.getId()).isPositive();
        }

        @ParameterizedTest
        @NullSource
        void 가격이_null이라면_예외를_반환한다(BigDecimal price) {
            // given
            final MenuRequest menuRequest = new MenuRequest(MenuFixture.메뉴명, price, menuGroup.getId(), menuProductDtos);

            // when & then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 가격이_음수라면_예외를_반환한다() {
            // given
            final BigDecimal negative_price = BigDecimal.valueOf(-10_000);
            final MenuRequest menuRequest = new MenuRequest(MenuFixture.메뉴명, negative_price, menuGroup.getId(), menuProductDtos);

            // when & then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_메뉴_그룹이라면_예외를_반환한다() {
            // given
            final Long unsavedMenuGroupId = 999L;
            final MenuRequest menuRequest = new MenuRequest(MenuFixture.메뉴명, BigDecimal.valueOf(10), unsavedMenuGroupId, menuProductDtos);

            // when & then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_상품이라면_예외를_반환한다() {
            // given
            final MenuProductDto unsavedMenuProductDto = new MenuProductDto(999L, 1L);
            final MenuRequest menuRequest = new MenuRequest(MenuFixture.메뉴명, BigDecimal.valueOf(10), menuGroup.getId(), List.of(unsavedMenuProductDto));

            // when & then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품들의_값보다_메뉴의_가격이_더_크다면_예외를_반환한다() {
            // given
            final BigDecimal priceMoreThanProductsPrice = BigDecimal.valueOf(Integer.MAX_VALUE);
            final MenuRequest menuRequest = new MenuRequest(MenuFixture.메뉴명, priceMoreThanProductsPrice, menuGroup.getId(), menuProductDtos);

            // when & then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 메뉴_목록_조회 {

        private MenuGroup menuGroup;
        private List<Product> products;

        @BeforeEach
        void setUp() {
            menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_생성());
            products = productRepository.saveAll(ProductFixture.상품_엔티티들_생성(2));
        }

        @Test
        void 메뉴_목록을_조회한다() {
            // given
            final List<Menu> menus = menuRepository.saveAll(MenuFixture.메뉴_엔티티들_생성(3, menuGroup, products));

            // when
            final List<MenuResponse> actual = menuService.list();

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual).hasSize(3);
                softAssertions.assertThat(actual.get(0).getId()).isEqualTo(menus.get(0).getId());
                softAssertions.assertThat(actual.get(1).getId()).isEqualTo(menus.get(1).getId());
                softAssertions.assertThat(actual.get(2).getId()).isEqualTo(menus.get(2).getId());
            });
        }
    }
}
