package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {

    @DisplayName("가격이 음수인 제품은 생성할 수 없다")
    @Test
    void create_priceNegative() {
        assertThatThrownBy(() -> new Product("test", -1L)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 null인 제품은 생성할 수 없다")
    @Test
    void create_priceNull() {
        assertThatThrownBy(() -> new Product("test", null)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이름이 null인 제품은 생성할 수 없다")
    @Test
    void create_nameNull() {
        assertThatThrownBy(() -> new Product(null, 0L)).isInstanceOf(IllegalArgumentException.class);
    }
}
