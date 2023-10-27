package kitchenpos.application;

import kitchenpos.execute.ServiceIntegrateTest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.product.dto.request.ProductCreateRequest;
import kitchenpos.product.dto.response.ProductResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductServiceIntegrateTest extends ServiceIntegrateTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Nested
    class 상품을_생성한다 {
        @Test
        void 상품을_정상적으로_생성한다() {
            // given
            ProductCreateRequest request = new ProductCreateRequest("치킨", BigDecimal.valueOf(12000));

            // when, then
            assertDoesNotThrow(() -> productService.create(request));
        }

        @Test
        void 가격이_Null이면_예외가_발생한다() {
            // given
            ProductCreateRequest request = new ProductCreateRequest("치킨", null);

            // when, then
            assertThrows(
                    IllegalArgumentException.class, () -> productService.create(request)
            );
        }

        @Test
        void 가격이_0보다_낮으면_예외가_발생한다() {
            // given
            ProductCreateRequest request = new ProductCreateRequest("치킨", BigDecimal.valueOf(-10000000000L));

            // when, then
            assertThrows(
                    IllegalArgumentException.class, () -> productService.create(request)
            );
        }
    }

    @Nested
    class 상품_목록을_조회한다 {

        Product product;

        @BeforeEach
        void setUp() {
            Product product = new Product("치킨", BigDecimal.valueOf(20000L));
            this.product = productRepository.save(product);
        }

        @Test
        void 상품_목록을_조회한다() {
            // when
            List<ProductResponse> products = productService.list();

            // then
            Assertions.assertAll(
                    () -> assertThat(products).hasSize(1),
                    () -> assertThat(products).extracting(ProductResponse::getName)
                            .contains("치킨"),
                    () -> assertThat(products).extracting(ProductResponse::getPrice)
                            .allSatisfy(price -> assertThat(price).isGreaterThanOrEqualTo(BigDecimal.valueOf(20000L)))
            );
        }

    }

}