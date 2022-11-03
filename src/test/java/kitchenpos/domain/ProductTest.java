package kitchenpos.domain;

import static kitchenpos.support.fixture.ProductFixtures.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Nested
    class 상품을_생성 {
        @Test
        void 할_때_가격이_음수인_경우_예외가_발생한다() {
            assertThatThrownBy(() -> new Product(후라이드상품.getName(), 상품_가격_음수()));
        }

        @Test
        void 할_때_가격이_NULL_일_경우_예외가_발생한다() {
            assertThatThrownBy(() -> new Product(후라이드상품.getName(), 상품_가격_NULL()));
        }
    }
}
