package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
public class ProductTest {

    @Test
    void Id_상품이름_가격을_받아서_상품정보를_등록할_수_있다() {
        // given
        Long id = 1L;
        String name = "강정치킨";
        BigDecimal price = BigDecimal.valueOf(17000);

        // when
        Product product = new Product(id, name, price);

        // then
        assertThat(product.getId()).isEqualTo(id);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(price);
    }

    @Test
    void 상품_가격이_0_미만이면_예외처리한다() {
        // given
        Long id = 1L;
        String name = "강정치킨";
        BigDecimal price = BigDecimal.valueOf(-1000);

        // when
        assertThatThrownBy(() -> new Product(id, name, price))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
