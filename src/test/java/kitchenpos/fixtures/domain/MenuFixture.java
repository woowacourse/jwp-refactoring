package kitchenpos.fixtures.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuFixture {

    public static Menu createMenu(final String name, final BigDecimal price, final Long menuGroupId,
                                  final List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

    public static class MenuRequestBuilder {

        private Long id;
        private String name = "세트1";
        private BigDecimal price = new BigDecimal(5_000);
        private Long menuGroupId;
        private List<MenuProduct> menuProducts;

        public MenuRequestBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public MenuRequestBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public MenuRequestBuilder price(final BigDecimal price) {
            this.price = price;
            return this;
        }

        public MenuRequestBuilder price(final int price) {
            this.price = new BigDecimal(price);
            return this;
        }

        public MenuRequestBuilder menuGroupId(final Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public MenuRequestBuilder menuProducts(final Collection<MenuProduct> menuProducts) {
            this.menuProducts = List.copyOf(menuProducts);
            return this;
        }

        public MenuRequestBuilder menuProducts(final MenuProduct... menuProducts) {
            this.menuProducts = List.of(menuProducts);
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
}
