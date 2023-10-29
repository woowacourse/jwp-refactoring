package kitchenpos.domain.menuproduct;

import kitchenpos.DomainTest;
import kitchenpos.menu.domain.MenuProductQuantity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DomainTest
class MenuProductQuantityTest {
    @ParameterizedTest
    @ValueSource(longs = {0, 1, 255})
    void 메뉴_상품_수량은_0_이상이다(long quantity) {
        final MenuProductQuantity menuProductQuantity = new MenuProductQuantity(quantity);
        assertThat(menuProductQuantity.getQuantity()).isEqualTo(quantity);
    }

    @Test
    void 메뉴_상품_수량이_음수이면_예외가_발생한다() {
        assertThatThrownBy(() -> new MenuProductQuantity(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
