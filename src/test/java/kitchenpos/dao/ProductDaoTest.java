package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("ProductDao 테스트")
@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class ProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @Nested
    class save_메서드는 {

        @Nested
        class 상품이_주어지면 {

            final Product product = new Product("파스타", new BigDecimal(1000));

            @Test
            void 저장한다() {
                final Product savedProduct = productDao.save(product);

                assertThat(savedProduct.getId()).isNotNull();
            }
        }
    }

    @Nested
    class findById_메서드는 {

        @Nested
        class id가_주어지면 {

            final Product product = new Product("파스타", new BigDecimal(1000));
            private Product savedProduct;

            @BeforeEach
            void setUp() {
                savedProduct = productDao.save(product);
            }

            @Test
            void 저장한다() {
                final Optional<Product> foundProduct = productDao.findById(savedProduct.getId());

                assertAll(
                        () -> assertThat(foundProduct).isPresent(),
                        () -> assertThat(foundProduct.get()).usingRecursiveComparison()
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
                savedProduct = productDao.save(product);
            }

            @Test
            void 모든_상품들을_반환한다() {
                final List<Product> products = productDao.findAll();

                assertThat(products).usingFieldByFieldElementComparator()
                        .containsAll(List.of(savedProduct));
            }
        }
    }
}
