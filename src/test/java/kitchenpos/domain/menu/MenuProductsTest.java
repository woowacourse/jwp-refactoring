package kitchenpos.domain.menu;

import kitchenpos.domain.product.Name;
import kitchenpos.domain.product.Price;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("메뉴에 속한 상품들 테스트")
class MenuProductsTest {

    @Test
    void 메뉴에_속한_상품들의_가격_총합을_제대로_계산하는지_확인한다() {
        final MenuProduct 가격_45_상품 = MenuProduct.from(new Product(1L, Name.from("name"), Price.from(new BigDecimal(15))), 3);
        final MenuProduct 가격_0_상품 = MenuProduct.from(new Product(1L, Name.from("name"), Price.from(new BigDecimal(15))), 0);
        final MenuProduct 가격_70_상품 = MenuProduct.from(new Product(1L, Name.from("name"), Price.from(new BigDecimal(10))), 7);

        final MenuProducts menuProducts = MenuProducts.from(List.of(가격_45_상품, 가격_0_상품, 가격_70_상품));

        assertThat(menuProducts.getPrice()).isEqualByComparingTo(new BigDecimal(115));
    }
}
