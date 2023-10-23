package kitchenpos.domain;

import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_두마리메뉴;
import static kitchenpos.fixture.MenuProductFixture.메뉴상품;
import static kitchenpos.fixture.ProductFixture.양념치킨_16000;
import static kitchenpos.fixture.ProductFixture.후라이드_16000;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Nested
    class addMenuProducts {

        @Test
        void 이름과_가격과_메뉴그룹을_가지는_메뉴에_메뉴상품을_추가할_수_있다() {
            // given
            final var 두마리메뉴 = 메뉴그룹_두마리메뉴;

            final var 후라이드 = 후라이드_16000;
            final var 양념치킨 = 양념치킨_16000;

            final var 후라이드_1개 = 메뉴상품(후라이드, 1);
            final var 양념치킨_1개 = 메뉴상품(양념치킨, 1);

            final var menu = new Menu("후라이드양념", new Price(BigDecimal.valueOf(25000)), 두마리메뉴);

            // when
            menu.addMenuProducts(List.of(후라이드_1개, 양념치킨_1개));

            // then
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

            final var menu = new Menu("후라이드양념", new Price(BigDecimal.valueOf(35000)), 두마리메뉴);

            // when & then
            assertThatThrownBy(() -> menu.addMenuProducts(List.of(후라이드_1개, 양념치킨_1개)));
        }
    }
}
