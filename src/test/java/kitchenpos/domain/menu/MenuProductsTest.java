package kitchenpos.domain.menu;

import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("메뉴에 속한 상품들 테스트")
class MenuProductsTest {

    @Test
    void 메뉴에_속한_상품들의_가격_총합을_제대로_계산하는지_확인한다() {
        final MenuProduct 가격_45_상품 = MenuProduct.create(new Product(1L, Name.create("name"), Price.create(new BigDecimal(15))), 3);
        final MenuProduct 가격_0_상품 = MenuProduct.create(new Product(1L, Name.create("name"), Price.create(new BigDecimal(15))), 0);
        final MenuProduct 가격_70_상품 = MenuProduct.create(new Product(1L, Name.create("name"), Price.create(new BigDecimal(10))), 7);

        final MenuProducts menuProducts = MenuProducts.create(List.of(가격_45_상품, 가격_0_상품, 가격_70_상품));

        assertThat(menuProducts.getPrice()).isEqualByComparingTo(new BigDecimal(115));
    }
}
