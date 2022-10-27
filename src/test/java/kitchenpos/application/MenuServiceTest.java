package kitchenpos.application;

import static kitchenpos.fixture.MenuBuilder.aMenu;
import static kitchenpos.fixture.MenuGroupFactory.createMenuGroup;
import static kitchenpos.fixture.ProductBuilder.aProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    MenuDao menuDao;

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    MenuProductDao menuProductDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    MenuService sut;

    @Test
    @DisplayName("Menu의 가격은 null일 수 없다")
    void throwException_WhenPriceNull() {
        // given
        Menu menu = aMenu(savedMenuGroup().getId())
                .withPrice(null)
                .build();

        // when && then
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격이 유효하지 않습니다");
    }

    @Test
    @DisplayName("Menu의 가격은 음수일 수 없다")
    void throwException_WhenPriceNegative() {
        // given
        Menu menu = aMenu(savedMenuGroup().getId())
                .withPrice(BigDecimal.valueOf(-1L))
                .build();

        // when && then
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격이 유효하지 않습니다");
    }

    @Test
    @DisplayName("Menu의 MenuGroupId가 존재하지 않으면 Menu를 생성할 수 없다")
    void throwException_WhenGivenNonExistMenuGroupId() {
        // given
        final long NON_EXIST_ID = 0L;
        Menu menu = aMenu(NON_EXIST_ID)
                .build();

        // when && when
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴 그룹이 존재하지 않습니다");
    }

    @Test
    @DisplayName("Menu에 포함된 Product가 존재하지 않으면 Menu를 생성할 수 없다")
    void throwException_WhenGivenNonExistMenuProductId() {
        // given
        final long NON_EXIST_ID = 0L;
        Menu menu = aMenu(savedMenuGroup().getId())
                .withMenuProducts(List.of(new MenuProduct(NON_EXIST_ID, 1)))
                .build();

        // when && when
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품이 존재하지 않습니다");
    }

    @Test
    @DisplayName("Product의 가격의 합보다 Menu의 가격이 더 커서는 안된다")
    void throwException_WhenMenuPriceAndSumOfMenuProductPrice_NotMatch() {
        // given
        final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(1000L);
        final long QUANTITY = 2;
        final BigDecimal WRONG_MENU_PRICE = PRODUCT_PRICE.multiply(BigDecimal.valueOf(QUANTITY))
                .add(BigDecimal.valueOf(1000L));

        Long productId = productDao.save(
                aProduct()
                        .withPrice(PRODUCT_PRICE)
                        .build()
        ).getId();

        Menu menu = aMenu(savedMenuGroup().getId())
                .withMenuProducts(List.of(new MenuProduct(productId, QUANTITY)))
                .withPrice(WRONG_MENU_PRICE)
                .build();

        // when && then
        assertThatThrownBy(() -> sut.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴의 가격은 상품 전체 가격의 합보다 클 수 없습니다");
    }

    @Test
    @DisplayName("메뉴를 생성한다")
    void saveMenu() {
        // given
        final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(14_000L);
        final long QUANTITY = 1L;
        final BigDecimal MENU_PRICE = PRODUCT_PRICE.multiply(BigDecimal.valueOf(QUANTITY));

        Long productId = productDao.save(
                aProduct()
                        .withPrice(PRODUCT_PRICE)
                        .build()
        ).getId();

        Menu menu = aMenu(savedMenuGroup().getId())
                .withPrice(MENU_PRICE)
                .withMenuProducts(List.of(new MenuProduct(productId, QUANTITY)))
                .build();

        // when
        Menu savedMenu = sut.create(menu);

        // then
        assertThat(savedMenu).isNotNull();
        assertThatMenuIdIsSet(savedMenu.getMenuProducts(), savedMenu.getId());
    }

    @Test
    @DisplayName("Menu 목록을 조회한다")
    void listMenus() {
        List<Menu> expected = menuDao.findAll();

        List<Menu> menus = sut.list();

        assertThat(menus).isEqualTo(expected);
        for (Menu menu : menus) {
            assertThatMenuIdIsSet(menu.getMenuProducts(), menu.getId());
        }
    }

    private void assertThatMenuIdIsSet(List<MenuProduct> menuProducts, Long menuId) {
        for (MenuProduct menuProduct : menuProducts) {
            assertThat(menuProduct.getMenuId()).isEqualTo(menuId);
        }
    }

    private MenuGroup savedMenuGroup() {
        return menuGroupDao.save(createMenuGroup());
    }
}
