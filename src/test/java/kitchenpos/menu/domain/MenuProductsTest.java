package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {

  @Test
  @DisplayName("validateSumLowerThat() : 주어진 가격이 MenuProduct의 총합보다 크면 IllegalArgumentException이 발생할 수 있다.")
  void test_validateSumLowerThat_IllegalArgumentException() throws Exception {
    //given
    final BigDecimal price = BigDecimal.valueOf(41);
    final Product product1 = ProductFixture.createProduct();
    final MenuProducts menuProducts = new MenuProducts(
        List.of(MenuProductFixture.createMenuProduct(product1)));

    //when & then
    assertThatThrownBy(() -> menuProducts.validateSumLowerThan(price))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("validateSumLowerThat() : 주어진 가격이 MenuProduct의 총합보다 작거나 같아야한다.")
  void test_validateSumLowerThat() throws Exception {
    //given
    final BigDecimal price = BigDecimal.valueOf(41);
    final Product product1 = ProductFixture.createProduct();
    final MenuProducts menuProducts = new MenuProducts(
        List.of(MenuProductFixture.createMenuProduct(product1)));

    //when & then
    assertThatThrownBy(() -> menuProducts.validateSumLowerThan(price))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
