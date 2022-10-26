package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@ApplicationTest
class ProductServiceTest {

    private final ProductDao productDao;
    private final ProductService productService;

    ProductServiceTest(ProductDao productDao, ProductService productService) {
        this.productDao = productDao;
        this.productService = productService;
    }

    @Test
    void 상품을_생성한다() {
        Product product = new Product("맥도날드 페페로니 피자 버거", new BigDecimal(7_300));

        Product actual = productService.create(product);
        assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
    }

    @Test
    void 생성할때_가격이_존재하지_않는_경우_예외를_발생시킨다() {
        Product product = new Product("맥도날드 페페로니 피자 버거", null);

        assertThatThrownBy(() -> productService.create(product))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성할때_가격이_0보다_작은_경우_예외를_발생시킨다() {
        Product product = new Product("맥도날드 페페로니 피자 버거", new BigDecimal(-1));

        assertThatThrownBy(() -> productService.create(product))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_상품을_조회한다() {
        productDao.save(new Product());

        List<Product> products = productService.list();

        assertThat(products).hasSizeGreaterThanOrEqualTo(1);
    }
}
