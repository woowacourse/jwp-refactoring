package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품 금액이 null이면, 저장할 수 없다.")
    @Test
    void createFailTest_ByProductPriceIsNull() {
        //given
        Product product = new Product();
        product.setName("TestProduct");
        product.setPrice(null);

        //when then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "상품 금액이 0원 미만이면, 저장할 수 없다.")
    @ValueSource(ints = {-1000, -1})
    void createFailTest_ByProductPriceIsLessThanZero(int price) {
        //given
        Product product = new Product();
        product.setName("TestProduct");
        product.setPrice(BigDecimal.valueOf(price));

        //when then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void createSuccessTest() {
        //given
        Product product = new Product();
        product.setName("TestProduct");
        product.setPrice(BigDecimal.ZERO);

        //when
        Product savedProduct = productService.create(product);

        //then
        Product findProduct = productService.list()
                .stream()
                .filter(p -> p.getId().equals(savedProduct.getId()))
                .findAny()
                .get();

        assertThat(findProduct).usingRecursiveComparison()
                .isEqualTo(savedProduct);
    }

}
