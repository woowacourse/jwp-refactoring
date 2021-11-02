package kitchenpos.factory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class KitchenPosFactory {

    private KitchenPosFactory() {}

    public static MenuGroup getStandardMenuGroup() {
        MenuGroup standardMenuGroup = new MenuGroup();
        standardMenuGroup.setName("메뉴그룹이름");
        standardMenuGroup.setId(1L);
        return standardMenuGroup;
    }

    public static List<MenuGroup> getStandardMenuGroups() {
        List<MenuGroup> standardMenuGroups = new ArrayList<>();
        standardMenuGroups.add(getStandardMenuGroup());
        return standardMenuGroups;
    }

    public static Product getStandardProduct() {
        Product standardProduct = new Product();
        standardProduct.setId(1L);
        standardProduct.setName("상품 이름");
        standardProduct.setPrice(new BigDecimal(1000));
        return standardProduct;
    }

    public static List<Product> getStandardProducts() {
        List<Product> standardProducts = new ArrayList<>();
        standardProducts.add(getStandardProduct());
        return standardProducts;
    }

    public static MenuProduct getStandardMenuProduct() {
        MenuProduct standardMenuProduct = new MenuProduct();
        standardMenuProduct.setProductId(1L);
        standardMenuProduct.setMenuId(1L);
        standardMenuProduct.setSeq(1L);
        standardMenuProduct.setQuantity(1L);
        return standardMenuProduct;
    }

    public static List<MenuProduct> getStandardMenuProducts() {
        List<MenuProduct> standardMenuProducts = new ArrayList<>();
        standardMenuProducts.add(getStandardMenuProduct());
        return standardMenuProducts;
    }

    public static Menu getStandardMenu() {
        Menu standardMenu = new Menu();
        standardMenu.setName("메뉴이름");
        standardMenu.setId(1L);
        standardMenu.setPrice(new BigDecimal(1000));
        standardMenu.setMenuGroupId(1L);
        standardMenu.setMenuProducts(getStandardMenuProducts());
        return standardMenu;
    }

    public static List<Menu> getStandardMenus() {
        List<Menu> standardMenus = new ArrayList<>();
        standardMenus.add(getStandardMenu());
        return standardMenus;
    }
}
