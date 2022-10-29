package kitchenpos.ui;

import static kitchenpos.fixture.MenuGroupFactory.menuGroup;
import static kitchenpos.fixture.ProductFactory.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class MenuRestControllerTest {

    @Autowired
    private MenuRestController menuRestController;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @DisplayName("create 메서드는")
    @Nested
    class create {

        private Product coke = productDao.save(product("콜라", 1000));
        private MenuGroup menuGroup = menuGroupDao.save(menuGroup("콜라메뉴"));
        private MenuProduct cokeInMenu = menuProductDao.save(new MenuProduct(menuGroup.getId(), coke, 1));
        private MenuProduct fetchMenuProduct = new MenuProduct(cokeInMenu.getMenuId(), coke, cokeInMenu.getQuantity());

        @DisplayName("메뉴를 등록하고 CREATED를 반환한다")
        @Test
        void addMenuGroup() {
            final var menu = new Menu("콜라세트", coke.getPrice(), menuGroup.getId(), List.of(fetchMenuProduct));
            final var response = menuRestController.create(menu);

            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                    () -> assertThat(response.getHeaders().getLocation()).isNotNull()
            );
        }
    }

    @DisplayName("메뉴 그룹 등록")
    @Test
    void create() {
        final var coke = productDao.save(product("콜라", 1000));
        final var menuGroup = menuGroupDao.save(menuGroup("콜라메뉴"));

        final var menu = MenuFactory.menu("콜라세트", menuGroup, List.of(coke));
        final var response = menuRestController.create(menu);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().getLocation()).isNotNull()
        );
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        final var response = menuRestController.list();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
