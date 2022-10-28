package kitchenpos.application.jpa;

import static kitchenpos.application.fixture.ProductFixture.PRODUCT_NAME;
import static kitchenpos.application.fixture.ProductFixture.PRODUCT_PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.entity.Product;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest extends ServiceTestJpa {

    @Autowired
    private ProductServiceJpa productService;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("product를 생성한다.")
    @Test
    void create() {
        Product product = new Product(PRODUCT_NAME, PRODUCT_PRICE);
        productService.create(product);

        assertThat(product.getId()).isNotNull();
    }

    @DisplayName("모든 product를 조회한다.")
    @Test
    void list() {
        int numberOfProductBeforeCreate = productService.list().size();
        productRepository.save(new Product(PRODUCT_NAME, PRODUCT_PRICE));

        int numberOfProduct = productService.list().size();

        assertThat(numberOfProductBeforeCreate + 1).isEqualTo(numberOfProduct);
    }
}
