package kitchenpos.domain.menu;

import kitchenpos.domain.menuproduct.MenuProducts;
import kitchenpos.domain.Price;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menuproduct.MenuProduct;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(String name, BigDecimal price) {
        this(name, new Price(price));
    }

    public Menu(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(name, new Price(price), menuGroup, new MenuProducts());
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this(name, new Price(price), menuGroup, menuProducts);
    }

    public Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public void changeMenuProducts(List<MenuProduct> menuProducts) {
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenu(this);
        }
        this.menuProducts = MenuProducts.of(price.getValue(), menuProducts);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public void setPrice(final Price price) {
        this.price = price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void changeMenuGroup(final MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public void setMenuProducts(final MenuProducts menuProducts) {
        this.menuProducts = menuProducts;
    }
}
