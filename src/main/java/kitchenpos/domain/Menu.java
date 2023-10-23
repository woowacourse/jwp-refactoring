package kitchenpos.domain;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Menu {

    @Id
    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final MenuGroup menuGroup;
    private final List<MenuProduct> menuProducts;

    private Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validate(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private void validate(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void validPrice(BigDecimal price) {
        if (this.price.compareTo(price) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public Menu updateMenuProducts(List<MenuProduct> savedMenuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct savedMenuProduct : savedMenuProducts) {
            sum = sum.add(savedMenuProduct.getPrice());
        }
        validPrice(sum);
        return new Menu(id, name, price, menuGroup, savedMenuProducts);
    }

    public static MenuBuilder builder() {
        return new MenuBuilder();
    }

    public static final class MenuBuilder {
        private Long id;
        private String name;
        private BigDecimal price;
        private MenuGroup menuGroup;
        private List<MenuProduct> menuProducts = new ArrayList<>();

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

        public MenuBuilder menuGroup(MenuGroup menuGroup) {
            this.menuGroup = menuGroup;
            return this;
        }

        public MenuBuilder menuProducts(List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(id, name, price, menuGroup, menuProducts);
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
