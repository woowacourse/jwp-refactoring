package kitchenpos.product;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.common.UnitTest;
import kitchenpos.product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class ProductTest {

    @Test
    void product를_생성한다() {
        Product actual = new Product("후라이드", BigDecimal.valueOf(16000));

        assertAll(() -> {
            assertThat(actual.getName()).isEqualTo("후라이드");
            assertThat(actual.getPrice().compareTo(BigDecimal.valueOf(16000))).isEqualTo(0);
        });
    }

    @Test
    void product를_생성할_때_price가_null인_경우_예외를_던진다() {
        assertThatThrownBy(() -> new Product("후라이드", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "product가 {0}미만인 경우 예외를 던진다")
    @ValueSource(ints = {-15000, -10, Integer.MIN_VALUE})
    void menu를_생성할_때_price가_0미만인_경우_예외를_던진다(final int price) {
        assertThatThrownBy(() -> new Product("후라이드", BigDecimal.valueOf(price)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
