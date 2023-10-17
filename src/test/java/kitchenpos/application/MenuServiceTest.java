package kitchenpos.application;

import kitchenpos.application.config.ServiceTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

class MenuServiceTest extends ServiceTestConfig {

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(menuDao, menuGroupDao, menuProductDao, productDao);
    }

    @DisplayName("메뉴 생성")
    @Nested
    class Create {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final MenuGroup menuGroup = saveMenuGroup();
            final Menu menuInput = new Menu();
            menuInput.setName("여우곰탕");
            menuInput.setPrice(BigDecimal.valueOf(0));
            menuInput.setMenuGroupId(menuGroup.getId());

            // FIXME: addMenuProducts 추가
            final Menu spyMenuInput = spy(menuInput);
            given(spyMenuInput.getMenuProducts()).willReturn(new ArrayList<>());

            // when
            final Menu actual = menuService.create(spyMenuInput);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getName()).isEqualTo(spyMenuInput.getName());
                softly.assertThat(actual.getPrice().compareTo(spyMenuInput.getPrice())).isZero();
                softly.assertThat(actual.getMenuGroupId()).isEqualTo(spyMenuInput.getMenuGroupId());
                softly.assertThat(actual.getMenuProducts()).isEqualTo(spyMenuInput.getMenuProducts());
            });
        }

        @DisplayName("가격이 null 이면 실패한다.")
        @Test
        void fail_if_price_is_null() {
            // given
            final MenuGroup menuGroup = saveMenuGroup();
            final Menu menuInput = new Menu();
            menuInput.setName("여우곰탕");
            menuInput.setPrice(null);
            menuInput.setMenuGroupId(menuGroup.getId());

            // FIXME: addMenuProducts 추가
            final Menu spyMenuInput = spy(menuInput);
            given(spyMenuInput.getMenuProducts()).willReturn(new ArrayList<>());

            // then
            assertThatThrownBy(() -> menuService.create(spyMenuInput))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 0원 미만이면 실패한다.")
        @Test
        void fail_if_price_under_zero() {
            // given
            final MenuGroup menuGroup = saveMenuGroup();
            final Menu menuInput = new Menu();
            menuInput.setName("여우곰탕");
            menuInput.setPrice(BigDecimal.valueOf(-1));
            menuInput.setMenuGroupId(menuGroup.getId());

            // FIXME: addMenuProducts 추가
            final Menu spyMenuInput = spy(menuInput);
            given(spyMenuInput.getMenuProducts()).willReturn(new ArrayList<>());

            // then
            assertThatThrownBy(() -> menuService.create(spyMenuInput))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 MenuGroup 이면 실패한다.")
        @Test
        void fail_if_menuGroup_not_exist() {
            // given
            final MenuGroup menuGroup = new MenuGroup(null, "없는 그룹");
            final Menu menuInput = new Menu();
            menuInput.setName("여우곰탕");
            menuInput.setPrice(BigDecimal.valueOf(-1));
            menuInput.setMenuGroupId(menuGroup.getId());

            // FIXME: addMenuProducts 추가
            final Menu spyMenuInput = spy(menuInput);
            given(spyMenuInput.getMenuProducts()).willReturn(new ArrayList<>());

            // then
            assertThatThrownBy(() -> menuService.create(spyMenuInput))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("Menu 가격이 Product 의 가격 합보다 크면 실패한다.")
        @Test
        void fail_if_menu_price_is_bigger_than_sum_of_product_price() {
            // given
            final MenuGroup menuGroup = saveMenuGroup();
            final Menu menuInput = new Menu();
            menuInput.setName("여우곰탕");
            menuInput.setPrice(BigDecimal.valueOf(10000));
            menuInput.setMenuGroupId(menuGroup.getId());

            // FIXME: addMenuProducts 추가
            final Menu spyMenuInput = spy(menuInput);
            given(spyMenuInput.getMenuProducts()).willReturn(new ArrayList<>());

            // then
            assertThatThrownBy(() -> menuService.create(spyMenuInput))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴 전체 조회")
    @Nested
    class ReadAll {
        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final MenuGroup menuGroup = saveMenuGroup();
            final Menu savedMenu = saveMenu(menuGroup);

            // when
            final List<Menu> actual = menuService.list();

            // then
            // FIXME: equals&hashcode 적용
            assertSoftly(softly -> {
                softly.assertThat(actual.size()).isEqualTo(1);
//                softly.assertThat(actual).containsExactly(savedMenu);
            });
        }
    }
}
