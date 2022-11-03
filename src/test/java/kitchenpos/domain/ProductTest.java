package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.application.ServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@IgnoreDisplayNameUnderscores
class ProductTest {

    @Nested
    class 생성자는 {

        @Test
        void 가격이_null이면_예외가_발생한다() {
            // when & then
            assertThatThrownBy(() -> new Product("상품", null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 가격이_음수면_예외가_발생한다() {
            // when & then
            assertThatThrownBy(() -> new Product("상품", BigDecimal.valueOf(-1)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
