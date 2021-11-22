package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceTest {

    @DisplayName("가격은 음수가 될 수 없습니다")
    @Test
    void create() {
        //given
        //when
        //then
        assertThatThrownBy(() -> Price.create(-1000))
                .isInstanceOf(InvalidPriceException.class);
    }
}