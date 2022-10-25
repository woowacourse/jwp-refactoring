package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class MenuRestControllerTest extends ControllerTest {

    @Autowired
    private MenuRestController menuController;

    @Test
    void create() {
        MenuGroup menuGroup = createMenuGroup("추천 메뉴");
        Product product = createProduct("강정치킨", 18000);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);

        Menu menu = new Menu();
        menu.setName("강정치킨");
        menu.setPrice(BigDecimal.valueOf(18000));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct));

        ResponseEntity<Menu> response = menuController.create(menu);

        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getMenuProducts()).hasSize(1);
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void createMenuPriceLessThanZero() {
        MenuGroup menuGroup = createMenuGroup("추천 메뉴");
        Product product = createProduct("강정치킨", 18000);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);

        Menu menu = new Menu();
        menu.setName("강정치킨");
        menu.setPrice(BigDecimal.valueOf(-1));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(List.of(menuProduct));

        assertThatThrownBy(() -> menuController.create(menu))
                .hasMessage("가격이 0 이상이어야 합니다");
    }

    @Test
    void createMenuNtoFoundMenuGroup() {
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
    void createMenuNtoFoundProduct() {
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

    @Test
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
}
