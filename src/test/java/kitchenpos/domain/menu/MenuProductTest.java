package kitchenpos.domain.menu;

import static kitchenpos.support.TestFixtureFactory.메뉴_상품을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.domain.common.Price;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    void 금액을_계산할_수_있다() {
        MenuProduct menuProduct = 메뉴_상품을_생성한다(1L, 10, new Price(BigDecimal.ONE));

        Price amount = menuProduct.getAmount();

        assertThat(amount).isEqualTo(new Price(BigDecimal.TEN));
    }
}
