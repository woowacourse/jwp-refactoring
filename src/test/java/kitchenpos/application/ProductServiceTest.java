package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.product.ProductCreateRequest;
import kitchenpos.application.dto.product.ProductCreateResponse;
import kitchenpos.application.dto.product.ProductResponse;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("classpath:truncate.sql")
@TestConstructor(autowireMode = AutowireMode.ALL)
class ProductServiceTest {

    private final ProductService productService;

    public ProductServiceTest(final ProductService productService) {
        this.productService = productService;
    }

    @Nested
    class 상품_등록_시 {

        @Test
        void 상품을_정상적으로_등록한다() {
            // given
            final ProductCreateRequest 등록할_상품 = new ProductCreateRequest("상품", BigDecimal.valueOf(10000, 2));

            // when
            final ProductCreateResponse 등록된_상품 = productService.create(등록할_상품);

            // then
            assertAll(
                    () -> assertThat(등록된_상품.getId()).isNotNull(),
                    () -> assertThat(등록된_상품).usingRecursiveComparison()
                            .ignoringFields("id")
                            .isEqualTo(등록할_상품)
            );
        }

        @Test
        void 상품의_가격이_0보다_작으면_예외가_발생한다() {
            // given
            final ProductCreateRequest 등록할_상품 = new ProductCreateRequest("상품", BigDecimal.valueOf(-10000));

            // expected
            assertThatThrownBy(() -> productService.create(등록할_상품))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 상품의_가격이_없으면_예외가_발생한다() {
            // given
            final ProductCreateRequest 등록할_상품 = new ProductCreateRequest("상품", null);

            // expected
            assertThatThrownBy(() -> productService.create(등록할_상품))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 상품_목록을_정상적으로_조회한다() {
        // given
        productService.create(new ProductCreateRequest("상품1", BigDecimal.valueOf(10000, 2)));
        productService.create(new ProductCreateRequest("상품2", BigDecimal.valueOf(20000, 2)));

        // when
        final List<ProductResponse> 상품들 = productService.list();

        // then
        final ProductResponse 상품1_응답값 = ProductResponse.of(new Product("상품1", BigDecimal.valueOf(10000, 2)));
        final ProductResponse 상품2_응답값 = ProductResponse.of(new Product("상품2", BigDecimal.valueOf(20000, 2)));

        assertThat(상품들).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(상품1_응답값, 상품2_응답값));
    }
}
