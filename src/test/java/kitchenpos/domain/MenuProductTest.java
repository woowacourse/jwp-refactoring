package kitchenpos.domain;

import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    @DisplayName("메뉴상품의 가격 총합을 구할 수 있다.")
    void getMenuProductPrice_success() {
        MenuProduct wuga = new MenuProduct(Product.of("wuga", BigDecimal.valueOf(1000)), 10);

        Assertions.assertThat(wuga.getMenuProductPrice()).isEqualTo(BigDecimal.valueOf(10000));
    }
}
