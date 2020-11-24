package kitchenpos.repository;

import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상품을 저장할 수 있다.")
    @Test
    void save() {
        Product product = createProduct(null, "치킨", 0L);

        Product savedProduct = productRepository.save(product);

        assertAll(
            () -> assertThat(savedProduct).isNotNull(),
            () -> assertThat(savedProduct.getId()).isNotNull(),
            () -> assertThat(savedProduct.getName()).isEqualTo(product.getName()),
            () -> assertThat(savedProduct.getPrice().longValue())
                .isEqualTo(product.getPrice().longValue())
        );
    }

    @DisplayName("상품 아이디로 상품을 조회할 수 있다.")
    @Test
    void findById() {
        Product product = productRepository.save(createProduct(null, "치킨", 0L));

        Optional<Product> foundProduct = productRepository.findById(product.getId());

        assertThat(foundProduct.get().getId()).isEqualTo(product.getId());
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void findAll() {
        List<Product> savedProducts = Arrays.asList(
            productRepository.save(createProduct(null, "치킨1", 10000L)),
            productRepository.save(createProduct(null, "치킨2", 10000L)),
            productRepository.save(createProduct(null, "치킨3", 10000L))
        );

        List<Product> allProducts = productRepository.findAll();

        assertThat(allProducts).usingFieldByFieldElementComparator().containsAll(savedProducts);
    }
}
