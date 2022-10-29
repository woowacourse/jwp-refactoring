package kitchenpos.application;

import kitchenpos.application.request.menu.MenuProductRequest;
import kitchenpos.application.request.menu.MenuRequest;
import kitchenpos.application.response.menu.MenuResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ServiceTest
class MenuServiceTest {

    private final MenuService menuService;
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    @Autowired
    public MenuServiceTest(final MenuService menuService, final MenuDao menuDao, final MenuGroupDao menuGroupDao,
                           final MenuProductDao menuProductDao, final ProductDao productDao) {
        this.menuService = menuService;
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Nested
    @ServiceTest
    class CreateTest {

        private static final String MENU_NAME = "메뉴";

        @DisplayName("메뉴를 추가한다")
        @Test
        void create() {
            final var menuGroup = menuGroupDao.save(new MenuGroup("중식"));
            final var product = productDao.save(new Product("자장면", 5000));
            final var menuProduct = menuProductDao.save(new MenuProduct(1, product.getId(), 1));

            final var menuProductRequest = new MenuProductRequest(menuProduct.getProductId(), menuProduct.getQuantity());
            final var request = new MenuRequest("자장면", product.getPrice(), menuGroup.getId(), menuProductRequest);
            final var actual = menuService.create(request);

            assertThat(actual.getId()).isPositive();
        }

        @DisplayName("메뉴 가격이 양수여야 한다")
        @Test
        void createWithNegativePrice() {
            final var negativePrice = -1;

            final var request = new MenuRequest(MENU_NAME, BigDecimal.valueOf(negativePrice), 1L);

            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴 가격은 양수여야 합니다.");
        }

        @DisplayName("메뉴 가격이 상품 전체 금액보다 작거나 같아야 한다")
        @Test
        void createWithBiggerPriceThenSum() {
            final var menuPrice = 11;
            final var productPrice = 10;

            final var menuGroup = menuGroupDao.save(new MenuGroup("중식"));
            final var product = productDao.save(new Product("상품", productPrice));

            final var menuProductRequest = new MenuProductRequest(product.getId(), 1);
            final var request = new MenuRequest(MENU_NAME, BigDecimal.valueOf(menuPrice), menuGroup.getId(), menuProductRequest);

            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴 가격은 상품 금액 합산보다 클 수 없습니다.");
        }

        @DisplayName("존재하는 메뉴 그룹이어야 한다")
        @Test
        void createWithNonExistMenuGroup() {
            final var nonExistMenuGroupId = 0L;

            final var menuProductRequest = new MenuProductRequest(1L, 1);
            final var request = new MenuRequest(MENU_NAME, BigDecimal.valueOf(10), nonExistMenuGroupId, menuProductRequest);

            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴 그룹을 찾을 수 없습니다.");
        }

        @DisplayName("존재하는 상품이어야 한다")
        @Test
        void createWithNonExistProduct() {
            final var nonExistProductId = 0L;

            final var menuGroup = menuGroupDao.save(new MenuGroup("중식"));

            final var menuProductRequest = new MenuProductRequest(nonExistProductId, 1);
            final var request = new MenuRequest(MENU_NAME, BigDecimal.valueOf(10), menuGroup.getId(), menuProductRequest);

            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상품을 찾을 수 없습니다.");
        }
    }

    @DisplayName("메뉴를 전체 조회한다")
    @Test
    void list() {
        saveMenu("중식 세트", 23000, 1);
        saveMenu("일식 세트", 20000, 2);

        final List<MenuResponse> actual = menuService.list();
        assertThat(actual).hasSize(2);
    }

    private Menu saveMenu(final String name, final int price, final long menuGroupId) {
        final var menu = new Menu(name, price, menuGroupId);
        return menuDao.save(menu);
    }
}
