//package kitchenpos.fixture;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.List;
//import kitchenpos.domain.Menu;
//import kitchenpos.domain.MenuProduct;
//
//public class MenuFixture {
//
//    public Menu 메뉴_생성(
//            String name,
//            BigDecimal price,
//            Long menuGroupId,
//            List<MenuProduct> menuProducts
//    ) {
//        Menu menu = new Menu();
//        menu.setName(name);
//        menu.setPrice(price);
//        menu.setMenuGroupId(menuGroupId);
//        menu.setMenuProducts(menuProducts);
//        return menu;
//    }
//
//    public Menu 메뉴_생성(
//            Long id,
//            String name,
//            BigDecimal price,
//            Long menuGroupId,
//            List<MenuProduct> menuProducts
//    ) {
//        Menu menu = new Menu();
//        menu.setId(id);
//        menu.setName(name);
//        menu.setPrice(price);
//        menu.setMenuGroupId(menuGroupId);
//        menu.setMenuProducts(menuProducts);
//        return menu;
//    }
//
//    public List<Menu> 메뉴_리스트_생성(Menu... menu) {
//        return Arrays.asList(menu);
//    }
//}
