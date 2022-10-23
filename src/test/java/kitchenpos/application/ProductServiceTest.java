package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
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
        Product product = new Product();
        BigDecimal price = BigDecimal.valueOf(3000);
        String name = "맘모스빵";
        product.setPrice(price);
        product.setName(name);

        Product savedProduct = productService.create(product);
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
        BigDecimal price = BigDecimal.valueOf(3000);
        String name = "맘모스빵";

        Product noNameProduct = new Product();
        noNameProduct.setPrice(price);
        Product noPriceProduct = new Product();
        noPriceProduct.setName(name);

        return Stream.of(
                Arguments.of(noNameProduct),
                Arguments.of(noPriceProduct)
        );
    }
}
