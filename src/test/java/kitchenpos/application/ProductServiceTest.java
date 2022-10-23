package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class ProductServiceTest {

    private final ProductService productService;
    private final ProductDao productDao;

    @Autowired
    public ProductServiceTest(final ProductService productService, final ProductDao productDao) {
        this.productService = productService;
        this.productDao = productDao;
    }

    @Test
    void createWithPositivePrice() {
        final var positivePrice = 1;

        final var expected = new Product("탕수육", positivePrice);
        final var actual = productDao.save(expected);

        assertThat(actual.getId()).isPositive();
        assertProductEqualsWithoutId(actual, expected);
    }

    @Test
    void createWithZeroPrice() {
        final var zeroPrice = 0;

        final var expected = new Product("탕수육", zeroPrice);
        final var actual = productDao.save(expected);

        assertThat(actual.getId()).isPositive();
        assertProductEqualsWithoutId(actual, expected);
    }

    @Test
    void createWithNegativePrice() {
        final var negativePrice = -1;

        final var product = new Product("탕수육", negativePrice);

        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void assertProductEqualsWithoutId(final Product actual, final Product expected) {
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getPrice()).isEqualByComparingTo(expected.getPrice());
    }

    @Test
    void list() {
        final List<Product> expected = Map.of("자장면", 4500, "짬뽕", 5000, "탕수육", 10000)
                .entrySet()
                .stream()
                .map(entry -> new Product(entry.getKey(), entry.getValue()))
                .map(productDao::save)
                .collect(Collectors.toUnmodifiableList());
        final List<Product> actual = productService.list();

        assertAllMatches(actual, expected);
    }

    private void assertAllMatches(final List<Product> actualList, final List<Product> expectedList) {
        final var expectedSize = actualList.size();
        assertThat(expectedList).hasSize(expectedSize);

        for (int i = 0; i < expectedSize; i++) {
            final var actual = actualList.get(i);
            final var expected = expectedList.get(i);

            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }
}