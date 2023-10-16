package kitchenpos.application;

import kitchenpos.domain.product.Product;
import kitchenpos.support.ServiceTest;
import kitchenpos.ui.dto.request.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

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
                soft.assertThat(new BigDecimal(1)).isEqualByComparingTo(new BigDecimal(1));
                soft.assertThat(저장_상품).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(새상품);
            });
        }

        @Test
        void 상품_가격이_0이하면_예외가_발생한다() {
            final ProductRequest 새상품 = new ProductRequest("test", new BigDecimal(-1));


            assertThatThrownBy(() -> productService.create(새상품))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품의 가격은 0원 이상이여야 합니다");
        }

        @Test
        void 상품_가격이_비어있다면_예외가_발생한다() {
            final ProductRequest 새상품 = new ProductRequest("test", null);

            assertThatThrownBy(() -> productService.create(새상품))
                    .isExactlyInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품의 가격은 0원 이상이여야 합니다");
        }
    }
}
