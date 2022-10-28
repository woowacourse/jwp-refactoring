package kitchenpos.dao;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DaoTest
class JdbcTemplateProductDaoTest {

    private final JdbcTemplateProductDao productDao;

    @Autowired
    public JdbcTemplateProductDaoTest(final DataSource dataSource) {
        this.productDao = new JdbcTemplateProductDao(dataSource);
    }

    @DisplayName("Insert 쿼리 테스트")
    @Nested
    class InsertTest {

        @Test
        void save() {
            final var product = new Product("탕수육", 1000);
            final var actual = productDao.save(product);

            assertThat(actual.getId()).isPositive();
            assertThat(actual.getName()).isEqualTo(product.getName());
            assertThat(actual.getPrice()).isEqualByComparingTo(product.getPrice());
        }
    }

    @DisplayName("Select 쿼리 테스트")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class SelectTest {

        private final Map<Long, Product> savedProducts = saveAll(
                new Product("자장면", 4500L),
                new Product("짬뽕", 5000L),
                new Product("탕수육", 10000L)
        );

        private Map<Long, Product> saveAll(final Product... products) {
            return Stream.of(products)
                    .map(productDao::save)
                    .collect(Collectors.toMap(Product::getId, product -> product));
        }

        @Test
        void findAll() {
            final var actual = productDao.findAll();
            final var expected = DaoUtils.asList(savedProducts);
            DaoUtils.assertAllEquals(actual, expected, JdbcTemplateProductDaoTest.this::assertEquals);
        }

        @Nested
        class findById {

            @ParameterizedTest(name = "success")
            @ValueSource(longs = 1L)
            void success(final long id) {
                assert savedProducts.containsKey(id);

                final var actual = productDao.findById(id);
                assertThat(actual).isPresent();
                assertEquals(actual.get(), savedProducts.get(id));
            }

            @ParameterizedTest(name = "fail")
            @ValueSource(longs = 10L)
            void fail(final long id) {
                assert !savedProducts.containsKey(id);

                final var actual = productDao.findById(id);
                assertThat(actual).isEmpty();
            }
        }
    }

    private void assertEquals(final Product actual, final Product expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getPrice()).isEqualByComparingTo(expected.getPrice());
    }
}
