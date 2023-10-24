package kitchenpos.domain.menu;

import kitchenpos.exception.KitchenposException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static kitchenpos.exception.ExceptionInformation.MENU_QUANTITY_OUT_OF_BOUNCE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("메뉴에 속하는 상품 개수 테스트")
class QuantityTest {

    @Test
    void 개수는_음수일수_없다() {
        assertThatThrownBy(() -> Quantity.create(-1))
                .isExactlyInstanceOf(KitchenposException.class)
                .hasMessage(MENU_QUANTITY_OUT_OF_BOUNCE.getMessage());
    }
}
