package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductTest {

    private Long id = 1L;
    private String name = "pasta";
    private BigDecimal price = BigDecimal.valueOf(13000);

    @Test
    void product를_생성할_수_있다() {
        Product product = new Product(id, name, price);
        Assertions.assertAll(
                () -> assertThat(product.getId()).isEqualTo(id),
                () -> assertThat(product.getName()).isEqualTo(name),
                () -> assertThat(product.getPrice()).isEqualTo(price)
        );
    }

    @Test
    void price가_null이면_예외을_반환한다() {
        BigDecimal nullPrice = null;
        assertThatThrownBy(() -> new Product(id, name, nullPrice))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void price가_0보다_적으면_예외를_반환한다() {
        BigDecimal negativePrice = BigDecimal.valueOf(-1000);
        assertThatThrownBy(() -> new Product(id, name, negativePrice))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
