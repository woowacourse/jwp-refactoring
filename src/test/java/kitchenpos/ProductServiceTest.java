package kitchenpos;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("Product 테스트")
class ProductServiceTest {

    private static final int VALID_PRICE = 18_900;
    private static final int INVALID_PRICE = -1;

    @Autowired
    private ProductService productService;

    @DisplayName("Product 추가 테스트 - 성공")
    @Test
    void create() {
        //given
        Product product = Product.builder()
                .name("이달의 치킨")
                .price(BigDecimal.valueOf(VALID_PRICE))
                .build();
        //when
        Product create = productService.create(product);
        //then
        assertThat(create.getId()).isNotNull();
    }

    @DisplayName("Product 추가 테스트 - 실패 - 가격이 0보다 작은 경우")
    @Test
    void createFailureWhenInvalidPrice() {
        //given
        Product product = Product.builder()
                .name("이달의 치킨")
                .price(BigDecimal.valueOf(INVALID_PRICE))
                .build();
        Product nullProduct = Product.builder()
                .name("이달의 치킨")
                .price(null)
                .build();
        //when
        //then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> productService.create(nullProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 Product 반환")
    @Test
    void list() {
        //given
        //when
        List<Product> products = productService.list();
        //then
        assertThat(products).isNotEmpty();
    }
}
