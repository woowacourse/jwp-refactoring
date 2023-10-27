package kitchenpos.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NameTest {
    @Test
    @DisplayName("이름은 255자까지만 입력할 수 있다.")
    void nameLength() {
        Assertions.assertThatThrownBy(() -> new Name("메".repeat(256)))
                .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatCode(() -> new Name("메".repeat(255)))
                .doesNotThrowAnyException();
    }
}
