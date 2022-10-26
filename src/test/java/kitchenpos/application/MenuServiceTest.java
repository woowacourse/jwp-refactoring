package kitchenpos.application;

import static kitchenpos.application.DomainFixture.getMenuGroup;
import static kitchenpos.application.DomainFixture.getProduct;
import static kitchenpos.application.DomainFixture.getMenu;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    private MenuGroup createMenuGroup() {
        return menuGroupDao.save(getMenuGroup());
    }

    private List<MenuProduct> createMenuProducts() {
        final Product product = productDao.save(getProduct());
        return List.of(new MenuProduct(null, null, product.getId(), 1));
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        final MenuGroup menuGroup = createMenuGroup();
        final List<MenuProduct> menuProducts = createMenuProducts();
        final Menu menu = getMenu(menuGroup.getId());
        menu.setMenuProducts(menuProducts);

        final Menu savedMenu = menuService.create(menu);

        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getName()).isEqualTo(menu.getName()),
                () -> assertThat(savedMenu.getPrice()).isEqualByComparingTo(menu.getPrice()),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroup.getId()),
                () -> assertThat(savedMenu.getMenuProducts()).hasSize(1)
        );
    }

    @DisplayName("메뉴를 등록한다. - 메뉴 그룹이 존재하지 않으면 예외를 반환한다.")
    @Test
    void create_exception_noSuchMenuGroup() {
        final List<MenuProduct> menuProducts = createMenuProducts();
        final Menu menu = getMenu(null);
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록한다. - 존재하지 않는 상품이 포함되어 있으면 예외를 반환한다.")
    @Test
    void create_exception_noSuchProduct() {
        final MenuGroup menuGroup = createMenuGroup();
        final Menu menu = getMenu(menuGroup.getId());
        menu.setMenuProducts(List.of(new MenuProduct(null, null, null, 1)));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록한다. - 메뉴 금액이 각 상품 금액의 합보다 크면 예외를 반환한다.")
    @Test
    void create_exception_wrongTotalPrice() {
        final MenuGroup menuGroup = createMenuGroup();
        final List<MenuProduct> menuProducts = createMenuProducts();
        final Menu menu = new Menu( "마이쮸 포도맛", BigDecimal.valueOf(900), menuGroup.getId());
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        final List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(6);
    }
}
