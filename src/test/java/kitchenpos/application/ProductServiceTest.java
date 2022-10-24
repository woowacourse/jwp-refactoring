package kitchenpos.application;

import java.math.BigDecimal;
import kitchenpos.common.builder.ProductBuilder;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @DisplayName("상품을 등록한다.")
    @Test
    void 상품을_등록한다() {
        // given
        Product 야채곱창 = new ProductBuilder()
                .name("야채곱창")
                .price(new BigDecimal(10000))
                .build();


    }
}