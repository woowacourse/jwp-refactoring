package kitchenpos.application;

import static kitchenpos.support.TestFixtureFactory.새로운_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_등록한다() {
        Product 상품 = 새로운_상품("상품", new BigDecimal("10000.00"));

        Product 등록된_상품 = productService.create(상품);

        assertSoftly(softly -> {
            softly.assertThat(등록된_상품.getId()).isNotNull();
            softly.assertThat(등록된_상품).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(상품);
        });
    }

    @Test
    void 상품의_이름은_최대_255자이다() {
        Product 상품 = 새로운_상품("짱".repeat(256), new BigDecimal(10000));

        assertThatThrownBy(() -> productService.create(상품))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품의_가격은_0원_이상이어야_한다() {
        Product 상품 = 새로운_상품("상품", new BigDecimal(-1));

        assertThatThrownBy(() -> productService.create(상품))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품의_가격은_100조원_미만이어야_한다() {
        Product 상품 = 새로운_상품("상품", BigDecimal.valueOf(Math.pow(10, 20)));

        assertThatThrownBy(() -> productService.create(상품))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품의_목록을_조회한다() {
        Product 등록된_상품 = productService.create(새로운_상품("상품", new BigDecimal(10000)));

        List<Product> 상품의_목록 = productService.readAll();

        assertThat(상품의_목록).hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .containsOnly(등록된_상품);
    }
}
