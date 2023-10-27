package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.application.ProductDao;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

@DataJdbcTest
class ProductServiceTest {

    private ProductService productService;

    @Autowired
    ProductDao productDao;

    @BeforeEach
    void setUp() {
        this.productService = new ProductService(productDao);
    }

    @Test
    @DisplayName("이름과 가격을 제공하면 새로운 상품을 제공할 수 있다.")
    void givenNameAndPrice() {
        final ProductResponse savedProduct = this.productService.create(new Name("쫀득쫀득 지렁이"), new Price(new BigDecimal("4000")));
        assertThat(savedProduct).isNotNull();
    }
}
