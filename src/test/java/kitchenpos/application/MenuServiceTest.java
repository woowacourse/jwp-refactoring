package kitchenpos.application;

import static kitchenpos.support.fixture.domain.MenuFixture.getMenu;
import static kitchenpos.support.fixture.domain.MenuGroupFixture.getMenuGroup;
import static kitchenpos.support.fixture.domain.MenuProductFixture.getMenuProduct;
import static kitchenpos.support.fixture.domain.ProductFixture.getProduct;
import static kitchenpos.support.fixture.dto.MenuCreateRequestFixture.menuCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.support.ServiceTest;
import kitchenpos.ui.dto.menu.MenuCreateRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
public class MenuServiceTest {

    private static final List<MenuProduct> EMPTY_MENU_PRODUCTS = Collections.emptyList();

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    @Nested
    class 메뉴를_등록시 {

        @Test
        void 가격이_0보다_작으면_예외를_던진다() {
            //given
            final MenuCreateRequest request = menuCreateRequest("menu", -1L, 1L, EMPTY_MENU_PRODUCTS);

            //when
            //then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹ID에_해당하는_메뉴그룹이_존재하지_않으면_예외를_던진다() {
            //given
            final MenuCreateRequest request = menuCreateRequest("menu", -1L, 1L, EMPTY_MENU_PRODUCTS);

            //when
            //then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 없는_메뉴_상품이_메뉴에_있으면_예외를_던진다() {
            //given
            final MenuGroup menuGroup = menuGroupDao.save(getMenuGroup("menuGroup"));
            final MenuCreateRequest request = menuCreateRequest("menu", -1L, menuGroup.getId(), EMPTY_MENU_PRODUCTS);

            //when
            //then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴의_가격이_메뉴_상품들의_총금액보다_크면_예외를_던진다() {
            //given
            final MenuGroup menuGroup = menuGroupDao.save(getMenuGroup("menuGroup"));
            final Product product = productDao.save(getProduct("product", 100L));
            final MenuProduct menuProduct = getMenuProduct(null, product.getId(), 2L);
            final MenuCreateRequest request = menuCreateRequest("menu", -1L, menuGroup.getId(), List.of(menuProduct));

            //when
            //then
            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 성공한다() {
            //given
            final MenuGroup menuGroup = menuGroupDao.save(getMenuGroup("menuGroup"));
            final Product product = productDao.save(getProduct("prodcut", 100L));
            final MenuProduct menuProduct = getMenuProduct(null, product.getId(), 2L);
            final MenuCreateRequest request = menuCreateRequest("menu", 190L, menuGroup.getId(), List.of(menuProduct));

            //when
            final Menu savedMenu = menuService.create(request);

            //then
            assertAll(
                    () -> assertThat(menuDao.findById(savedMenu.getId())).isPresent(),
                    () -> assertThat(menuProductDao.findAllByMenuId(savedMenu.getId()))
                            .usingRecursiveComparison()
                            .isEqualTo(savedMenu.getMenuProducts())
            );
        }
    }

    @Test
    void 모든_메뉴를_조회한다() {
        // given
        final MenuGroup menuGroup = menuGroupDao.save(getMenuGroup("menuGroup"));

        final Menu menu1 = menuDao.save(getMenu("menu1", 90L, menuGroup.getId()));
        final Menu menu2 = menuDao.save(getMenu("menu2", 80L, menuGroup.getId()));

        final Product product = productDao.save(getProduct("product", 100L));

        final MenuProduct menuProduct1 = menuProductDao.save(getMenuProduct(menu1.getId(), product.getId(), 1L));
        final MenuProduct menuProduct2 = menuProductDao.save(getMenuProduct(menu2.getId(), product.getId(), 1L));

        menu1.addMenuProducts(List.of(menuProduct1));
        menu2.addMenuProducts(List.of(menuProduct2));

        // when
        final List<Menu> allMenu = menuService.list();

        // then
        assertThat(allMenu)
                .usingRecursiveComparison()
                .isEqualTo(List.of(menu1, menu2));
    }
}
