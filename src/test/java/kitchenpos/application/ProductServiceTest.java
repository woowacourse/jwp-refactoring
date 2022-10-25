package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.ApplicationTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void create() {
        Product request = Product.of("name", BigDecimal.valueOf(1000));

        Product savedProduct = productService.create(request);

        assertThat(savedProduct.getId()).isNotNull();
    }

    @Test
    void createThrowExceptionWhenPriceNegative() {
        assertThatThrownBy(() -> productService.create(Product.of("name", BigDecimal.valueOf(-10))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("product의 가격은 0원 이상이어야 합니다.");
    }

    @Test
    void list() {
        List<Product> products = productService.list();

        assertThat(products.size()).isEqualTo(6);
    }
}
