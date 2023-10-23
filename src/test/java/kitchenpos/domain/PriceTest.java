package kitchenpos.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.exception.InvalidRequestParameterException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class PriceTest {

    @Test
    void 음수인_가격을_생성하는_경우_예외를_반환한다() {
        assertThrows(InvalidRequestParameterException.class, () -> Price.from(new BigDecimal( -1)));
    }

    @Test
    void NULL인_가격을_생성하는_경우_예외를_반환한다() {
        assertThrows(InvalidRequestParameterException.class, () -> Price.from(null));
    }
}
