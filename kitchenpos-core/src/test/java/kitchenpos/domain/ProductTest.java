package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-1", "-999"})
    void 상품의_가격이_null_이거나_0보다_작으면_예외가_발생한다(final BigDecimal value) {
        // when & then
        Assertions.assertThatThrownBy(() -> new Product("망고", value))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
