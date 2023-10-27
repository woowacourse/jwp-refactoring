package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.exception.InvalidNameException;
import kitchenpos.domain.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductTest {

    @Test
    void 생성자는_유효한_이름과_가격을_전달하면_Product를_초기화한다() {
        // when & then
        assertThatCode(() -> new Product("상품", BigDecimal.TEN));
    }

    @ParameterizedTest(name = "이름이 {0}이면 예외가 발생한다.")
    @NullAndEmptySource
    void 생성자는_유효하지_않은_이름을_전달하면_예외가_발생한다(final String invalidName) {
        // when & then
        assertThatThrownBy(() -> new Product(invalidName, BigDecimal.TEN))
                .isInstanceOf(InvalidNameException.class);
    }

    @Test
    void 생성자는_유효하지_않은_금액을_전달하면_예외가_발생한다() {
        // given
        final BigDecimal invalidPrice = new BigDecimal(-1);

        // when & then
        assertThatThrownBy(() -> new Product("상품", invalidPrice))
                .isInstanceOf(InvalidPriceException.class);
    }
}
