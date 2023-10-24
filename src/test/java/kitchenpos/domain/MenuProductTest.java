package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.supports.MenuProductFixture;
import kitchenpos.supports.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuProductTest {

    @Test
    void 가격의_총합을_계산한다() {
        // given
        Product product = ProductFixture.fixture().price(10_000).build();
        MenuProduct menuProduct = MenuProductFixture.fixture().product(product).quantity(2L).build();

        // when & then
        Price price = menuProduct.calculatePrice();

        // then
        assertThat(price.getValue()).isEqualTo(new BigDecimal(20_000));
    }
}
