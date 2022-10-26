package kitchenpos.fixtures;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public enum MenuFixtures {

    TWO_CHICKEN_COMBO("두마리 치킨 콤보", new BigDecimal(30_000), 1L, new ArrayList<>());

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProduct> menuProducts;

    MenuFixtures(final String name, final BigDecimal price, final Long menuGroupId,
                 final List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu create() {
        return new Menu(null, name, price, menuGroupId, menuProducts);
    }
    
    public Menu createWithPrice(final BigDecimal price) {
        return new Menu(null, name, price, menuGroupId, menuProducts);
    }

    public Menu createWithMenuGroupId(final Long menuGroupId) {
        return new Menu(null, name, price, menuGroupId, menuProducts);
    }

    public Menu createWithMenuProducts(final MenuProduct... menuProducts) {
        return new Menu(null, name, price, menuGroupId, Arrays.asList(menuProducts));
    }

    public Menu createWithPriceAndMenuProducts(final BigDecimal price, final MenuProduct... menuProducts) {
        return new Menu(null, name, price, menuGroupId, Arrays.asList(menuProducts));
    }

    /*public static class Builder {

        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private List<MenuProduct> menuProducts;

        public Builder setName(final String name){
            this.name = name;
            return this;
        }

        public Builder setPrice(final BigDecimal price){
            this.price = price;
            return this;
        }

        public Builder setMenuGroupId(final Long menuGroupId){
            this.menuGroupId = menuGroupId;
            return this;
        }

        public Builder setMenuProducts(final MenuProduct... menuProducts){
            this.menuProducts = Arrays.asList(menuProducts);
            return this;
        }
    }*/
}
