package kitchenpos.domain;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductTest {

    @Test
    void 가격이_0보다_작거나_없다면_예외가_발생한다() {
        // given
        assertThatThrownBy(() -> new Product("치즈피자", new BigDecimal(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품 가격은 0 이하 혹은 null일 수 없습니다.");
    }

    @Test
    void 상품_가격이_없으면_예외가_발생한다() {
        // given
        assertThatThrownBy(() -> new Product("치즈피자", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품 가격은 0 이하 혹은 null일 수 없습니다.");
    }
}
