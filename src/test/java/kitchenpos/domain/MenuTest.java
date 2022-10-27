package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {

    private final Long noId = null;

    @Test
    @DisplayName("메뉴를 생성한다.")
    void create() {
        // when, then
        assertDoesNotThrow(() -> new Menu("후라이드+후라이드+후라이드", new BigDecimal(0), noId,
            List.of(new MenuProduct(noId, 3))));
    }

    @Test
    @DisplayName("가격이 음수이면 예외를 던진다.")
    void create_price_negative() {
        // when, then
        assertThatThrownBy(() -> new Menu("후라이드+후라이드+후라이드", new BigDecimal(-1), noId,
            List.of(new MenuProduct(noId, 3))))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
