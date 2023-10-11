package kitchenpos.application;

import kitchenpos.application.test.IntegrateServiceTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.domain.fixture.MenuFixture.치킨_피자_세트_메뉴_생성;
import static kitchenpos.domain.fixture.MenuGroupFixture.*;
import static kitchenpos.domain.fixture.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MenuServiceTest extends IntegrateServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    Menu menu;
    MenuProduct 치킨_메뉴_상품;
    MenuProduct 피자_메뉴_상품;
    Product 치킨;
    Product 피자;

    @BeforeEach
    void setUp() {
        MenuGroup 인기_메뉴 = menuGroupDao.save(인기_메뉴_생성());
        Menu 치킨_피자_세트_메뉴 = 치킨_피자_세트_메뉴_생성(인기_메뉴.getId());

        Product 치킨 =  productDao.save(치킨_생성());
        Product 피자 =  productDao.save(피자_생성());

        MenuProduct 치킨_메뉴_상품 = getMenuProduct(치킨.getId(), 1L);
        MenuProduct 피자_메뉴_상품 = getMenuProduct(피자.getId(),1L);

        List<MenuProduct> menuProducts = List.of(치킨_메뉴_상품, 피자_메뉴_상품);

        치킨_피자_세트_메뉴.setMenuProducts(menuProducts);

        this.menu = 치킨_피자_세트_메뉴;
        this.치킨_메뉴_상품 = 치킨_메뉴_상품;
        this.피자_메뉴_상품 = 피자_메뉴_상품;
        this.치킨 = 치킨;
        this.피자 = 피자;
    }

    private MenuProduct getMenuProduct(final Long productId, final Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    @Nested
    class 메뉴를_생성한다 {

        @Test
        void 메뉴를_생성한다() {
            // when, then
            assertDoesNotThrow(() -> menuService.create(menu));
        }

        @Test
        void 메뉴의_가격이_null이면_예외가_발생한다() {
            // given
            menu.setPrice(null);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
        }

        @Test
        void 메뉴의_가격이_0보다_작으면_예외가_발생한다() {
            // given
            menu.setPrice(BigDecimal.valueOf(-1000));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
        }

        @Test
        void 메뉴의_MenuGroupId가_존재하지_않는다면_예외가_발생한다() {
            // given
            menu.setMenuGroupId(null);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
        }

        @Test
        void MenuProduct_중에서_MenuProduct의_ProductId를_가진_상품이_존재하지_않는다면_예외가_발생한다() {
            // given
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(99L);
            menu.setMenuProducts(List.of(menuProduct));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
        }

        @Test
        void 메뉴의_가격이_메뉴에_속한_제품들의_가격의_합보다_큰_경우_예외가_발생한다() {
            // given
            menu.setPrice(BigDecimal.valueOf(100000));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> menuService.create(menu));
        }

    }

    @Nested
    class 메뉴_목록을_조회한다 {

        @BeforeEach
        void setUp() {
            menuDao.save(menu);
        }

        @Test
        void 메뉴_목록을_조회한다() {
            // when
            List<Menu> menus = menuService.list();

            // then
            Assertions.assertAll(
                    () -> assertThat(menus).hasSize(1),
                    () -> assertThat(menus).extracting(Menu::getName)
                            .contains("치킨 피자 세트")
            );
        }

    }

}
