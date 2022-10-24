package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;
    @Autowired
    private ProductDao productDao;

    @Nested
    @DisplayName("create()")
    class create {

        @Test
        @DisplayName("예외사항이 존재하지 않는 경우 새로운 메뉴를 생성한다.")
        void create() {
            // given
            MenuProduct menuProduct = createMenuProduct(createAndSaveProduct().getId());
            MenuGroup menuGroup = createMenuGroup();
            Menu menu = createMenu(
                "menu",
                new BigDecimal(1000),
                menuGroup.getId(),
                menuProduct
            );

            // when
            Menu savedMenu = menuService.create(menu);

            // then
            assertAll(
                () -> assertThat(savedMenu).isNotNull(),
                () -> assertThat(savedMenu.getId()).isNotNull()
            );
        }

        @Test
        @DisplayName("가격이 빈값일 경우 예외가 발생한다.")
        void nullPrice() {
            // given
            MenuProduct menuProduct = createMenuProduct(createAndSaveProduct().getId());
            MenuGroup menuGroup = createMenuGroup();
            Menu menu = createMenu(
                "menu",
                null,
                menuGroup.getId(),
                menuProduct
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("가격이 0 미만일 경우 예외가 발생한다.")
        void negativePrice() {
            // given
            MenuProduct menuProduct = createMenuProduct(createAndSaveProduct().getId());
            MenuGroup menuGroup = createMenuGroup();
            Menu menu = createMenu(
                "menu",
                new BigDecimal(-1),
                menuGroup.getId(),
                menuProduct
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 menu group id인 경우 예외가 발생한다.")
        void invalidMenuGroupId() {
            // given
            MenuProduct menuProduct = createMenuProduct(createAndSaveProduct().getId());
            Menu menu = createMenu(
                "menu",
                new BigDecimal(1000),
                0L,
                menuProduct
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 menu product id인 경우 예외가 발생한다.")
        void invalidMenuProductId() {
            // given
            MenuProduct menuProduct = createMenuProduct(0L);
            MenuGroup menuGroup = createMenuGroup();
            Menu menu = createMenu(
                "menu",
                new BigDecimal(1000),
                menuGroup.getId(),
                menuProduct
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("각 상품의 합보다 가격이 큰 값일 경우 예외가 발생한다.")
        void biggerThanProductPriceSum() {
            // given
            MenuProduct menuProduct = createMenuProduct(0L);
            MenuGroup menuGroup = createMenuGroup();
            Menu menu = createMenu(
                "menu",
                new BigDecimal(2000),
                menuGroup.getId(),
                menuProduct
            );

            // when, then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        Menu createMenu(String name, BigDecimal price, long menuGroupId, MenuProduct menuProduct) {
            Menu menu = new Menu();
            menu.setName(name);
            menu.setPrice(price);
            menu.setMenuGroupId(menuGroupId);
            menu.setMenuProducts(new ArrayList<MenuProduct>() {{
                add(menuProduct);
            }});

            return menu;
        }

        Product createAndSaveProduct() {
            Product product = new Product();
            product.setName("product");
            product.setPrice(new BigDecimal(1000));

            return productDao.save(product);
        }

        MenuProduct createMenuProduct(long productId) {
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(10L);

            return menuProduct;
        }

        MenuGroup createMenuGroup() {
            MenuGroup menuGroup = new MenuGroup();
            menuGroup.setName("menuGroup");
            menuGroup = menuGroupDao.save(menuGroup);

            return menuGroupDao.save(menuGroup);
        }

    }

    @Test
    void list() {
        List<Menu> menus = menuService.list();
        assertThat(menus).isNotNull();
    }
}
