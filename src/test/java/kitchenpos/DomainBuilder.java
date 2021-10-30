package kitchenpos;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.List;

public class DomainBuilder {

    public static Product createProductWithId(Long id, String name, BigDecimal price) {
        Product product = createProduct(
                name,
                price
        );
        product.setId(id);
        return product;
    }

    public static Product createProduct(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    public static Menu createMenuWithId(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = createMenu(
                name,
                price,
                menuGroupId,
                menuProducts
        );
        menu.setId(id);
        return menu;
    }

    public static Menu createMenuWithId(Long id, Menu menu) {
        Menu menuWithId = createMenu(
                menu.getName(),
                menu.getPrice(),
                menu.getMenuGroupId(),
                menu.getMenuProducts()
        );
        menu.setId(id);
        return menuWithId;
    }

    public static Menu createMenu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(
                menuProducts
        );
        return menu;
    }

    public static MenuProduct createMenuProduct(Long productId, Long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
