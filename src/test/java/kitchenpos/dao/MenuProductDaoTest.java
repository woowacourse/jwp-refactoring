package kitchenpos.dao;

import static kitchenpos.support.MenuFixture.MENU_1;
import static kitchenpos.support.MenuGroupFixture.MENU_GROUP_1;
import static kitchenpos.support.MenuProductFixture.MENU_PRODUCT_1;
import static kitchenpos.support.ProductFixture.PRODUCT_1;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;

class MenuProductDaoTest extends JdbcDaoTest {

    @Test
    void 메뉴상품을_저장한다() {
        // given
        final Product savedProduct = 상품을_저장한다(PRODUCT_1.생성());
        final MenuGroup savedMenuGroup = 메뉴그룹을_저장한다(MENU_GROUP_1.생성());
        final Menu savedMenu = 메뉴를_저장한다(MENU_1.생성(savedMenuGroup.getId()));
        final MenuProduct menuProduct = new MenuProduct(savedMenu.getId(), savedProduct.getId(), 10);

        // when
        final MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);

        // then
        assertThat(savedMenuProduct.getSeq()).isNotNull();
    }

    @Test
    void 메뉴상품을_아이디로_조회한다() {
        // given
        final Product savedProduct = 상품을_저장한다(PRODUCT_1.생성());
        final MenuGroup savedMenuGroup = 메뉴그룹을_저장한다(MENU_GROUP_1.생성());
        final Menu savedMenu = 메뉴를_저장한다(MENU_1.생성(savedMenuGroup.getId()));
        final MenuProduct savedMenuProduct = 메뉴상품을_저장한다(MENU_PRODUCT_1.생성(savedMenu.getId(), savedProduct.getId()));

        // when
        final MenuProduct foundMenuProduct = menuProductDao.findById(savedMenuProduct.getSeq())
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(foundMenuProduct.getQuantity()).isEqualTo(1);
    }

    @Test
    void 모든_메뉴상품을_조회한다() {
        // given
        final int alreadyExistCount = menuProductDao.findAll()
                .size();
        final Product savedProduct = 상품을_저장한다(PRODUCT_1.생성());
        final MenuGroup savedMenuGroup = 메뉴그룹을_저장한다(MENU_GROUP_1.생성());
        final Menu savedMenu = 메뉴를_저장한다(MENU_1.생성(savedMenuGroup.getId()));
        final MenuProduct savedMenuProduct = 메뉴상품을_저장한다(MENU_PRODUCT_1.생성(savedMenu.getId(), savedProduct.getId()));

        // when
        final List<MenuProduct> menuProducts = menuProductDao.findAll();

        // then
        assertThat(menuProducts).usingFieldByFieldElementComparator()
                .hasSize(alreadyExistCount + 1)
                .contains(savedMenuProduct);
    }

    @Test
    void 메뉴_아이디로_메뉴상품_목록을_조회한다() {
        // given
        final Product savedProduct = 상품을_저장한다(PRODUCT_1.생성());
        final MenuGroup savedMenuGroup = 메뉴그룹을_저장한다(MENU_GROUP_1.생성());
        final Menu savedMenu = 메뉴를_저장한다(MENU_1.생성(savedMenuGroup.getId()));
        final MenuProduct savedMenuProduct = 메뉴상품을_저장한다(MENU_PRODUCT_1.생성(savedMenu.getId(), savedProduct.getId()));

        // when
        final List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(savedMenu.getId());

        // then
        assertThat(menuProducts).usingFieldByFieldElementComparator()
                .hasSize(1)
                .containsOnly(savedMenuProduct);
    }
}
