package kitchenpos.integration.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.integration.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MenuServiceIntegrationTest extends IntegrationTest {

    @DisplayName("메뉴를 등록한다.")
    @Nested
    class Create {

        @DisplayName("메뉴를 등록할 수 있다.")
        @Test
        void create_Valid_Success() {
            // given
            Product product = new Product();
            product.setName("얌 프라이");
            product.setPrice(BigDecimal.valueOf(8000, 2));

            Product savedProduct = productService.create(product);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setMenuId(7L);
            menuProduct.setProductId(savedProduct.getId());
            menuProduct.setQuantity(3);

            Menu menu = new Menu();
            menu.setName("얌 프라이");
            menu.setPrice(BigDecimal.valueOf(8000, 2));
            menu.setMenuGroupId(4L);
            menu.setMenuProducts(Collections.singletonList(menuProduct));

            // when
            Menu savedMenu = menuService.create(menu);

            // then
            assertThat(savedMenu)
                .usingRecursiveComparison()
                .ignoringFields("id", "menuProducts.seq")
                .isEqualTo(menu);
        }

        @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다. - null인 경우")
        @Test
        void create_InvalidPriceWithNull_Fail() {
            // given
            Product product = new Product();
            product.setName("얌 프라이");
            product.setPrice(BigDecimal.valueOf(8000, 2));

            Product savedProduct = productService.create(product);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setMenuId(7L);
            menuProduct.setProductId(savedProduct.getId());
            menuProduct.setQuantity(3);

            Menu menu = new Menu();
            menu.setName("얌 프라이");
            menu.setMenuGroupId(4L);
            menu.setMenuProducts(Collections.singletonList(menuProduct));

            // when
            // then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다. - 0 이하인 경우")
        @Test
        void create_InvalidPriceWithNegative_Fail() {
            // given
            Product product = new Product();
            product.setName("얌 프라이");
            product.setPrice(BigDecimal.valueOf(8000, 2));

            Product savedProduct = productService.create(product);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setMenuId(7L);
            menuProduct.setProductId(savedProduct.getId());
            menuProduct.setQuantity(3);

            Menu menu = new Menu();
            menu.setName("얌 프라이");
            menu.setPrice(BigDecimal.valueOf(-1000));
            menu.setMenuGroupId(4L);
            menu.setMenuProducts(Collections.singletonList(menuProduct));

            // when
            // then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 메뉴 그룹이 존재하지 않으면 등록할 수 없다.")
        @Test
        void create_NonExistingMenuGroup_Fail() {
            // given
            Product product = new Product();
            product.setName("얌 프라이");
            product.setPrice(BigDecimal.valueOf(8000, 2));

            Product savedProduct = productService.create(product);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setMenuId(7L);
            menuProduct.setProductId(savedProduct.getId());
            menuProduct.setQuantity(3);

            Menu menu = new Menu();
            menu.setName("얌 프라이");
            menu.setPrice(BigDecimal.valueOf(-1000));
            menu.setMenuGroupId(100L);
            menu.setMenuProducts(Collections.singletonList(menuProduct));

            // when
            // then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 메뉴 상품이 존재하지 않으면 등록할 수 없다.")
        @Test
        void create_NonExistingMenuProduct_Fail() {
            // given
            Menu menu = new Menu();
            menu.setName("얌 프라이");
            menu.setPrice(BigDecimal.valueOf(-1000));
            menu.setMenuGroupId(4L);
            menu.setMenuProducts(Collections.emptyList());

            // when
            // then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 수량이 올바르지 않으면 등록할 수 없다. - 0개인 경우")
        @Test
        void create_InvalidQuantityWithZero_Fail() {
            // given
            Product product = new Product();
            product.setName("얌 프라이");
            product.setPrice(BigDecimal.valueOf(8000, 2));

            Product savedProduct = productService.create(product);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setMenuId(7L);
            menuProduct.setProductId(savedProduct.getId());
            menuProduct.setQuantity(0);

            Menu menu = new Menu();
            menu.setName("얌 프라이");
            menu.setPrice(BigDecimal.valueOf(8000, 2));
            menu.setMenuGroupId(4L);
            menu.setMenuProducts(Collections.singletonList(menuProduct));

            // when
            // then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴의 수량이 올바르지 않으면 등록할 수 없다. - 0개 이하인 경우")
        @Test
        void create_InvalidQuantityWithNegative_Fail() {
            // given
            Product product = new Product();
            product.setName("얌 프라이");
            product.setPrice(BigDecimal.valueOf(8000, 2));

            Product savedProduct = productService.create(product);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setMenuId(7L);
            menuProduct.setProductId(savedProduct.getId());
            menuProduct.setQuantity(-2);

            Menu menu = new Menu();
            menu.setName("얌 프라이");
            menu.setPrice(BigDecimal.valueOf(8000, 2));
            menu.setMenuGroupId(4L);
            menu.setMenuProducts(Collections.singletonList(menuProduct));

            // when
            // then
            assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("메뉴의 목록을 조회한다.")
    @Nested
    class Read {

        @DisplayName("메뉴의 목록을 조회할 수 있다.")
        @Test
        void list_Valid_Success() {
            // given
            Product product = new Product();
            product.setName("얌 프라이");
            product.setPrice(BigDecimal.valueOf(8000, 2));

            Product savedProduct = productService.create(product);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setMenuId(7L);
            menuProduct.setProductId(savedProduct.getId());
            menuProduct.setQuantity(3);

            Menu menu = new Menu();
            menu.setName("얌 프라이");
            menu.setPrice(BigDecimal.valueOf(8000, 2));
            menu.setMenuGroupId(4L);
            menu.setMenuProducts(Collections.singletonList(menuProduct));

            Menu savedMenu = menuService.create(menu);

            // when
            List<Menu> menus = menuService.list();

            // then
            assertThat(menus).hasSize(7);
            assertThat(menus.get(6))
                .usingRecursiveComparison()
                .isEqualTo(savedMenu);
        }
    }
}
