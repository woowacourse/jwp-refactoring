package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixture.createMenu;
import static kitchenpos.application.fixture.MenuGroupFixture.메뉴그룹A;
import static kitchenpos.application.fixture.MenuGroupFixture.메뉴그룹B;
import static kitchenpos.application.fixture.ProductFixture.짜장면;
import static kitchenpos.application.fixture.ProductFixture.짬뽕;
import static kitchenpos.application.fixture.ProductFixture.탕수육;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuServiceTest extends ServiceTest {

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        // given
        final MenuGroup menuGroup = 메뉴그룹등록(메뉴그룹A);
        final Product product = 상품등록(탕수육);

        final Menu menu = createMenu("탕수육_메뉴", 10_000, menuGroup, product);

        // when
        final Menu createdMenu = menuService.create(menu);

        // then
        final Long createdMenuId = createdMenu.getId();
        assertAll(
                () -> assertThat(createdMenuId).isNotNull(),
                () -> assertThat(menuDao.findById(createdMenuId)).isPresent()
        );
    }

    @Test
    @DisplayName("메뉴의 가격이 0원 미만이면 예외가 발생한다.")
    void create_invalidPrice_throwException() {
        // given
        final MenuGroup menuGroup = 메뉴그룹등록(메뉴그룹A);
        final Product product = 상품등록(탕수육);

        final Menu menu = createMenu("탕수육_메뉴", -1, menuGroup, product);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 그룹이 존재하지 않으면 예외가 발생한다.")
    void create_noGroup_throwException() {
        // given
        final MenuGroup notRegisteredMenuGroup = 메뉴그룹A;
        final Product product = 상품등록(탕수육);
        final Menu menu = createMenu("탕수육_메뉴", 10_000, notRegisteredMenuGroup, product);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴의 가격이 메뉴 상품들 가격의 합보다 클 경우 예외가 발생한다.")
    void create_overProductsPrice_throwException() {
        // given
        final MenuGroup menuGroup = 메뉴그룹등록(메뉴그룹A);
        final Product product1 = 상품등록(짜장면);
        final Product product2 = 상품등록(짬뽕);

        final Menu menu = createMenu("짜장짬뽕_메뉴", 30_000, menuGroup, product1, product2);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void list() {
        // given
        final MenuGroup menuGroup1 = 메뉴그룹등록(메뉴그룹A);
        final Product product1 = 상품등록(짜장면);
        final Menu menu1 = createMenu("짜장_메뉴", 8_000, menuGroup1, product1);
        메뉴등록(menu1);

        final MenuGroup menuGroup2 = 메뉴그룹등록(메뉴그룹B);
        final Product product2 = 상품등록(짬뽕);
        final Menu menu2 = createMenu("짬뽕_메뉴", 8_000, menuGroup2, product2);
        메뉴등록(menu2);

        // when
        final List<Menu> actual = menuService.list();

        // then
        assertThat(actual).hasSize(2);
    }
}
