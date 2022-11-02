package kitchenpos.repository.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.ProductFixtures;
import kitchenpos.domain.product.Product;
import kitchenpos.repository.ProductRepository;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class ProductEntityRepositoryTest {

    private final ProductRepository productRepository;
    private final ProductEntityRepository productEntityRepository;

    @Autowired
    public ProductEntityRepositoryTest(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.productEntityRepository = new ProductEntityRepositoryImpl(productRepository);
    }

    @Test
    void getAlLByIdIn() {
        // given
        Product product = productRepository.save(ProductFixtures.createProduct());
        // when
        List<Product> foundProducts = productEntityRepository.getAllByIdIn(List.of(product.getId()));
        // then
        assertThat(foundProducts).hasSize(1);
    }

    @Test
    void getAllByIdInWithInvalidId() {
        // given
        Long invalidId = 999L;
        // when & then
        assertThatThrownBy(() -> productEntityRepository.getAllByIdIn(List.of(invalidId)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
