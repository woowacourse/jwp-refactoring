package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("REPOSITORY_ProductRepository 테스트")
@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest(showSql = false)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Nested
    class save_메서드는 {

        @Nested
        class 상품이_주어지면 {

            final Product product = new Product("파스타", BigDecimal.valueOf(1_000L));

            @Test
            void 저장한다() {
                final Product savedProduct = productRepository.save(product);

                assertThat(savedProduct.getId()).isNotNull();
            }
        }
    }

    @Nested
    class findById_메서드는 {

        @Nested
        class id가_주어지면 {

            final Product product = new Product("파스타", BigDecimal.valueOf(1_000L));
            private Product savedProduct;

            @BeforeEach
            void setUp() {
                savedProduct = productRepository.save(product);
            }

            @Test
            void 저장한다() {
                final Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

                assertAll(
                        () -> assertThat(foundProduct).isPresent(),
                        () -> assertThat(foundProduct.get()).usingRecursiveComparison()
                                .usingDefaultComparator()
                                .withComparatorForType(Comparator.comparing(BigDecimal::intValue), BigDecimal.class)
                                .isEqualTo(savedProduct)
                );
            }
        }
    }

    @Nested
    class findAll_메서드는 {

        @Nested
        class 호출되면 {

            final Product product = new Product("파스타", new BigDecimal(1000));
            private Product savedProduct;

            @BeforeEach
            void setUp() {
                savedProduct = productRepository.save(product);
            }

            @Test
            void 모든_상품들을_반환한다() {
                final List<Product> products = productRepository.findAll();

                assertThat(products).usingFieldByFieldElementComparator()
                        .usingComparatorForType(Comparator.comparing(BigDecimal::intValue), BigDecimal.class)
                        .containsAll(List.of(savedProduct));
            }
        }
    }
}
