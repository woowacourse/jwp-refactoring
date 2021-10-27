package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import support.IntegrationTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
class ProductServiceTest {

    private static final String PRODUCT_NAME = "스타벅스 돌체라떼";
    private static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(5_600);

    @Autowired
    private ProductService productService;

    @Nested
    @DisplayName("[상품 등록]")
    class CreateProduct {

        @DisplayName("성공")
        @Test
        void create() {
            //given
            Product savedProduct = testProduct();

            //then
            assertThat(savedProduct).isNotNull();
            assertThat(savedProduct.getPrice().longValue()).isEqualTo(PRODUCT_PRICE.longValue());
            assertThat(savedProduct.getName()).isEqualTo(PRODUCT_NAME);
        }

        @DisplayName("실패 - 상품의 가격이 0원 미만인 경우")
        @Test
        void createWhenPriceIsNegative() {
            //given
            BigDecimal invalidPrice = BigDecimal.valueOf(-1_000);

            // when //then
            assertThatThrownBy(() -> testProduct(invalidPrice))
                    .isExactlyInstanceOf(IllegalArgumentException.class);

        }
    }

    @DisplayName("[상품 조회] - 성공")
    @Test
    void list() {
        //when
        List<Product> actual = productService.list();

        //then
        assertThat(actual).hasSize(6);
    }

    private Product testProduct(BigDecimal price) {
        Product product = new Product();
        product.setName(PRODUCT_NAME);
        product.setPrice(price);

        return productService.create(product);
    }

    private Product testProduct() {
        return testProduct(PRODUCT_PRICE);
    }
}
