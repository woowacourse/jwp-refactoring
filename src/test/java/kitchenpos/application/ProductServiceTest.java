package kitchenpos.application;

import static kitchenpos.application.ProductFixture.PRODUCT_NAME;
import static kitchenpos.application.ProductFixture.PRODUCT_PRICE;
import static kitchenpos.application.ProductFixture.UNSAVED_PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Stream;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 만든다.")
    @Test
    void create() {
        Product savedProduct = productService.create(UNSAVED_PRODUCT);
        List<Product> products = productService.list();

        assertThat(products).contains(savedProduct);
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
}
