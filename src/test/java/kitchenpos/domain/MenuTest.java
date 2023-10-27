package kitchenpos.domain;

import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_두마리메뉴;
import static kitchenpos.fixture.MenuProductFixture.메뉴상품;
import static kitchenpos.fixture.ProductFixture.양념치킨_16000;
import static kitchenpos.fixture.ProductFixture.후라이드_16000;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.Price;
import kitchenpos.menu.domain.Menu;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void 메뉴는_이름과_가격과_메뉴그룹과_메뉴상품리스트를_가진다() {
        // given
        final var 두마리메뉴 = 메뉴그룹_두마리메뉴;

        final var 후라이드 = 후라이드_16000;
        final var 양념치킨 = 양념치킨_16000;

        final var 후라이드_1개 = 메뉴상품(후라이드, 1);
        final var 양념치킨_1개 = 메뉴상품(양념치킨, 1);

        // when
        final var menu = new Menu("후라이드양념", new Price(BigDecimal.valueOf(25000)), 두마리메뉴, List.of(후라이드_1개, 양념치킨_1개));

        // then
        assertThat(menu.getName()).isEqualTo("후라이드양념");
        assertThat(menu.getPrice()).isEqualTo(new Price(BigDecimal.valueOf(25000)));
        assertThat(menu.getMenuGroup()).isEqualTo(두마리메뉴);
        assertThat(menu.getMenuProducts()).isEqualTo(List.of(후라이드_1개, 양념치킨_1개));
    }

    @Test
    void 메뉴상품의_합보다_메뉴의_가격이_클_수는_없다() {
        // given
        final var 두마리메뉴 = 메뉴그룹_두마리메뉴;

        final var 후라이드 = 후라이드_16000;
        final var 양념치킨 = 양념치킨_16000;

        final var 후라이드_1개 = 메뉴상품(후라이드, 1);
        final var 양념치킨_1개 = 메뉴상품(양념치킨, 1);

        // when & then
        assertThatThrownBy(() -> new Menu("후라이드양념", new Price(BigDecimal.valueOf(35000)), 두마리메뉴, List.of(후라이드_1개, 양념치킨_1개)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 상품가격 * 수량의 합보다 작거나 같아야한다");
    }
}
