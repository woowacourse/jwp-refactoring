package kitchenpos.application;

import static kitchenpos.common.ProductFixtures.PRODUCT1_REQUEST;
import static kitchenpos.common.ProductFixtures.PRODUCT1_NAME;
import static kitchenpos.common.ProductFixtures.PRODUCT1_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Nested
    @DisplayName("Product 생성 시")
    class CreateProduct {

        @Test
        @DisplayName("생성에 성공한다.")
        void success() {
            // given
            Product expectedProduct = new Product();
            expectedProduct.setName(PRODUCT1_NAME);
            expectedProduct.setPrice(PRODUCT1_PRICE);
            expectedProduct.setId(1L);

            given(productDao.save(any(Product.class)))
                    .willReturn(expectedProduct);

            // when
            Product createdProduct = productService.create(PRODUCT1_REQUEST());

            // then
            assertThat(createdProduct).usingRecursiveComparison().isEqualTo(expectedProduct);
        }

        @Test
        @DisplayName("상품 가격이 존재하지 않으면 예외가 발생한다.")
        void throws_WhenProductPriceNotExist() {
            // given
            Product product = new Product();
            product.setName(PRODUCT1_NAME);
            product.setPrice(null);

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 상품 가격은 null 또는 0 미만의 값일 수 없습니다.");
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, Integer.MIN_VALUE})
        @DisplayName("상품 가격이 0 미만이면 예외가 발생한다.")
        void throws_WhenProductPriceLessThanZero(final int price) {
            // given
            Product product = new Product();
            product.setName(PRODUCT1_NAME);
            product.setPrice(BigDecimal.valueOf(price));

            // when & then
            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 상품 가격은 null 또는 0 미만의 값일 수 없습니다.");
        }
    }
}
