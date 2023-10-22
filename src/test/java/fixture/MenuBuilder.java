//package fixture;
//
//import kitchenpos.domain.Menu;
//import kitchenpos.domain.MenuProduct;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//public class MenuBuilder {
//    private Long id;
//    private String name;
//    private BigDecimal price;
//    private Long menuGroupId;
//    private List<MenuProduct> menuProducts;
//
//    public static MenuBuilder init() {
//        final MenuBuilder builder = new MenuBuilder();
//        builder.id = null;
//        builder.name = "메뉴";
//        builder.price = BigDecimal.valueOf(10000);
//        builder.menuGroupId = 1L;
//        builder.menuProducts = List.of(MenuProductBuilder.init().build());
//        return builder;
//    }
//
//    public MenuBuilder id(Long id) {
//        this.id = id;
//        return this;
//    }
//
//    public MenuBuilder name(String name) {
//        this.name = name;
//        return this;
//    }
//
//    public MenuBuilder price(Long price) {
//        this.price = BigDecimal.valueOf(price);
//        return this;
//    }
//
//    public MenuBuilder menuGroupId(Long menuGroupId) {
//        this.menuGroupId = menuGroupId;
//        return this;
//    }
//
//    public MenuBuilder menuProducts(List<MenuProduct> menuProducts) {
//        this.menuProducts = menuProducts;
//        return this;
//    }
//
//    public Menu build() {
//        final Menu menu = new Menu();
//        menu.setId(this.id);
//        menu.setName(this.name);
//        menu.setPrice(this.price);
//        menu.setMenuGroupId(this.menuGroupId);
//        menu.setMenuProducts(this.menuProducts);
//        return menu;
//    }
//}
