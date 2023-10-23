package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품_엔티티를_저장한다() {
        Product productEntity = createProductEntity();

        Product savedProduct = productRepository.save(productEntity);

        assertThat(savedProduct.getId()).isPositive();
    }

    @Test
    void 상품_엔티티를_조회한다() {
        Product productEntity = createProductEntity();
        Product savedProduct = productRepository.save(productEntity);

        assertThat(productRepository.findById(savedProduct.getId())).isPresent();
    }

    @Test
    void 모든_상품_엔티티를_조회한다() {
        Product productEntityA = createProductEntity();
        Product productEntityB = createProductEntity();
        Product savedProductA = productRepository.save(productEntityA);
        Product savedProductB = productRepository.save(productEntityB);

        List<Product> products = productRepository.findAll();

        assertThat(products)
                .usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(savedProductA, savedProductB);
    }

    private Product createProductEntity() {
        return Product.builder()
                .name("juice")
                .price(1_000_000_000)
                .build();
    }
}
