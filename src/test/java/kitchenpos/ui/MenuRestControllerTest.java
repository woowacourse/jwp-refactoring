package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@DisplayName("메뉴 관련 기능에서")
class MenuRestControllerTest extends ControllerTest {

    @Autowired
    private MenuRestController menuController;

    @Nested
    @DisplayName("메뉴를 생성할 때")
    class CreateMenu {

        @Test
        @DisplayName("메뉴를 정상적으로 생성한다.")
        void create() {
            Menu menu = new Menu();
            menu.setName("강정치킨");
            menu.setPrice(BigDecimal.valueOf(18000));
            menu.setMenuGroupId(createMenuGroup("추천 메뉴").getId());
            menu.setMenuProducts(List.of(createMenuProduct(createProduct("강정치킨", 18000))));

            ResponseEntity<Menu> response = menuController.create(menu);

            assertThat(response.getBody().getId()).isNotNull();
            assertThat(response.getBody().getMenuProducts()).hasSize(1);
            assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
        }

        @Nested
        @DisplayName("예외가 발생하는 경우는")
        class Exception {

            @Test
            @DisplayName("가격이 0원 이하면 예외가 발생한다.")
            void createMenuPriceLessThanZero() {
                Menu menu = new Menu();
                menu.setName("강정치킨");
                menu.setPrice(BigDecimal.valueOf(-1));
                menu.setMenuGroupId(createMenuGroup("추천 메뉴").getId());
                menu.setMenuProducts(List.of(createMenuProduct(createProduct("강정치킨", 18000))));

                assertThatThrownBy(() -> menuController.create(menu))
                        .hasMessage("가격이 0 이상이어야 합니다");
            }

            @Test
            @DisplayName("메뉴 그룹이 존재하지 않으면 예외가 발생한다.")
            void createMenuNotFoundMenuGroup() {
                Product product = createProduct("강정치킨", 18000);

                MenuProduct menuProduct = new MenuProduct();
                menuProduct.setProductId(product.getId());
                menuProduct.setQuantity(2);

                Menu menu = new Menu();
                menu.setName("강정치킨");
                menu.setPrice(BigDecimal.valueOf(18000));
                menu.setMenuGroupId(1L);
                menu.setMenuProducts(List.of(menuProduct));

                assertThatThrownBy(() -> menuController.create(menu))
                        .hasMessage("메뉴 그룹이 존재하지 않습니다.");
            }

            @Test
            @DisplayName("상품이 존재하지 않으면 예외가 발생한다.")
            void createMenuNotFoundProduct() {
                MenuGroup menuGroup = createMenuGroup("추천 메뉴");

                MenuProduct menuProduct = new MenuProduct();
                menuProduct.setProductId(1L);
                menuProduct.setQuantity(2);

                Menu menu = new Menu();
                menu.setName("강정치킨");
                menu.setPrice(BigDecimal.valueOf(18000));
                menu.setMenuGroupId(menuGroup.getId());
                menu.setMenuProducts(List.of(menuProduct));

                assertThatThrownBy(() -> menuController.create(menu))
                        .hasMessage("상품이 존재하지 않습니다.");
            }

            @Test
            @DisplayName("메뉴의 가격이 상품의 가격보다 높으면 예외가 발생한다.")
            void createMenuComparePrice() {
                Product product = createProduct("강정치킨", 18000);
                MenuGroup menuGroup = createMenuGroup("추천 메뉴");

                MenuProduct menuProduct = new MenuProduct();
                menuProduct.setProductId(product.getId());
                menuProduct.setQuantity(2);

                Menu menu = new Menu();
                menu.setName("강정치킨");
                menu.setPrice(BigDecimal.valueOf(37000));
                menu.setMenuGroupId(menuGroup.getId());
                menu.setMenuProducts(List.of(menuProduct));

                assertThatThrownBy(() -> menuController.create(menu))
                        .hasMessage("메뉴의 가격이 상품의 가격보다 높습니다.");
            }
        }
    }

    @Test
    @DisplayName("존재하는 메뉴을 모두 조회한다.")
    void list() {
        createMenu("강정치킨", 18000);
        createMenu("릭냥이네 치킨", 35000);
        createMenu("호호네 치킨", 20000);

        ResponseEntity<List<Menu>> response = menuController.list();

        assertThat(response.getBody()).hasSize(3);
    }

    private void createMenu(String name, int price) {
        MenuGroup menuGroup = createMenuGroup("추천 메뉴");
        Product product = createProduct("강정치킨", 18000);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);

        createMenu(name, price, menuGroup.getId(), List.of(menuProduct));
    }

    private MenuProduct createMenuProduct(Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);
        return menuProduct;
    }
}
