package kitchenpos.application;

import static kitchenpos.DomainFixture.createProduct;
import static kitchenpos.DomainFixture.양념치킨;
import static kitchenpos.DomainFixture.피자;
import static kitchenpos.DomainFixture.후라이드치킨;
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
        final Product product = createProduct("김피탕", 10_000);

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
    @DisplayName("create -> 상품의 가격이 0원 미만이면 예외가 발생한다.")
    void create_invalidPrice_throwException() {
        // given
        final Product product = createProduct("김피탕", -1);

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품을 조회한다.")
    void list() {
        // given
        상품등록(후라이드치킨);
        상품등록(양념치킨);
        상품등록(피자);

        // when
        final List<Product> list = productService.list();

        // then
        assertThat(list).hasSize(3);
    }
}
