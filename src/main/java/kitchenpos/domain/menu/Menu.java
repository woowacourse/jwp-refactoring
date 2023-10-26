package kitchenpos.domain.menu;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Menu {

    @Id
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    @MappedCollection(idColumn = "MENU_ID")
    private final Set<MenuProduct> menuProducts;

    private Menu(Long id, String name, BigDecimal price, Long menuGroupId, Set<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuBuilder builder() {
        return new MenuBuilder();
    }

    public static final class MenuBuilder {
        private Long id;
        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private Set<MenuProduct> menuProducts = new HashSet<>();

        private MenuBuilder() {
        }

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

        public MenuBuilder menuProducts(Set<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(id, name, price, menuGroupId, menuProducts);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public Set<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
