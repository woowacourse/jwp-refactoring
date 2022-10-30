package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuTest {

    @DisplayName("가격은 0 이상이어야 한다.")
    @Test
    void createAndList_invalidPrice() {
        BigDecimal 음수_가격 = BigDecimal.valueOf(-10000);

        assertThatThrownBy(() -> new Menu(1L, "후라이드", 음수_가격,
                1L, List.of(new MenuProduct(1L, 1L, 1L, 1, new Price(16000)))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격은 0 이상이어야 한다.");
    }
}
