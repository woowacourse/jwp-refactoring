package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    @DisplayName("상품을 생성한다.")
    void create() {
        final Product product = Product.create("테스트상품", BigDecimal.valueOf(15000L));

        assertAll(
                () -> assertThat(product.getName()).isEqualTo("테스트상품"),
                () -> assertThat(product.getPrice().longValue()).isEqualTo(15000L)
        );
    }
}