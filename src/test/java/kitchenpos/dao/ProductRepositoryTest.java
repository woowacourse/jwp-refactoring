package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class ProductRepositoryTest {

    private ProductRepository productRepository;

    @Autowired
    public ProductRepositoryTest(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Test
    void save() {
        // given
        Product product = new Product("상품", BigDecimal.valueOf(1000));

        // when
        Product savedProduct = productRepository.save(product);

        // then
        assertThat(savedProduct.getId()).isNotNull();
    }

    @Test
    void findById() {
        // given
        Product savedProduct = productRepository.save(new Product("상품", BigDecimal.valueOf(1000)));

        // when
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        // then
        assertThat(foundProduct).isPresent();
    }

    @Test
    void findAll() {
        // given
        productRepository.save(new Product("상품A", BigDecimal.valueOf(1000)));
        productRepository.save(new Product("상품B", BigDecimal.valueOf(2000)));

        // when
        List<Product> products = productRepository.findAll();

        // then
        int defaultSize = 6;
        assertThat(products).hasSize(defaultSize + 2);
    }
}
