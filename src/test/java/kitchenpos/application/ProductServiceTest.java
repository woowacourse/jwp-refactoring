package kitchenpos.application;

import static kitchenpos.application.fixture.ProductFixture.PRODUCT_PRICE;
import static kitchenpos.application.fixture.ProductFixture.UNSAVED_PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

    @DisplayName("상품을 만든다.")
    @Test
    void create() {
        Product savedProduct = productService.create(UNSAVED_PRODUCT);
        Optional<Product> foundProduct = productDao.findById(savedProduct.getId());

        assertThat(foundProduct).isPresent();
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void list() {
        int numberOfSavedProductBeforeCreate = productService.list().size();
        productService.create(UNSAVED_PRODUCT);

        int numberOfSavedProduct = productService.list().size();

        assertThat(numberOfSavedProductBeforeCreate + 1).isEqualTo(numberOfSavedProduct);
    }

    @DisplayName("이름이 없으면 예외가 발생한다.")
    @Test
    void create_Exception() {
        assertThatThrownBy(() -> productService.create(new Product(null, PRODUCT_PRICE)))
                .isInstanceOf(Exception.class);
    }
}
