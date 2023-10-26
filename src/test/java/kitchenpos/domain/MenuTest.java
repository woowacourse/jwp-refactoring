package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
public class MenuTest {

    @Nested
    class Constructor_성공_테스트 {
    }

    @Nested
    class Constructor_실패_테스트 {

        @Test
        void 메뉴의_가격이_실제_각_메뉴의_상품_합보다_클_경우_에러를_반환한다() {
            // given
            final var product = new Product("상품", BigDecimal.valueOf(100L));
            final var menuProduct = new MenuProduct(product, 3L);

            // when & then
            assertThatThrownBy(() -> new Menu("메뉴_이름", BigDecimal.valueOf(1000L), null, List.of(menuProduct)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 총 금액이 각 상품의 합보다 큽니다.");
        }
    }
}
