package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.Product;
import kitchenpos.vo.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_저장한다() {
        // given
        String name = "상품";
        long price = 1_000L;

        // when
        Product result = productService.create(name, price);

        // then
        assertThat(result.name()).isEqualTo("상품");
        assertThat(result.price()).isEqualTo(new Price(1000L));
    }
}
