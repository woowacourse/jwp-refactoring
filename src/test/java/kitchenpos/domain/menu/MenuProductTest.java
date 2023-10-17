package kitchenpos.domain.menu;

import kitchenpos.domain.product.Name;
import kitchenpos.domain.product.Price;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("메뉴에 속한 상품 테스트")
class MenuProductTest {

    @ParameterizedTest
    @CsvSource(value = {"1:2:2", "2:0:0", "100:20:2000"}, delimiter = ':')
    void 가격_조회시_상품과_가격을_제대로_계산하는지_확인한다(int price, int quantity, int expected) {
        final Product product = new Product(1L, Name.from("name"), Price.from(new BigDecimal(price)));
        final MenuProduct menuProduct = MenuProduct.from(product, quantity);

        assertThat(menuProduct.getPrice()).isEqualByComparingTo(new BigDecimal(expected));
    }
}
