package kitchenpos.support;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;

public abstract class MenuProductFixture {

    public static List<MenuProduct> menuProductOne(long productId, long quantity) {

        Product product = new Product(productId, "상품" , BigDecimal.valueOf(quantity));

        return List.of(new MenuProduct(null, null, product, quantity));
    }

    public static List<MenuProduct> menuProducts(long productId1, long quantity1, long productId2, long quantity2) {

        Product product1 = new Product(productId1, "상품" , BigDecimal.valueOf(quantity1));
        Product product2 = new Product(productId2, "상품" , BigDecimal.valueOf(quantity2));

        return List.of(new MenuProduct(product1, quantity1), new MenuProduct(product2, quantity2));
    }

    public static MenuProductBuilder menuProductBuilder() {
        return new MenuProductBuilder();
    }

    public static class MenuProductBuilder {

        private Long seq;
        private Menu menu;
        private Product product;
        private long quantity;

        public MenuProductBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public MenuProductBuilder menu(Menu menu) {
            this.menu = menu;
            return this;
        }

        public MenuProductBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public MenuProductBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            return new MenuProduct(seq, menu, product, quantity);
        }
    }
}
