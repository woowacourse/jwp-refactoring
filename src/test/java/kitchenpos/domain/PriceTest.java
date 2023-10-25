package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    void 메뉴_가격은_음수일_수_없다() {
        assertThatThrownBy(() -> Price.of(-1)).isInstanceOf(IllegalArgumentException.class);
    }
}
