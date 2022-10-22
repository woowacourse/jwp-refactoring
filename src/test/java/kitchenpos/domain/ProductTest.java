package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductTest {

    @Test
    void invalidPrice() {
        assertThatThrownBy(() -> new Product("상품", -1)).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 가격은 음수일 수 없습니다.");
    }
}
