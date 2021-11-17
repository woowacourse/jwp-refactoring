package kitchenpos;

import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menuproduct.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.dto.MenuProductRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuFixture {
    public static final String MENU_NAME1 = "육회초밥단품";
    public static final String MENU_NAME2 = "육회초밥+연어초밥";
    public static final String MENU_GROUP_NAME1 = "나혼자세트";
    public static final String MENU_GROUP_NAME2 = "둘이서세트";
    public static final BigDecimal MENU_PRICE = BigDecimal.valueOf(15900);
    public static final Long MENU_GROUP_ID = 1L;
    public static final Long PRODUCT_ID = 1L;
    public static final long MENU_QUANTITY = 1;

    public static MenuGroup createMenuGroup1() {
        return new MenuGroup(MENU_GROUP_NAME1);
    }

    public static MenuGroup createMenuGroup2() {
        return new MenuGroup(MENU_GROUP_NAME2);
    }

    public static MenuGroup createMenuGroup1(Long id) {
        return new MenuGroup(id, MENU_GROUP_NAME1);
    }

    public static Menu createMenu1(MenuGroup menuGroup, List<Product> products) {
        Menu menu = new Menu(MENU_NAME1, MENU_PRICE, menuGroup);
        List<MenuProduct> menuProducts = products.stream()
                .map(it -> new MenuProduct(it, MENU_QUANTITY))
                .collect(Collectors.toList());
        menu.changeMenuProducts(menuProducts);
        return menu;
    }

    public static Menu createMenu2(MenuGroup menuGroup, List<Product> products) {
        Menu menu = new Menu(MENU_NAME2, MENU_PRICE, menuGroup);
        List<MenuProduct> menuProducts = products.stream()
                .map(it -> new MenuProduct(it, MENU_QUANTITY))
                .collect(Collectors.toList());
        menu.changeMenuProducts(menuProducts);
        return menu;
    }

    public static Menu createMenu1(Long id) {
        return createMenu1(id, MENU_NAME1, MENU_PRICE);
    }

    public static Menu createMenu1(Long id, String name, BigDecimal price) {
        return new Menu(name, price);
    }

    public static MenuProduct createMenuProduct(Product product) {
        MenuProduct menuProduct = new MenuProduct();
//        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(MENU_QUANTITY);
        return menuProduct;
    }

    public static MenuProductRequest createMenuProductRequest() {
        return new MenuProductRequest(PRODUCT_ID, MENU_QUANTITY);
    }

    public static MenuProduct createMenuProduct(Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
//        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
