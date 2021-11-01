package kitchenpos.testutils;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

import java.math.BigDecimal;
import java.util.List;

public class TestDomainBuilder {

    public static ProductBuilder productBuilder() {
        return new ProductBuilder();
    }

    public static MenuGroupBuilder menuGroupBuilder() {
        return new MenuGroupBuilder();
    }

    public static MenuBuilder menuBuilder() {
        return new MenuBuilder();
    }

    public static MenuProductBuilder menuProductBuilder() {
        return new MenuProductBuilder();
    }

    public static class ProductBuilder {
        private Long id;
        private String name;
        private BigDecimal price;

        public ProductBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Product build() {
            Product product = new Product();
            product.setId(id);
            product.setName(name);
            product.setPrice(price);
            return product;
        }
    }

    public static class MenuGroupBuilder {
        private Long id;
        private String name;

        public MenuGroupBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MenuGroupBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuGroup build() {
            MenuGroup menuGroup = new MenuGroup();
            menuGroup.setId(id);
            menuGroup.setName(name);
            return menuGroup;
        }
    }

    public static class MenuBuilder {
        private Long id;
        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private List<MenuProduct> menuProducts;

        public MenuBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MenuBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public MenuBuilder menuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public MenuBuilder menuProducts(List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            Menu menu = new Menu();
            menu.setId(id);
            menu.setName(name);
            menu.setPrice(price);
            menu.setMenuGroupId(menuGroupId);
            menu.setMenuProducts(menuProducts);
            return menu;
        }
    }

    public static class MenuProductBuilder {
        private Long seq;
        private Long menuId;
        private Long productId;
        private long quantity;

        public MenuProductBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public MenuProductBuilder menuId(Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public MenuProductBuilder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public MenuProductBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setSeq(seq);
            menuProduct.setMenuId(menuId);
            menuProduct.setProductId(productId);
            menuProduct.setQuantity(quantity);
            return menuProduct;
        }
    }
}
