package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
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
    private MenuGroupService menuGroupService;

    @Autowired
    private ProductService productService;

    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupService.create(new MenuGroup(null, "마이쮸 1종 세트"));
        Product product = productService.create(new Product(null, "마이쮸", BigDecimal.valueOf(800)));
        menuProducts = List.of(new MenuProduct(null, null, product.getId(), 1));
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        final String name = "마이쮸 포도맛";
        final BigDecimal price = BigDecimal.valueOf(800);

        final Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(menuProducts);

        final Menu savedMenu = menuService.create(menu);

        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getName()).isEqualTo(name),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menuGroup.getId()),
                () -> assertThat(savedMenu.getMenuProducts()).hasSize(1)
        );
    }

    @DisplayName("메뉴를 등록한다. - 가격이 null이면 예외를 반환한다.")
    @Test
    void create_exception_priceIsNull() {
        final Menu menu = new Menu();
        menu.setName("마이쮸 포도맛");
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록한다. - 가격이 0보다 작으면 예외를 반환한다.")
    @Test
    void create_exception_priceIsLessThanZero() {
        final Menu menu = new Menu();
        menu.setName("마이쮸 포도맛");
        menu.setPrice(BigDecimal.valueOf(-1));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록한다. - 메뉴 그룹이 존재하지 않으면 예외를 반환한다.")
    @Test
    void create_exception_noSuchMenuGroup() {
        final Menu menu = new Menu();
        menu.setName("마이쮸 포도맛");
        menu.setPrice(BigDecimal.valueOf(800));
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록한다. - 존재하지 않는 상품이 포함되어 있으면 예외를 반환한다.")
    @Test
    void create_exception_noSuchProduct() {
        final Menu menu = new Menu();
        menu.setName("마이쮸 포도맛");
        menu.setPrice(BigDecimal.valueOf(800));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(List.of(new MenuProduct(null, null, null, 1)));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 등록한다. - 메뉴 금액이 각 상품 금액의 합보다 크면 예외를 반환한다.")
    @Test
    void create_exception_wrongTotalPrice() {
        final Menu menu = new Menu();
        menu.setName("마이쮸 포도맛");
        menu.setPrice(BigDecimal.valueOf(900));
        menu.setMenuGroupId(menuGroup.getId());
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
