package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MenuProductRepository menuProductRepository;

    @InjectMocks
    private MenuService menuService;

    @Nested
    class 메뉴_등록 {

        @Test
        void 메뉴를_등록할_수_있다() {
            // given
            final var menu = MenuFixture.메뉴_망고치킨_17000원_신메뉴();
            given(menuRepository.save(any()))
                    .willReturn(menu);
            given(menuGroupRepository.existsById(anyLong()))
                    .willReturn(true);

            final var product1 = ProductFixture.상품_망고_1000원();
            final var product2 = ProductFixture.상품_치킨_15000원();
            given(productRepository.findById(product1.getId()))
                    .willReturn(Optional.of(product1));
            given(productRepository.findById(product2.getId()))
                    .willReturn(Optional.of(product2));

            final var menuProduct1 = MenuProductFixture.메뉴상품_망고_2개();
            final var menuProduct2 = MenuProductFixture.메뉴상품_치킨_1개();
            given(menuProductRepository.save(any()))
                    .willReturn(menuProduct1, menuProduct2);

            // when
            final var actual = menuService.create(menu);

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(menu);
        }

        @Test
        void 메뉴의_가격이_0보다_작으면_예외가_발생한다() {
            // given
            final var menu = MenuFixture.메뉴_망고치킨_N원_신메뉴(-1);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
            // given
            final var menu = MenuFixture.메뉴_망고치킨_17000원_신메뉴();
            given(menuGroupRepository.existsById(anyLong()))
                    .willReturn(false);

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_상품이_존재하지_않으면_예외가_발생한다() {
            // given
            final var menu = MenuFixture.메뉴_망고치킨_17000원_신메뉴();
            given(menuGroupRepository.existsById(anyLong()))
                    .willReturn(true);
            given(productRepository.findById(anyLong()))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_가격과_메뉴_상품의_총_가격이_다르면_예외가_발생한다() {
            // given
            final var menu = MenuFixture.메뉴_망고치킨_17000원_신메뉴();
            given(menuGroupRepository.existsById(anyLong()))
                    .willReturn(true);

            final var invalidProduct = ProductFixture.상품_망고_N원(0);
            given(productRepository.findById(anyLong()))
                    .willReturn(Optional.of(invalidProduct));

            // when & then
            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 메뉴_목록_조회 {

        @Test
        void 메뉴_목록을_조회할_수_있다() {
            // given
            final var menus = Collections.singletonList(MenuFixture.메뉴_망고치킨_17000원_신메뉴());
            given(menuRepository.findAll())
                    .willReturn(menus);

            // when
            final var actual = menuService.list();

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(menus);
        }
    }
}
