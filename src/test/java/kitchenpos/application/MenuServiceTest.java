package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.MenuProductFixture.createMenuProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create_success() {
        // given
        MenuGroup newMenuGroup = menuGroupDao.save(createMenuGroup("추천메뉴"));
        Menu menu = createMenu("후라이드+후라이드", 19_000L, newMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(1L, 2)));

        // when
        Menu savedMenu = menuService.create(menu);

        // then
        Menu dbMenu = menuDao.findById(savedMenu.getId())
                .orElseThrow(NoSuchElementException::new);
        assertAll(
                () -> assertThat(dbMenu.getName()).isEqualTo(menu.getName()),
                () -> assertThat(dbMenu.getPrice().compareTo(menu.getPrice())).isZero(),
                () -> assertThat(dbMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId())
        );
    }

    @DisplayName("메뉴를 생성할 때 가격이 null이면 예외를 반환한다.")
    @Test
    void create_fail_if_price_is_null() {
        // given
        MenuGroup newMenuGroup = menuGroupDao.save(createMenuGroup("추천메뉴"));
        Menu menu = createMenu("후라이드+후라이드", newMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(1L, 2)));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성할 때 가격이 음수이면 예외를 반환한다.")
    @Test
    void create_fail_if_price_is_negative() {
        // given
        MenuGroup newMenuGroup = menuGroupDao.save(createMenuGroup("추천메뉴"));
        Menu menu = createMenu("후라이드+후라이드", -1L, newMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(1L, 2)));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성할 때 존재하지 않는 메뉴그룹ID라면 예외를 반환한다.")
    @Test
    void create_fail_if_menuGroupId_is_null() {
        // given
        Menu menu = createMenu("후라이드+후라이드", 19_000L, 9999999L,
                Collections.singletonList(createMenuProduct(1L, 2)));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성할 때 금액의 총합이 음수이면 예외를 반환한다.")
    @Test
    void create_fail_if_amount_is_negative() {
        // given
        Menu menu = createMenu("후라이드+후라이드", 19_000L, 9999999L,
                Collections.singletonList(createMenuProduct(1L, -1)));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 메뉴를 조회한다.")
    @Test
    void list_success() {
        // given
        MenuGroup newMenuGroup = menuGroupDao.save(createMenuGroup("추천메뉴"));
        Menu menu = menuDao.save(createMenu("후라이드+후라이드", 19_000L, newMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(1L, 2))));

        // when
        List<Menu> menus = menuService.list();

        // then
        List<String> menuNames = menus.stream()
                .map(Menu::getName)
                .collect(Collectors.toList());
        assertThat(menuNames).contains(menu.getName());
    }
}
