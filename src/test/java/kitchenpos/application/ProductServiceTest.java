package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.support.domain.ProductTestSupport;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService target;

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        //given
        final Product product = ProductTestSupport.builder().build();

        //when

        //then
        Assertions.assertDoesNotThrow(() -> target.create(product));
    }

    @DisplayName("상품의 가격이 음수이면 예외 처리한다.")
    @Test
    void create_fail_price_minus() {
        //given
        final BigDecimal price = new BigDecimal("-1");
        final Product product = ProductTestSupport.builder().price(price).build();

        //when

        //then
        assertThatThrownBy(() -> target.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 모든 상품을 조회한다.")
    @Test
    void list() {
        //given
        final BigDecimal price1 = new BigDecimal("1000");
        final Product product1 = ProductTestSupport.builder().price(price1).build();
        final Product product2 = ProductTestSupport.builder().build();
        target.create(product1);
        target.create(product2);

        //when
        final List<Product> result = target.list();

        //then
        assertThat(result).extracting(Product::getName)
                .contains(product1.getName(), product2.getName());
    }
}
