package kitchenpos.service;

import static kitchenpos.utils.TestObjectUtils.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품 생성 - 성공")
    @Test
    void create_SuccessToCreate() {
        String name = "강정치킨";
        BigDecimal price = BigDecimal.valueOf(17000L);
        Product product = createProduct(name, price);

        Product savedProduct = productService.create(product);

        assertAll(() -> {
            assertThat(savedProduct.getId()).isNotNull();
            assertThat(savedProduct.getName()).isEqualTo(name);
            assertThat(savedProduct.getPrice()).isEqualByComparingTo(price);
        });
    }

    @DisplayName("상품 생성 - 예외, 상품 가격이 Null인 경우")
    @Test
    void create_IfProductPriceIsNull_ThrownException() {
        String name = "강정치킨";
        BigDecimal price = null;
        Product product = createProduct(name, price);

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 생성 - 예외, 상품 가격이 음수인 경우")
    @Test
    void create_IfProductPriceIsNegative_ThrownException() {
        String name = "강정치킨";
        BigDecimal price = BigDecimal.valueOf(-10000L);
        Product product = createProduct(name, price);

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 전체 조회 - 성공")
    @Test
    void create_SuccessToFindAll() {
        List<Product> products = productService.list();

        assertThat(products).isNotEmpty();
    }
}
