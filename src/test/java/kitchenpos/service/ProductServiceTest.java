package kitchenpos.service;

import static kitchenpos.utils.TestObjectUtils.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;
    
    private String productName;
    private BigDecimal productPrice;

    @BeforeEach
    void setUp() {
        productName = "강정치킨";
        productPrice = BigDecimal.valueOf(17000L);
    }

    @DisplayName("상품 생성 - 성공")
    @Test
    void create_SuccessToCreate() {
        Product product = createProduct(productName, productPrice);

        Product savedProduct = productService.create(product);

        assertAll(() -> {
            assertThat(savedProduct.getId()).isNotNull();
            assertThat(savedProduct.getName()).isEqualTo(productName);
            assertThat(savedProduct.getPrice()).isEqualByComparingTo(productPrice);
        });
    }

    @DisplayName("상품 생성 - 예외, 상품 가격이 Null인 경우")
    @Test
    void create_IfProductPriceIsNull_ThrownException() {
        productPrice = null;
        Product product = createProduct(productName, productPrice);

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 생성 - 예외, 상품 가격이 음수인 경우")
    @Test
    void create_IfProductPriceIsNegative_ThrownException() {
        productPrice = BigDecimal.valueOf(-10000L);
        Product product = createProduct(productName, productPrice);

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
