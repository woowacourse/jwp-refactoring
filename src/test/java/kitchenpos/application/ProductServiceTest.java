package kitchenpos.application;

import kitchenpos.domain.Product;
import kitchenpos.fixture.Fixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProductServiceTest extends ServiceBaseTest {

    @Autowired
    protected ProductService productService;

    @Test
    @DisplayName("상품을 생성할 수 있다.")
    void create() {
        //given
        final Product product = Fixture.product("오션", 5000);

        //when
        final Product createdProduct = productService.create(product);

        //then
        assertAll(
                () -> assertThat(createdProduct.getName()).isEqualTo(product.getName()),
                () -> assertThat(createdProduct.getPrice().intValue()).isEqualTo(product.getPrice().intValue())
        );
    }

    @Test
    @DisplayName("상품의 가격은 0원 이상이어야 한다.")
    void priceOverZero() {
        //given
        final Product product = Fixture.product("오션", -1);

        //when&then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 목록을 조회할 수 있다.")
    void list() {
        //given
        final Product savedProduct = productService.create(Fixture.product("오션", 10000));

        //when
        final List<Product> products = productService.list();

        //then
        assertAll(
                () -> assertThat(products).hasSize(1),
                () -> assertThat(products.get(0).getName()).isEqualTo(savedProduct.getName()),
                () -> assertThat(products.get(0).getPrice().intValue()).isEqualTo(savedProduct.getPrice().intValue())
        );
    }
}
