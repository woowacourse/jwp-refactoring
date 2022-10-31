package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class ProductTest {

    @Test
    void 가격이_존재하지_않는_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new Product("치킨", null))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("price가 존재하지 않습니다.");
    }

    @Test
    void 가격이_음수인_경우_예외가_발생한다() {
        assertThatThrownBy(() -> new Product("치킨", new BigDecimal(-1)))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("price가 음수입니다.");
    }

}
