package kitchenpos.product.domain;

import static kitchenpos.support.TestFixtureFactory.새로운_상품;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.common.exception.PriceException;
import kitchenpos.product.exception.ProductException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductTest {

    @Test
    void 상품의_이름은_255자_이하여야_한다() {
        assertThatThrownBy(() -> 새로운_상품(null, "짱".repeat(256), BigDecimal.ONE))
                .isInstanceOf(ProductException.class)
                .hasMessage("상품의 이름이 유효하지 않습니다.");

    }

    @ValueSource(strings = {"-1", "100000000000000000000"})
    @ParameterizedTest
    void 상품의_가격은_0원_이상_100조원_미만이어야_한다(String price) {
        assertThatThrownBy(() -> 새로운_상품(null, "상품", new BigDecimal(price)))
                .isInstanceOf(PriceException.class)
                .hasMessage("가격이 유효하지 않습니다.");
    }
}
