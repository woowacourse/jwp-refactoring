//package fixture;
//
//import kitchenpos.domain.MenuProduct;
//
//public class MenuProductBuilder {
//    private Long seq;
//    private Long menuId;
//    private Long productId;
//    private long quantity;
//
//    public static MenuProductBuilder init() {
//        final MenuProductBuilder builder = new MenuProductBuilder();
//        builder.seq = 1L;
//        builder.menuId = 1L;
//        builder.productId = 1L;
//        builder.quantity = 100L;
//        return builder;
//    }
//
//    public MenuProductBuilder seq(Long seq) {
//        this.seq = seq;
//        return this;
//    }
//
//    public MenuProductBuilder menuId(Long menuId) {
//        this.menuId = menuId;
//        return this;
//    }
//
//    public MenuProductBuilder productId(Long productId) {
//        this.productId = productId;
//        return this;
//    }
//
//    public MenuProductBuilder quantity(long quantity) {
//        this.quantity = quantity;
//        return this;
//    }
//
//    public MenuProduct build() {
//        final MenuProduct menuProduct = new MenuProduct();
//        menuProduct.setSeq(this.seq);
//        menuProduct.connectMenu(this.menuId);
//        menuProduct.setProductId(this.productId);
//        menuProduct.setQuantity(this.quantity);
//        return menuProduct;
//    }
//}
