package kitchenpos.application;

import kitchenpos.common.service.ServiceTest;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;


    @Test
    void Product를_생성할_수_있다() {
        //when
        final Product product = productService.create(new Product("치킨", new BigDecimal(20000)));

        //then
        assertThat(product.getId()).isNotNull();
    }

    @Test
    void price가_null이면_예외가_발생한다() {
        //when, then
        Assertions.assertThatThrownBy(() -> productService.create(new Product("치킨", null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void price가_0원보다_작으면_예외가_발생한다() {
        //when, then
        Assertions.assertThatThrownBy(() -> productService.create(new Product("치킨", new BigDecimal(-1))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Product_를_조회할_수_있다() {
        //when
        final List<Product> list = productService.list();

        //then
        assertThat(list).hasSize(6);
    }
}
