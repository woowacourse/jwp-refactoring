package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Collections;
import kitchenpos.domain.Products;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductsTest {

    @Test
    @DisplayName("상품이 존재하지 않는 경우 예외를 발생한다.")
    void empty() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Products(Collections.emptyList()))
                .withMessage("상품이 존재하지 않습니다.");
    }
}
