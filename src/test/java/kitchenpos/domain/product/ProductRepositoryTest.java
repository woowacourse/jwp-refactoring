package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("상품을 저장한다.")
    void save() {
        final Product product = Product.create("상품", BigDecimal.valueOf(1000L));

        final Product savedProduct = productRepository.save(product);

        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo("상품"),
                () -> assertThat(savedProduct.getPriceValue().longValue()).isEqualTo(1000L)
        );
    }

    @Test
    @DisplayName("상품을 id로 찾는다.")
    void findById() {
        final Product product = Product.create("상품", BigDecimal.valueOf(1000L));
        final Product savedProduct = productRepository.save(product);

        final Product findProduct = productRepository.findById(savedProduct.getId()).orElseThrow();

        assertAll(
                () -> assertThat(findProduct.getId()).isEqualTo(savedProduct.getId()),
                () -> assertThat(findProduct.getName()).isEqualTo(savedProduct.getName()),
                () -> assertThat(findProduct.getPriceValue().longValue()).isEqualTo(savedProduct.getPriceValue().longValue())
        );
    }

    @Test
    @DisplayName("상품 전체를 조회한다.")
    void findAll() {
        final Product product1 = Product.create("상품1", BigDecimal.valueOf(1000L));
        final Product savedProduct1 = productRepository.save(product1);
        final Product product2 = Product.create("상품2", BigDecimal.valueOf(1500L));
        final Product savedProduct2 = productRepository.save(product2);

        final List<Product> products = productRepository.findAll();

        assertAll(
                () -> assertThat(products)
                        .extracting("id").contains(savedProduct1.getId(), savedProduct2.getId()),
                () -> assertThat(products)
                        .extracting("name").contains(savedProduct1.getName(), savedProduct2.getName())
        );
    }
}