package kitchenpos.application;

import static kitchenpos.application.fixture.ProductFixture.INVALID_PRODUCT_PRICE;
import static kitchenpos.application.fixture.ProductFixture.PRODUCT_NAME;
import static kitchenpos.application.fixture.ProductFixture.PRODUCT_PRICE;
import static kitchenpos.application.fixture.ProductFixture.UNSAVED_PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

    @DisplayName("이름이나 가격이 없으면 예외가 발생한다.")
    @ParameterizedTest
    @MethodSource("argsOfCreateException")
    void create_Exception(Product wrongProduct) {
        assertThatThrownBy(() -> productService.create(wrongProduct))
                .isInstanceOf(Exception.class);
    }

    static Stream<Arguments> argsOfCreateException() {
        return Stream.of(
                Arguments.of(new Product(null, PRODUCT_PRICE)),
                Arguments.of(new Product(PRODUCT_NAME, null))
        );
    }

    @DisplayName("상품 가격이 0원 미만이면 예외가 발생한다.")
    @Test
    void create_Exception_Invalid_Price() {
        Product wrongPriceProduct = new Product(PRODUCT_NAME, INVALID_PRODUCT_PRICE);
        assertThatThrownBy(() -> productService.create(wrongPriceProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
