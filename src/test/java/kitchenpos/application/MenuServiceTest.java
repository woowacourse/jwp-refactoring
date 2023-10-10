package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTestContext {

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 메뉴_가격이_0보다_작거나_같으면_예외를_던진다(int price) {
        // given
        Menu menu = new Menu();
        menu.setName("menuName");
        menu.setPrice(BigDecimal.valueOf(price));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹_아이디에_해당하는_메뉴_그룹이_없는_경우_예외를_던진다() {
        // given
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(1000));
        menu.setMenuGroupId(Long.MAX_VALUE);

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격이_실제_메뉴_상품들의_총_가격보다_크면_예외를_던진다() {
        // given
        Menu menu = new Menu();
        menu.setName("menuName");
        menu.setPrice(BigDecimal.valueOf(2001L));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(List.of(savedMenuProduct));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴를_정상적으로_생성하는_경우_생성한_메뉴가_반환된다() {
        // given
        Menu menu = new Menu();
        menu.setName("menuName");
        menu.setPrice(BigDecimal.valueOf(2000L));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(List.of(savedMenuProduct));

        // when
        Menu createdMenu = menuService.create(menu);

        // then
        assertThat(createdMenu.getId()).isNotNull();
        assertThat(createdMenu.getName()).isEqualTo(menu.getName());
    }

    @Test
    void 전체_메뉴를_조회할_수_있다() {
        // given
        Menu menu = new Menu();
        menu.setName("menuName");
        menu.setPrice(BigDecimal.valueOf(2000L));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(List.of(savedMenuProduct));

        menuService.create(menu);

        // when
        List<Menu> products = menuService.list();

        // then
        assertThat(products).hasSize(2);
    }
}