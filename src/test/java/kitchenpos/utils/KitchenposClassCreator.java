package kitchenpos.utils;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class KitchenposClassCreator {

    private KitchenposClassCreator() {
    }

    public static Product createProduct(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return product;
    }

    public static MenuGroup createMenuGroup(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroup;
    }

    public static MenuProduct createMenuProduct(Product product, Long quantity) {
        //원래는 Menu의 id도 가져야 하지만, 실제로는 MenuService에서 Menu를 저장할 때 MenuProduct가 같이 저장되는 방식.
        //그래서, Menu의 id와 본인 id를 제외한 나머지 값만 넣어준다.
        Long productId = product.getId();
        requireNonNull(productId);

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);

        return menuProduct;
    }

    public static Menu createMenu(String name, MenuGroup menuGroup, BigDecimal price, List<MenuProduct> menuProducts) {
        Long menuGroupId = menuGroup.getId();
        requireNonNull(menuGroupId);

        Menu menu = new Menu();
        menu.setMenuGroupId(menuGroup.getId());
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

}
