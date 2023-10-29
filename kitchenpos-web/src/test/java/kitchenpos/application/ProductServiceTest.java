package kitchenpos.application;

import kitchenpos.application.product.ProductService;
import kitchenpos.domain.product.Product;
import kitchenpos.exception.KitchenposException;
import kitchenpos.presentation.product.dto.ProductRequest;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.exception.ExceptionInformation.PRODUCT_PRICE_IS_NULL;
import static kitchenpos.exception.ExceptionInformation.PRODUCT_PRICE_LENGTH_OUT_OF_BOUNCE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayName("상품 서비스 테스트")
@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 모든_상품을_조회한다() {
        productService.create(new ProductRequest("test", new BigDecimal("1")));
        final List<Product> 조회한_상품들 = productService.list();

        assertThat(조회한_상품들).hasSize(1);
    }

    @Nested
    @DisplayName("상품 저장 테스트")
    class createTest {
        @Test
        void 정상적인_상품을_저장한다() {
            final ProductRequest 새상품 = new ProductRequest("test", new BigDecimal(10));
            var 저장_상품 = productService.create(새상품);

            assertSoftly(soft -> {
                soft.assertThat(저장_상품.getId()).isNotNull();
                soft.assertThat(저장_상품.getPrice().getPrice()).isEqualTo(새상품.getPrice());
                soft.assertThat(저장_상품.getName().getName()).isEqualTo(새상품.getName());
            });
        }

        @Test
        void 상품_가격이_0이하면_예외가_발생한다() {
            final ProductRequest 새상품 = new ProductRequest("test", new BigDecimal(-1));


            assertThatThrownBy(() -> productService.create(새상품))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(PRODUCT_PRICE_LENGTH_OUT_OF_BOUNCE.getMessage());
        }

        @Test
        void 상품_가격이_비어있다면_예외가_발생한다() {
            final ProductRequest 새상품 = new ProductRequest("test", null);

            assertThatThrownBy(() -> productService.create(새상품))
                    .isExactlyInstanceOf(KitchenposException.class)
                    .hasMessage(PRODUCT_PRICE_IS_NULL.getMessage());
        }
    }
}
