package kitchenpos.application;

import static kitchenpos.fixture.ProductFixture.카레맛_떡볶이;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @Test
    void 상품_생성() {
        // given
        Product product = 카레맛_떡볶이.toEntity();

        // when
        Product savedProduct = productService.create(product);

        // then
        Optional<Product> actual = productDao.findById(savedProduct.getId());
        assertThat(actual).isNotEmpty();
    }
}
