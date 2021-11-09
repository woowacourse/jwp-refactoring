package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @DisplayName("객체 생성 : 객체 정상 생성된다.")
    @Test
    void create() {
        // given
        Long id = 1L;
        String name = "후라이드 치킨 한마리";
        BigDecimal price = new BigDecimal(10000);

        // when
        Product product = new Product(id, name, price);

        // then
        assertThat(product.getId()).isEqualTo(id);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(price);
    }

    @DisplayName("객체 생성 : 가격은 null일 수 없다.")
    @Test
    void createWithNullPrice() {
        // given
        Long id = 1L;
        String name = "후라이드 치킨 한마리";

        // when then
        assertThatThrownBy(() -> new Product(id, name, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("객체 생성 : 가격은 음수일 수 없다.")
    @Test
    void createWithNegativePrice() {
        // given
        Long id = 1L;
        String name = "후라이드 치킨 한마리";
        BigDecimal price = new BigDecimal(-1);

        // when then
        assertThatThrownBy(() -> new Product(id, name, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
