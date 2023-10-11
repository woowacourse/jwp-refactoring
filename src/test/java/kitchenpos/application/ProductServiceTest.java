package kitchenpos.application;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@DirtiesContext
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Nested
    class 상품등록_테스트 {

        @Test
        void 상품을_등록할_수_있다() {
            Product product = new Product(null, "product", BigDecimal.ZERO);

            Product saved = productService.create(product);

            assertThat(saved.getId()).isNotNull();
        }

        @Test
        void 상품의_가격이_0원_이상이_아니면_등록할_수_없다() {
            Product product = new Product(null, "product", BigDecimal.valueOf(-1L));

            assertThatThrownBy(() -> productService.create(product))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품의 가격은 0원 이상이어야 합니다.");
        }
    }

    @Test
    void 상품의_목록을_조회할_수_있다() {
        Product product1 = new Product(null, "product1", BigDecimal.ZERO);
        Product product2 = new Product(null, "product2", BigDecimal.ONE);
        Product product3 = new Product(null, "product3", new BigDecimal("1000"));
        productService.create(product1);
        productService.create(product2);
        productService.create(product3);

        List<Product> list = productService.list();

        assertAll(
                () -> assertThat(list).hasSize(3),
                () -> assertThat(list).extracting("name")
                        .contains("product1", "product2", "product3")
        );
    }
}
