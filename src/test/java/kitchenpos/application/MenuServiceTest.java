package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.추천메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.ProductFixture.후추_치킨_10000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuProductFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceIntegrationTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Test
    void 메뉴를_성공적으로_저장한다() {
        // given
        Product savedProduct = productDao.save(후추_치킨_10000원());
        MenuProduct menuProduct = 메뉴_상품(savedProduct, 2);
        MenuGroup savedMenuGroup = menuGroupDao.save(추천메뉴_그룹());
        Menu menu = MenuFixture.메뉴_생성(BigDecimal.valueOf(19000), savedMenuGroup, menuProduct);

        // when
        Long savedMenuId = menuService.create(menu)
                .getId();

        // then
        Menu savedMenu = menuDao.findById(savedMenuId)
                .orElseThrow(NoSuchElementException::new);
        assertThat(savedMenu).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(savedMenu);
    }

    @Test
    void price가_null_이라서_메뉴_저장에_실패한다() {
        // given
        Product savedProduct = productDao.save(후추_치킨_10000원());
        MenuProduct menuProduct = 메뉴_상품(savedProduct, 2);
        MenuGroup savedMenuGroup = menuGroupDao.save(추천메뉴_그룹());
        Menu menu = MenuFixture.메뉴_생성(null, savedMenuGroup, menuProduct);

        // when then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void price가_음수여서_메뉴_저장에_실패한다() {
        // given
        Product savedProduct = productDao.save(후추_치킨_10000원());
        MenuProduct menuProduct = 메뉴_상품(savedProduct, 2);
        MenuGroup savedMenuGroup = menuGroupDao.save(추천메뉴_그룹());
        Menu menu = MenuFixture.메뉴_생성(BigDecimal.valueOf(-1), savedMenuGroup, menuProduct);

        // when then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹이_존재하지_않아_저장에_실패한다() {
        // given
        Product savedProduct = productDao.save(후추_치킨_10000원());
        MenuProduct menuProduct = 메뉴_상품(savedProduct, 2);
        Menu menu = MenuFixture.존재하지_않는_MenuGroup_을_가진_메뉴_생성(BigDecimal.valueOf(19000), menuProduct);

        // when then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void MenuProduct에_있는_상품이_존재하지_않는_메뉴이다() {
        // given
        MenuProduct invalidMenuProduct = MenuProductFixture.존재하지_않는_상품을_가진_메뉴_상품();
        MenuGroup savedMenuGroup = menuGroupDao.save(추천메뉴_그룹());
        Menu menu = MenuFixture.메뉴_생성(BigDecimal.valueOf(19000), savedMenuGroup, invalidMenuProduct);

        // when then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
