package kitchenpos.domain.menu;

import kitchenpos.domain.common.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @InjectMocks
    private MenuValidator menuValidator;

    @Mock
    private ProductRepository productRepository;

    @Test
    void 저장된_상품과_요청한_상품의_개수가_다르면_실패한다() {
        // given
        final Price overSumOfProductPrice = new Price(BigDecimal.valueOf(17_000));
        final Menu menu = new Menu("우동세트", overSumOfProductPrice, new MenuGroup(), new MenuProducts());

        final MenuProducts toAddMenuProducts = new MenuProducts();
        final Long productId = 1L;
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(productId, 3));
        toAddMenuProducts.addAll(menuProducts);

        given(productRepository.findAllById(anyList())).willReturn(List.of());

        // when. then
        assertThatThrownBy(() -> menuValidator.validatePrice(menu, menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {18_001, 20_000})
    void 메뉴_가격이_총_합산_가격보다_크면_메뉴를_추가할_수_없다(final int price) {
        // given
        final Price overSumOfProductPrice = new Price(BigDecimal.valueOf(price));
        final Menu menu = new Menu("우동세트", overSumOfProductPrice, new MenuGroup(), new MenuProducts());

        final MenuProducts toAddMenuProducts = new MenuProducts();
        final Long productId = 1L;
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(productId, 3));
        toAddMenuProducts.addAll(menuProducts);

        given(productRepository.findAllById(List.of(productId))).willReturn(List.of(new Product("국수", new Price(BigDecimal.valueOf(6000)))));

        // when. then
        assertThatThrownBy(() -> menuValidator.validatePrice(menu, menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {15_000, 17_999})
    void 메뉴_가격이_총_합산_가격보다_작으면_메뉴를_추가할_수_있다(final int price) {
        // given
        final Price overSumOfProductPrice = new Price(BigDecimal.valueOf(price));
        final Menu menu = new Menu("우동세트", overSumOfProductPrice, new MenuGroup(), new MenuProducts());

        final MenuProducts toAddMenuProducts = new MenuProducts();
        final Long productId = 1L;
        final List<MenuProduct> menuProducts = List.of(new MenuProduct(productId, 3));
        toAddMenuProducts.addAll(menuProducts);

        given(productRepository.findAllById(List.of(productId))).willReturn(List.of(new Product("국수", new Price(BigDecimal.valueOf(6000)))));

        // when. then
        assertThatCode(() -> menuValidator.validatePrice(menu, menuProducts))
                .doesNotThrowAnyException();
    }
}
