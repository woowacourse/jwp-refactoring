package kitchenpos.domain.product;

import kitchenpos.BaseTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductNameTest extends BaseTest {

    @Test
    void 상품_이름을_생성한다() {
        // given
        String name = "pizza";

        // when, then
        Assertions.assertThatNoException()
                .isThrownBy(() -> new ProductName(name));
    }

    @Test
    void 상품_이름은_공백일_수_있다() {
        // given
        String name = "";

        // when, then
        Assertions.assertThatNoException()
                .isThrownBy(() -> new ProductName(name));
    }
}
