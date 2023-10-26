package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuProductsTest {

  @ParameterizedTest
  @ValueSource(longs = {2001, 10000})
  void 가격이_각_메뉴상품_가격의_합보다_크면_예외(Long price) {
    // given
    List<MenuProduct> menuProducts = List.of(
        new MenuProduct(new Product(1L, "치킨", BigDecimal.valueOf(1000)), 1),
        new MenuProduct(new Product(2L, "피자", BigDecimal.valueOf(1000)), 1));

    // when && then
    assertThatThrownBy(() -> MenuProducts.create(menuProducts, BigDecimal.valueOf(price)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("메뉴의 가격은 개별 상품 가격의 합보다 같거나 적어야합니다.");
  }
}
