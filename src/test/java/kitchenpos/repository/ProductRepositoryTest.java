package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

    private final ProductRepository productRepository;

    @Autowired
    public ProductRepositoryTest(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Test
    void 저장한다() {
        Product product = new Product("pasta", price(13000));
        Product savedProduct = productRepository.save(product);

        Assertions.assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo("pasta")
        );
    }

    @Test
    void 목록을_조회한다() {
        // given & when
        List<Product> products = productRepository.findAll();

        // then
        assertThat(products).hasSize(6)
                .usingRecursiveComparison()
                .ignoringFields("price")
                .isEqualTo(Arrays.asList(
                                new Product(1L, "후라이드", price(16000)),
                                new Product(2L, "양념치킨", price(16000)),
                                new Product(3L, "반반치킨", price(16000)),
                                new Product(4L, "통구이", price(16000)),
                                new Product(5L, "간장치킨", price(17000)),
                                new Product(6L, "순살치킨", price(17000))
                        )
                );
    }

    private Price price(final int price) {
        return new Price(BigDecimal.valueOf(price));
    }

    @Test
    void ID로_상품을_조회한다() {
        // given
        Long id = 1L;

        // when
        Optional<Product> product = productRepository.findById(id);

        // then
        Assertions.assertAll(
                () -> assertThat(product).isPresent(),
                () -> assertThat(product.get().getName()).isEqualTo("후라이드")
        );
    }

    @Test
    void 일치하는_ID가_없는_경우_empty를_반환한다() {
        // given
        Long notExistId = -1L;

        // when
        Optional<Product> foundProduct = productRepository.findById(notExistId);

        // then
        assertThat(foundProduct).isEmpty();
    }
}
