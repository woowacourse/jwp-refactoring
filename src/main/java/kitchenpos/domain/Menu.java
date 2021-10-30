package kitchenpos.domain;

import kitchenpos.exception.menu.MenuPriceOverThanProductsException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(String name, Long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = Price.from(price);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;

        validatePrice(menuProducts);

        for (MenuProduct menuProduct : menuProducts) {
            if (menuProduct != null) {
                menuProduct.belongsTo(this);
            }
        }
    }

    private void validatePrice(List<MenuProduct> menuProducts) {
        long totalPriceOfMenuProducts = menuProducts.stream()
                .mapToLong(MenuProduct::getTotalPrice)
                .sum();
        if (totalPriceOfMenuProducts < price.longValue()) {
            throw new MenuPriceOverThanProductsException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price.longValue();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
