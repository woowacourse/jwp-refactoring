package kitchenpos.ui;

import static kitchenpos.fixture.MenuGroupFactory.menuGroup;
import static kitchenpos.fixture.ProductFactory.product;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuRequest.MenuInnerMenuProductRequest;
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

    @DisplayName("메뉴 그룹 등록")
    @Test
    void create() {
        final var coke = productDao.save(product("콜라", 1000));
        final var menuGroup = menuGroupDao.save(menuGroup("콜라메뉴"));

        final var cokeInMenu = menuProductDao.save(new MenuProduct(menuGroup.getId(), coke, 1));
        final var menuProduct = new MenuProduct(cokeInMenu.getMenuId(), coke, cokeInMenu.getQuantity());

        final var menuInnerRequest = new MenuInnerMenuProductRequest(
                menuProduct.getProductId(), menuProduct.getQuantity()
        );
        final var request = new MenuRequest("콜라세트", coke.getPrice(), menuGroup.getId(), List.of(menuInnerRequest));

        final var response = menuRestController.create(request);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().getLocation()).isNotNull()
        );
    }

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
            final var menuInnerRequest = new MenuInnerMenuProductRequest(
                    fetchMenuProduct.getProductId(), fetchMenuProduct.getQuantity()
            );
            final var request = new MenuRequest("콜라세트", coke.getPrice(), menuGroup.getId(), List.of(menuInnerRequest));

            final var response = menuRestController.create(request);

            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                    () -> assertThat(response.getHeaders().getLocation()).isNotNull()
            );
        }
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        final var response = menuRestController.list();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
