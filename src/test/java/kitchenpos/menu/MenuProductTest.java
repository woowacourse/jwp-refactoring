package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.product.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuProductTest {

    @ParameterizedTest
    @CsvSource(value = {"15000:5", "5000:2", "6000:4"}, delimiter = ':')
    void 수량과_가격을_바탕으로_총합가격을_계산한다(Long price, Integer quantity) {
        // given
        MenuProduct menuProduct = new MenuProduct(new Product("치킨", BigDecimal.valueOf(price)), quantity);

        // when && then
        assertThat(menuProduct.calculateTotalPrice()).isEqualTo(BigDecimal.valueOf(price * quantity));
    }
}
