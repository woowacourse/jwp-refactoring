package kitchenpos.application.menu.dto;

import static kitchenpos.exception.menu.MenuExceptionType.MENU_PRODUCT_COMMANDS_CAN_NOT_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.junit.jupiter.api.Test;

class CreateMenuCommandTest {

    @Test
    void 메뉴_상품들이_널이면_예외가_발생한다() {
        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                new CreateMenuCommand(null, null, null, null)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(MENU_PRODUCT_COMMANDS_CAN_NOT_NULL);
    }
}
