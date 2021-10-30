package kitchenpos.application;

import kitchenpos.annotation.IntegrationTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @ParameterizedTest
    @DisplayName("Product의 가격이 올바르지 않으면 등록할 수 없다.")
    @MethodSource("minusAndNullPrice")
    public void priceException(BigDecimal price) {
        //given
        Product product = new Product();

        //when
        product.setPrice(price);

        //then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> minusAndNullPrice() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(-1)),
                Arguments.of((Object) null)
        );
    }

    @Test
    @DisplayName("Product를 등록할 수 있다.")
    public void enrollProduct() {
        //given
        Product product = new Product();
        product.setName("진~~짜 맛있는 치킨");
        product.setPrice(BigDecimal.valueOf(1000));

        //when & then
        assertDoesNotThrow(() -> productService.create(product));
    }

    @Test
    @DisplayName("존재하는 Product를 조회할 수 있다.")
    public void findAll() {
        //given & when
        List<Product> products = productService.list();

        //then
        assertThat(products).hasSize(6);
    }
}