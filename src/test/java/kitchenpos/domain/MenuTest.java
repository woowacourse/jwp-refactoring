package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuTest {

    @Test
    void 해당_메뉴의_상품이_아닌_것을_넣으면_예외() {
        // given
        Menu menu = new Menu(
            1L,
            "name",
            new Price(BigDecimal.valueOf(1000)),
            1L,
            new ArrayList<>());

        Menu anotherMenu = new Menu(
            2L,
            "nam2",
            new Price(BigDecimal.valueOf(2000)),
            1L,
            new ArrayList<>());

        List<MenuProduct> newMenuProducts = List.of(new MenuProduct(
            1L,
            new Product(1L, "product", new Price(BigDecimal.valueOf(1000))),
            anotherMenu,
            1L));

        // when && then
        Assertions.assertThatThrownBy(() -> menu.addMenuProducts(newMenuProducts))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 메뉴의 상품이 아닙니다.");
    }

}
