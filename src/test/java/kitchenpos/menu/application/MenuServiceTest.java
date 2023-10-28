package kitchenpos.menu.application;

import kitchenpos.config.ServiceTestConfig;
import kitchenpos.exception.InvalidMenuPriceException;
import kitchenpos.exception.InvalidPriceValue;
import kitchenpos.exception.NotFoundMenuGroupException;
import kitchenpos.exception.NotFoundProductException;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.ui.dto.MenuProductDto;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.menu.ui.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.assertj.core.api.*;
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

import static org.assertj.core.api.Assertions.assertThat;
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
    private ProductRepository productRepository;

    @Nested
    class 메뉴_등록 {

        private MenuGroup menuGroup;
        private List<Product> products;
        private List<MenuProductDto> menuProductDtos;

        @BeforeEach
        void setUp() {
            menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_엔티티_생성());
            products = productRepository.saveAll(ProductFixture.상품_엔티티들_생성(2));
            menuProductDtos = products.stream()
                                      .map(product -> new MenuProductDto(product.getId(), 1L))
                                      .collect(Collectors.toList());
            menuRepository.save(MenuFixture.메뉴_엔티티_생성(menuGroup, products));
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
                    .isInstanceOf(InvalidPriceValue.class)
                    .hasMessage("상품의 가격은 0 혹은 양수여야 합니다.");
        }

        @Test
        void 가격이_음수라면_예외를_반환한다() {
            // given
            final BigDecimal negative_price = BigDecimal.valueOf(-10_000);
            final MenuRequest menuRequest = new MenuRequest(MenuFixture.메뉴명, negative_price, menuGroup.getId(), menuProductDtos);

            // when & then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(InvalidPriceValue.class)
                    .hasMessage("상품의 가격은 0 혹은 양수여야 합니다.");
        }

        @Test
        void 존재하지_않는_메뉴_그룹이라면_예외를_반환한다() {
            // given
            final Long unsavedMenuGroupId = 999L;
            final MenuRequest menuRequest = new MenuRequest(MenuFixture.메뉴명, BigDecimal.valueOf(10), unsavedMenuGroupId, menuProductDtos);

            // when & then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(NotFoundMenuGroupException.class)
                    .hasMessage("해당 메뉴 그룹이 존재하지 않습니다.");
        }

        @Test
        void 존재하지_않는_상품이라면_예외를_반환한다() {
            // given
            final MenuProductDto unsavedMenuProductDto = new MenuProductDto(999L, 1L);
            final MenuRequest menuRequest = new MenuRequest(MenuFixture.메뉴명, BigDecimal.valueOf(10), menuGroup.getId(), List.of(unsavedMenuProductDto));

            // when & then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(NotFoundProductException.class)
                    .hasMessage("존재하지 않는 상품이 있습니다.");
        }

        @Test
        void 상품들의_값보다_메뉴의_가격이_더_크다면_예외를_반환한다() {
            // given
            final BigDecimal priceMoreThanProductsPrice = BigDecimal.valueOf(Integer.MAX_VALUE);
            final MenuRequest menuRequest = new MenuRequest(MenuFixture.메뉴명, priceMoreThanProductsPrice, menuGroup.getId(), menuProductDtos);

            // when & then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(InvalidMenuPriceException.class)
                    .hasMessage("메뉴 가격이 상품들의 가격 합보다 클 수 없습니다.");
        }
    }

    @Nested
    class 메뉴_목록_조회 {

        private MenuGroup menuGroup;
        private List<Product> products;

        @BeforeEach
        void setUp() {
            menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_엔티티_생성());
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
