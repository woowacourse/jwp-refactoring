package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import kitchenpos.application.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("가격은")
class PriceTest {

    @Test
    @DisplayName("0원 미만일 경우 예외가 발생한다.")
    void priceLessThanZero() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Price.of(-1))
                .withMessage("가격은 0원 이상이어야 합니다.");
    }
}
