package kitchenpos.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductTest {

    @ParameterizedTest
    @ValueSource(strings = {"-1", "-100"})
    @NullSource
    void 상품의_가격이_Null이거나_음수면_예외(BigDecimal price) {
        // when & then
        Assertions.assertThatThrownBy(() -> Product.builder()
                        .price(price)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }
}
