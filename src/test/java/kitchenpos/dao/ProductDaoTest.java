package kitchenpos.dao;

import static kitchenpos.fixture.ProductFixture.PRODUCT_FIXTURE_1;
import static kitchenpos.fixture.ProductFixture.PRODUCT_FIXTURE_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductDaoTest {

    @Autowired
    private ProductDao productDao;

    @Test
    void save() {
        Product product = PRODUCT_FIXTURE_1;

        Product persistProduct = productDao.save(product);

        assertAll(
            () -> assertThat(persistProduct.getId()).isNotNull(),
            () -> assertThat(persistProduct.getName()).isEqualTo(product.getName()),
            () -> assertThat(persistProduct.getPrice().longValue()).isEqualTo(product.getPrice().longValue())
        );
    }

    @Test
    void findById() {
        Product persistProduct = productDao.save(PRODUCT_FIXTURE_1);

        Product findProduct = productDao.findById(persistProduct.getId()).get();

        assertThat(persistProduct).isEqualToComparingFieldByField(findProduct);
    }

    @Test
    void findAll() {
        productDao.save(PRODUCT_FIXTURE_1);
        productDao.save(PRODUCT_FIXTURE_2);

        List<Product> products = productDao.findAll();
        List<String> productNames = products.stream().
            map(Product::getName)
            .collect(Collectors.toList());

        assertThat(productNames).contains(PRODUCT_FIXTURE_1.getName(), PRODUCT_FIXTURE_2.getName());
    }
}
