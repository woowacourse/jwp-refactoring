package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.support.money.Money;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductTest {

    @Test
    void 상품의_가격이_음수인_경우_예외를_던진다() {
        // expect
        assertThatThrownBy(() -> new Product("피자", Money.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품의 가격은 0원 이상이어야 합니다.");
    }
}
