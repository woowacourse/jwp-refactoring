package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.PRODUCT_FIXTURE_1;
import static kitchenpos.fixture.ProductFixture.PRODUCT_FIXTURE_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void create() {
        ProductRequest product = new ProductRequest("1", BigDecimal.TEN);

        ProductResponse persistProduct = productService.create(product);

        assertAll(
            () -> assertThat(persistProduct.getId()).isNotNull(),
            () -> assertThat(persistProduct.getName()).isEqualTo(product.getName()),
            () -> assertThat(persistProduct.getPrice().longValue()).isEqualTo(product.getPrice().longValue())
        );
    }

    @Test
    void list() {
        productService.create(new ProductRequest("1", BigDecimal.TEN));
        productService.create(new ProductRequest("2", BigDecimal.TEN));

        List<ProductResponse> products = productService.list();
        List<String> productNames = products.stream()
            .map(ProductResponse::getName)
            .collect(Collectors.toList());

        assertThat(productNames).contains(PRODUCT_FIXTURE_1.getName(), PRODUCT_FIXTURE_2.getName());
    }
}