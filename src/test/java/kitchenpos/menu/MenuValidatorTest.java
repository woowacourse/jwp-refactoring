package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.InvalidMenuException;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 검증기(MenuValidator) 은(는)")
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class MenuValidatorTest {

    private final MenuGroupRepository menuGroupRepository = mock(MenuGroupRepository.class);
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final MenuValidator menuValidator = new MenuValidator(productRepository);

    @Test
    void 메뉴가_메뉴_그룹에_속하지_않는다면_예외() {
        // given
        given(menuGroupRepository.existsById(1L))
                .willReturn(false);

        // when & then
        assertThatThrownBy(() -> menuValidator.validateCreate(
                BigDecimal.valueOf(1000),
                null,
                List.of(new MenuProduct(1L, 2L))
        )).isInstanceOf(InvalidMenuException.class)
                .hasMessage("메뉴는 메뉴 그룹에 속해야 합니다.");
    }

    @Test
    void 메뉴에_속한_상품이_없으면_예외() {
        // given
        given(menuGroupRepository.existsById(1L))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> menuValidator.validateCreate(
                BigDecimal.valueOf(1000),
                new MenuGroup("메뉴그룹 1"),
                List.of()
        )).isInstanceOf(InvalidMenuException.class)
                .hasMessage("메뉴에는 최소 1개의 상품이 속해야 합니다.");
    }

    @Test
    void 메뉴의_가격이_0원_미만이라면_예외() {
        // given
        given(menuGroupRepository.existsById(1L))
                .willReturn(true);
        given(productRepository.getById(1L))
                .willReturn(new Product(1L, "상품", BigDecimal.valueOf(1000)));

        // when & then
        assertThatThrownBy(() -> menuValidator.validateCreate(
                BigDecimal.valueOf(-1),
                new MenuGroup("메뉴그룹 1"),
                List.of(new MenuProduct(1L, 2L))
        )).isInstanceOf(InvalidMenuException.class)
                .hasMessage("메뉴의 가격은 0원 이상이어야 합니다.");
    }

    @Test
    void 메뉴의_가격이_메뉴에_속한_상품들의_합보다_크면_예외() {
        // given
        given(menuGroupRepository.existsById(1L))
                .willReturn(true);
        given(productRepository.getById(1L))
                .willReturn(new Product(1L, "상품", BigDecimal.valueOf(1000)));

        // when & then
        assertThatThrownBy(() -> menuValidator.validateCreate(
                BigDecimal.valueOf(2001),
                new MenuGroup("메뉴그룹 1"),
                List.of(new MenuProduct(1L, 2L))
        )).isInstanceOf(InvalidMenuException.class)
                .hasMessage("메뉴의 가격은 메뉴에 포함된 상품들의 합 이하여야 합니다.");
    }

    @Test
    void 예외_상황이_없는_경우() {
        // given
        given(menuGroupRepository.existsById(1L))
                .willReturn(true);
        given(productRepository.getById(1L))
                .willReturn(new Product(1L, "상품", BigDecimal.valueOf(1000)));

        // when & then
        assertDoesNotThrow(() -> menuValidator.validateCreate(
                BigDecimal.valueOf(200),
                new MenuGroup("메뉴그룹 1"),
                List.of(new MenuProduct(1L, 2L))
        ));
    }
}
