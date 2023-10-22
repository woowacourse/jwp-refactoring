package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private MenuPrice price;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(final Long id, final String name, final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.menuProducts = new MenuProducts(menuProducts);
        this.price = this.menuProducts.calculatePrice();
    }

    public static Menu forSave(final String name, final List<MenuProduct> menuProducts) {
        return new Menu(null, name, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuPrice getPrice() {
        return price;
    }

    public List<MenuProduct> getMenuProducts() {
        return new ArrayList<>(menuProducts.getMenuProducts());
    }
}
