package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuFixture.존재하지_않는_MenuGroup_을_가진_메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.추천_메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.MenuProductFixture.존재하지_않는_상품을_가진_메뉴_상품;
import static kitchenpos.fixture.ProductFixture.후추_치킨_10000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceIntegrationTest {

    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴를_성공적으로_저장한다() {
        // given
        Product savedProduct = productDao.save(후추_치킨_10000원());
        MenuProduct menuProduct = 메뉴_상품(savedProduct, 2);
        MenuGroup savedMenuGroup = menuGroupDao.save(추천_메뉴_그룹());
        Menu menu = 메뉴_생성(BigDecimal.valueOf(19000), savedMenuGroup, menuProduct);

        // when
        Long savedMenuId = menuService.create(menu)
                .getId();

        // then
        Menu savedMenu = menuService.list()
                .stream()
                .filter(inquiryMenu -> inquiryMenu.getId().equals(savedMenuId))
                .findAny()
                .get();
        assertAll(
                () -> assertThat(savedMenu).usingRecursiveComparison()
                        .ignoringFields("id", "menuProducts.seq")
                        .ignoringFieldsOfTypes(BigDecimal.class)
                        .isEqualTo(menu),
                () -> assertThat(savedMenu.getPrice()).isEqualByComparingTo(menu.getPrice())
        );
    }

    @Test
    void price가_null_이라서_메뉴_저장에_실패한다() {
        // given
        Product savedProduct = productDao.save(후추_치킨_10000원());
        MenuProduct menuProduct = 메뉴_상품(savedProduct, 2);
        MenuGroup savedMenuGroup = menuGroupDao.save(추천_메뉴_그룹());
        Menu menu = 메뉴_생성(null, savedMenuGroup, menuProduct);

        // expect
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void price가_음수여서_메뉴_저장에_실패한다() {
        // given
        Product savedProduct = productDao.save(후추_치킨_10000원());
        MenuProduct menuProduct = 메뉴_상품(savedProduct, 2);
        MenuGroup savedMenuGroup = menuGroupDao.save(추천_메뉴_그룹());
        Menu menu = 메뉴_생성(BigDecimal.valueOf(-1), savedMenuGroup, menuProduct);

        // expect
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹이_존재하지_않아_저장에_실패한다() {
        // given
        Product savedProduct = productDao.save(후추_치킨_10000원());
        MenuProduct menuProduct = 메뉴_상품(savedProduct, 2);
        Menu menu = 존재하지_않는_MenuGroup_을_가진_메뉴_생성(BigDecimal.valueOf(19000), menuProduct);

        // expect
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void MenuProduct에_있는_상품이_존재하지_않는_메뉴이면_저장에_실패한다() {
        // given
        MenuProduct invalidMenuProduct = 존재하지_않는_상품을_가진_메뉴_상품();
        MenuGroup savedMenuGroup = menuGroupDao.save(추천_메뉴_그룹());
        Menu menu = 메뉴_생성(BigDecimal.valueOf(19000), savedMenuGroup, invalidMenuProduct);

        // expect
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void MenuProduct의_가격_합이_메뉴의_가격보다_낮으면_저장에_실패한다() {
        // given
        Product savedProduct = productDao.save(후추_치킨_10000원());
        MenuProduct menuProduct = 메뉴_상품(savedProduct, 2);
        MenuGroup savedMenuGroup = menuGroupDao.save(추천_메뉴_그룹());
        Menu menu = 메뉴_생성(BigDecimal.valueOf(10000 + 10000 + 1), savedMenuGroup, menuProduct);

        // expect
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_Menu을_조회한다() {
        // given
        List<Menu> menus = new ArrayList<>();
        List<Menu> savedMenus = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Product savedProduct = productDao.save(후추_치킨_10000원());
            MenuProduct menuProduct = 메뉴_상품(savedProduct, 2);
            MenuGroup savedMenuGroup = menuGroupDao.save(추천_메뉴_그룹());
            Menu menu = 메뉴_생성(BigDecimal.valueOf(19000), savedMenuGroup, menuProduct);
            menus.add(menu);
            savedMenus.add(menuService.create(menu));
        }

        // when
        List<Menu> resultsExcludeExistingData = menuService.list()
                .stream()
                .filter(menu ->
                        containsObjects(
                                savedMenus,
                                menuInSavedMenus -> menuInSavedMenus.getId().equals(menu.getId())
                        )
                )
                .collect(Collectors.toList());

        // then
        assertThat(resultsExcludeExistingData).usingRecursiveComparison()
                .ignoringFields("id", "price", "menuProducts.seq")
                .isEqualTo(menus);
    }

}
