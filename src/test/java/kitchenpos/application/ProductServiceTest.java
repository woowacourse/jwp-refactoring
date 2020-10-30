package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.PRODUCT_FIXTURE_1;
import static kitchenpos.fixture.ProductFixture.PRODUCT_FIXTURE_2;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void create() {
        Product product = PRODUCT_FIXTURE_1;

        Product persistProduct = productService.create(product);

        assertThat(persistProduct).isNotNull();
    }

    @Test
    void list() {
        productService.create(PRODUCT_FIXTURE_1);
        productService.create(PRODUCT_FIXTURE_2);

        List<Product> products = productService.list();
        List<String> productNames = products.stream()
            .map(Product::getName)
            .collect(Collectors.toList());

        assertThat(productNames).contains(PRODUCT_FIXTURE_1.getName(), PRODUCT_FIXTURE_2.getName());
    }
}