package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.fixtures.ProductFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @Test
    @DisplayName("상품을 생성한다")
    void create() {
        // given
        final Product product = ProductFixtures.CHICKEN.create();

        // when
        final Product saved = productService.create(product);

        // then
        assertAll(
                () -> assertThat(saved.getId()).isNotNull(),
                () -> assertThat(saved.getName()).isEqualTo("치킨"),
                () -> assertThat(saved.getPrice()).isEqualByComparingTo(new BigDecimal(10_000))
        );
    }

    @Test
    @DisplayName("상품의 가격이 없다면 상품을 생성할 때 예외가 발생한다")
    void createWithEmptyPrice() {
        // given
        final Product product = ProductFixtures.CHICKEN.createWithPrice(null);

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품의 가격이 0원보다 작으면 상품을 생성할 때 예외가 발생한다")
    void createWithWrongPrice() {
        // given
        final Product product = ProductFixtures.CHICKEN.createWithPrice(new BigDecimal(-1));

        // when, then
        assertThatThrownBy(() -> productService.create(product))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("모든 상품을 조회한다")
    void list() {
        // given
        final Product product = ProductFixtures.CHICKEN.create();
        final Product saved = productDao.save(product);

        // when
        final List<Product> products = productService.list();

        // then
        assertAll(
                () -> assertThat(products).hasSizeGreaterThanOrEqualTo(1),
                () -> assertThat(products).extracting("id")
                        .contains(saved.getId())
        );
    }
}
