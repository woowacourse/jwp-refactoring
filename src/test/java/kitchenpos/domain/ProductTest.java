package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    @DisplayName("아이디를 설정한다")
    void setId(){
        // given
        Product product = new Product();
        Long id = 999L;

        // when
        product.setId(id);

        // then
        assertThat(product.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("이름을 설정한다")
    void setName(){
        // given
        Product product = new Product();
        String name = "test";

        // when
        product.setName(name);

        // then
        assertThat(product.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("가격을 설정한다")
    void setPrice(){
        // given
        Product product = new Product();
        BigDecimal price = BigDecimal.ONE;

        // when
        product.setPrice(price);

        // then
        assertThat(product.getPrice()).isEqualTo(price);
    }
}
