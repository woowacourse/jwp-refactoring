package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static kitchenpos.fixture.MenuFixture.createMenuWithId;
import static kitchenpos.fixture.MenuProductFixture.createMenuProductWithMenu;
import static kitchenpos.fixture.MenuProductFixture.createMenuProductWithProductAndQuantity;
import static kitchenpos.fixture.ProductFixture.createProductWithPrice;
import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    @DisplayName("calculateAmount, price * quantity를 잘 반환하는지 테스트")
    @Test
    void calculateAmount() {
        Product product = createProductWithPrice(BigDecimal.TEN);
        MenuProduct menuProduct = createMenuProductWithProductAndQuantity(product, 10L);

        BigDecimal actual = menuProduct.calculateAmount();

        BigDecimal expected = BigDecimal.valueOf(100L);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("equalsByMenuId, MenuProduct의 Menu id와 같은지 비교")
    @Test
    void equalsByMenuId() {
        Menu menu = createMenuWithId(1L);
        MenuProduct menuproduct = createMenuProductWithMenu(menu);

        boolean actual = menuproduct.equalsByMenuId(1L);

        assertThat(actual).isTrue();
    }
}