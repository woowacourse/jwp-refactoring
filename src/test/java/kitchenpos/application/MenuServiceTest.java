package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTestHelper {

    @Test
    void 메뉴를_등록한다() {
        // given
        final Menu menu1 = 메뉴_등록("어른세트", 10500L, 세트, 아메리카노, 자바칩프라페);
        final Menu menu2 = 메뉴_등록("딸기에이드", 5500L, 에이드, 딸기에이드);

        // when
        final List<Menu> findMenus = 메뉴_목록_조회();
        final Menu findMenu1 = 메뉴_찾기(menu1.getId());
        final Menu findMenu2 = 메뉴_찾기(menu2.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(findMenus).usingElementComparatorIgnoringFields("menuProducts")
                    .contains(menu1, menu2);
            softly.assertThat(findMenu1.getMenuProducts()).extracting("productId")
                    .containsExactly(아메리카노.getId(), 자바칩프라페.getId());
            softly.assertThat(findMenu2.getMenuProducts()).extracting("productId")
                    .containsExactly(딸기에이드.getId());
        });
    }

    @Test
    void 메뉴_가격이_null이면_등록할_수_없다() {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 메뉴_등록("어른세트", null, 세트, 아메리카노, 자바칩프라페));
    }

    @Test
    void 메뉴_그룹이_존재하지_않으면_등록할_수_없다() {
        // given
        final MenuGroup emptyMenuGroup = new MenuGroup(-1L, "존재하지않는메뉴그룹");

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 메뉴_등록("어른세트", 15000L, emptyMenuGroup, 아메리카노, 자바칩프라페));
    }

    @Test
    void 메뉴상품_중_상품에_등록되어있지_않으면_예외가_발생한다() {
        // given
        final Product emptyProduct = Product.of("존재하지않는상품", BigDecimal.valueOf(1000L));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 메뉴_등록("어른세트", 15000L, 세트, emptyProduct));
    }

    @Test
    void 메뉴가격이_메뉴상품_가격의_합보다_크면_예외가_발생한다() {
        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 메뉴_등록("어른세트", 19001L, 세트, 아메리카노, 자바칩프라페));
    }

    @Test
    void 메뉴등록시_메뉴상품도_함께_등록된다() {
        // when
        final Menu menu = 메뉴_등록("어른세트", 9000L, 세트, 아메리카노, 자바칩프라페);
        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        // then
        assertThat(menuProducts).extracting("productId")
                .contains(아메리카노.getId(), 자바칩프라페.getId());
    }

    @ParameterizedTest
    @ValueSource(longs = {-5, -1})
    void 메뉴_가격이_0보다_작으면_등록할_수_없다(long price) {
        // when & then
        assertThatThrownBy(() -> 메뉴_등록("어른세트", price, 세트, 아메리카노, 자바칩프라페))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
