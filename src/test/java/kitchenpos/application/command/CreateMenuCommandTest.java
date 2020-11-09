package kitchenpos.application.command;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.model.menu.MenuProduct;

class CreateMenuCommandTest extends CommandTest {
    @DisplayName("메뉴 요청 유효성 검사")
    @Test
    void validation() {
        CreateMenuCommand request = new CreateMenuCommand("후라이드+후라이드", BigDecimal.valueOf(19_000L),
                1L,
                singletonList(new MenuProduct(null, null, 1L, 2L)));
        CreateMenuCommand badRequest = new CreateMenuCommand("", BigDecimal.valueOf(-1L), null,
                emptyList());

        assertAll(
                () -> assertThat(validator.validate(request).isEmpty()).isTrue(),
                () -> assertThat(validator.validate(badRequest).size()).isEqualTo(4)
        );
    }
}