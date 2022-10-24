package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = AutowireMode.ALL)
public class MenuServiceTest {

    private final MenuService menuService;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuServiceTest(MenuService menuService, MenuGroupService menuGroupService, ProductService productService) {
        this.menuService = menuService;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Nested
    @DisplayName("메뉴를 생성할 때")
    class MenuCreate {
        private MenuGroup menuGroup;
        private List<MenuProduct> menuProducts;

        @BeforeEach
        void setUp() {
            Product product = new Product();
            product.setName("삼겹살");
            product.setPrice(BigDecimal.valueOf(1000L));
            Product savedProduct = productService.create(product);

            MenuGroup menuGroup = new MenuGroup();
            menuGroup.setName("삼겹살 종류");
            MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(savedProduct.getId());
            menuProduct.setQuantity(1L);
            this.menuGroup = menuGroupService.create(menuGroup);
            this.menuProducts = new ArrayList<>();
            this.menuProducts.add(menuProduct);
        }

        @Nested
        @DisplayName("메뉴의 가격은")
        class PriceIs {

            @DisplayName("값이 존재하지 않는다면 예외가 발생한다.")
            @Test
            public void menuWithNullPrice() {
                Menu menu = new Menu();

                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("값이 음수라면 예외가 발생한다.")
            @Test
            public void menuWithNegativePrice() {
                Menu menu = new Menu();
                menu.setPrice(BigDecimal.valueOf(-1L));

                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("메뉴가 포함되는 그룹은")
        class MenuGroupIs {

            @DisplayName("존재하지 않다면 예외가 발생한다.")
            @Test
            public void menuGroupNotSaved() {
                Menu menu = new Menu();
                menu.setName("삼겹살 정식");
                menu.setPrice(BigDecimal.valueOf(1000L));
                menu.setMenuGroupId(-1L);

                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }

        @Nested
        @DisplayName("메뉴 내에 포함되는 상품은")
        class MenuProductIs {

            @DisplayName("존재하지 않는 상품의 경우 예외가 발생한다.")
            @Test
            public void menuProductNotContained() {
                Menu menu = new Menu();
                menu.setName("삼겹살 정식");
                menu.setPrice(BigDecimal.valueOf(1000L));
                menu.setMenuGroupId(menuGroup.getId());

                List<MenuProduct> copiedProducts = new ArrayList<>(menuProducts);
                MenuProduct fakeProduct = new MenuProduct();
                fakeProduct.setProductId(-1L);
                fakeProduct.setQuantity(10);
                copiedProducts.add(fakeProduct);
                menu.setMenuProducts(copiedProducts);

                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
            }

            @DisplayName("가격의 합보다 메뉴의 가격이 비싸다면 예외가 발생한다.")
            @Test
            public void menuProductPriceDoesNotExceedTotalSum() {
                Menu menu = new Menu();
                menu.setName("삼겹살 정식");
                menu.setPrice(BigDecimal.valueOf(1500L));
                menu.setMenuGroupId(menuGroup.getId());
                menu.setMenuProducts(menuProducts);

                assertThatThrownBy(() -> menuService.create(menu))
                        .isInstanceOf(IllegalArgumentException.class);
            }
        }
    }
}
