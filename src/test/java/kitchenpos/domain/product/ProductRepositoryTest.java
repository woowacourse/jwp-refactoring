package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.TransactionalTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품을_저장하면_id값이_채워진다() {
        Product product = new Product("상품", BigDecimal.ZERO);

        Product savedProduct = productRepository.save(product);

        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(product.getName()),
                () -> assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice())
        );
    }

    @Test
    void 상품을_id로_조회할_수_있다() {
        Product product = productRepository.save(new Product("상품", BigDecimal.ZERO));

        Product actual = productRepository.findById(product.getId())
                .orElseGet(Assertions::fail);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(product);
    }

    @Test
    void 없는_상품_id로_조회하면_Optional_empty를_반환한다() {
        Optional<Product> actual = productRepository.findById(0L);

        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_상품을_조회할_수_있다() {
        Product product1 = productRepository.save(new Product("상품1", BigDecimal.valueOf(1_000)));
        Product product2 = productRepository.save(new Product("상품2", BigDecimal.valueOf(2_000)));

        List<Product> actual = productRepository.findAll();

        assertThat(actual).hasSize(2)
                .usingFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("price")
                .containsExactly(product1, product2);
    }
}
