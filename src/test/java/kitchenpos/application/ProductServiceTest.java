package kitchenpos.application;

import static kitchenpos.common.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성한다() {
        // given
        Product product = 상품();

        // when
        Product createdProduct = productService.create(product);

        // then
        assertThat(createdProduct).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(상품());
    }

    @Test
    void 상품을_생성할_때_가격이_0미만이면_예외를_던진다() {
        // given
        Product invalidProduct = 상품("name", BigDecimal.valueOf(-1L));

        // expect
        assertThatThrownBy(() -> productService.create(invalidProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품을_생성할_때_가격이_null이면_예외를_던진다() {
        // given
        Product invalidProduct = 상품("name", null);

        // expect
        assertThatThrownBy(() -> productService.create(invalidProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_상품을_조회한다() {
        // given
        Long productId = productDao.save(상품()).getId();

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).usingRecursiveComparison()
                .isEqualTo(List.of(상품(productId)));
    }
}
