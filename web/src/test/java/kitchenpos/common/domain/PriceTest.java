package kitchenpos.common.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import domain.Price;
import exception.PriceException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PriceTest {

    @Test
    void 가격은_0원_이상이어야_한다() {
        assertThatThrownBy(() -> new Price(new BigDecimal(-1)))
                .isInstanceOf(PriceException.class)
                .hasMessage("가격이 유효하지 않습니다.");
    }

    @Test
    void 가격은_100조원_미만이어야_한다() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(Math.pow(10, 20))))
                .isInstanceOf(PriceException.class)
                .hasMessage("가격이 유효하지 않습니다.");
    }

}
