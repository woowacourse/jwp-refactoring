package kitchenpos.domain;

import static kitchenpos.support.TestFixtureFactory.새로운_메뉴;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_그룹;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_상품;
import static kitchenpos.support.TestFixtureFactory.새로운_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuTest {

    @Test
    void 메뉴의_이름은_255자_이하여야_한다() {
        assertThatThrownBy(() -> 새로운_메뉴("짱".repeat(256), BigDecimal.ONE, 새로운_메뉴_그룹(null, "메뉴 그룹")))
                .isInstanceOf(MenuException.class)
                .hasMessage("메뉴의 이름이 유효하지 않습니다.");

    }

    @ValueSource(strings = {"-1", "100000000000000000000"})
    @ParameterizedTest
    void 메뉴의_가격은_0원_이상_100조원_미만이어야_한다(String price) {
        assertThatThrownBy(() -> 새로운_메뉴("메뉴", new BigDecimal(price), 새로운_메뉴_그룹(null, "메뉴 그룹")))
                .isInstanceOf(PriceException.class)
                .hasMessage("가격이 유효하지 않습니다.");
    }

    @Test
    void 메뉴_상품을_추가한다() {
        Menu 메뉴 = 새로운_메뉴("메뉴", BigDecimal.ONE, 새로운_메뉴_그룹(null, "메뉴 그룹"));
        MenuProduct 상품1 = 새로운_메뉴_상품(메뉴, 새로운_상품(null, "상품1", BigDecimal.TEN), 1);
        MenuProduct 상품2 = 새로운_메뉴_상품(메뉴, 새로운_상품(null, "상품2", BigDecimal.TEN), 1);
        메뉴.addMenuProducts(List.of(상품1, 상품2));

        assertThat(메뉴.getMenuProducts())
                .hasSize(2)
                .usingRecursiveComparison()
                .isEqualTo(List.of(상품1, 상품2));
    }

    @Test
    void 메뉴_상품의_가격의_총합이_메뉴의_가격보다_커야한다() {
        Menu 메뉴 = 새로운_메뉴("메뉴", BigDecimal.valueOf(1000000L), 새로운_메뉴_그룹(null, "메뉴 그룹"));
        MenuProduct 상품1 = 새로운_메뉴_상품(메뉴, 새로운_상품(null, "상품1", BigDecimal.TEN), 1);
        MenuProduct 상품2 = 새로운_메뉴_상품(메뉴, 새로운_상품(null, "상품2", BigDecimal.TEN), 1);

        assertThatThrownBy(() -> 메뉴.addMenuProducts(List.of(상품1, 상품2)))
                .isInstanceOf(MenuException.class)
                .hasMessage("메뉴 상품의 가격의 총합이 메뉴의 가격보다 작습니다.");
    }
}
