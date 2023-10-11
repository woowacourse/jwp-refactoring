package kitchenpos.application.service;

import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static kitchenpos.domain.ProductFixture.상품_생성;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductServiceTest extends IntegrateServiceTest {

    @Autowired
    private ProductService productService;

    @Nested
    class 상품을_생성한다 {
        @Test
        void 상품을_정상적으로_생성한다() {
            // given
            Product 상품 = 상품_생성();

            // when, then
            assertDoesNotThrow(() -> productService.create(상품));
        }

        @Test
        void 가격이_Null이면_예외가_발생한다() {
            // given
            Product 상품 = 상품_생성();
            상품.setPrice(null);

            // when, then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> productService.create(상품)
            );
        }

        @Test
        void 가격이_0보다_낮으면_예외가_발생한다() {
            // given
            Product 상품 = 상품_생성();
            상품.setPrice(BigDecimal.valueOf(-1000));

            // when, then
            assertThrows(
                    IllegalArgumentException.class,
                    () -> productService.create(상품)
            );
        }
    }

}