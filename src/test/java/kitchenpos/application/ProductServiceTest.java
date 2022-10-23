package kitchenpos.application;

import static kitchenpos.application.fixture.ProductFixture.PRODUCT_짜장면;
import static kitchenpos.application.fixture.ProductFixture.PRODUCT_짬뽕;
import static kitchenpos.application.fixture.ProductFixture.PRODUCT_탕수육;
import static kitchenpos.application.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductServiceTest extends ServiceTest {

    @Test
    @DisplayName("상품을 등록한다.")
    void create() {
        // given
        final Product product = PRODUCT_짜장면.create();

        // when
        final Product actual = productService.create(product);

        // then
        final Long actualId = actual.getId();
        assertAll(
                () -> assertThat(actualId).isNotNull(),
                () -> assertThat(productDao.findById(actualId)).isPresent()
        );
    }

    @Test
    @DisplayName("상품의 가격이 0원 미만이면 예외가 발생한다.")
    void create_invalidPrice_throwException() {
        // given
        final Product product = createProduct("짜장면", -1);

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품을 조회한다.")
    void list() {
        // given
        productDao.save(PRODUCT_짜장면.create());
        productDao.save(PRODUCT_짬뽕.create());
        productDao.save(PRODUCT_탕수육.create());

        // when
        final List<Product> list = productService.list();

        // then
        assertThat(list).hasSize(3);
    }
}
