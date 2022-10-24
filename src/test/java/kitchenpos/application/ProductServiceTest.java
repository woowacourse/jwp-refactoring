package kitchenpos.application;

import static kitchenpos.common.fixtures.ProductFixtures.야채곱창_가격;
import static kitchenpos.common.fixtures.ProductFixtures.야채곱창_이름;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.common.builder.ProductBuilder;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    void 상품을_등록한다() {
        // given
        Product 야채곱창 = new ProductBuilder()
                .name(야채곱창_이름)
                .price(야채곱창_가격)
                .build();

        // when
        Product actual = productService.create(야채곱창);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(야채곱창_이름),
                () -> assertThat(actual.getPrice()).isEqualTo(야채곱창_가격)
        );
    }
}