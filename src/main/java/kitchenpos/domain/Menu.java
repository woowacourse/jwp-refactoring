package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @Column(nullable = false)
    private Long menuGroupId;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    private Menu(final Long id, final String name, final Price price, final Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public static Menu forSave(final String name, final Integer price, final Long menuGroupId) {
        return new Menu(null, name, Price.from(price), menuGroupId);
    }

    public static Menu saved(final Long id, final String name, final Integer price, final Long menuGroupId) {
        return new Menu(id, name, Price.from(price), menuGroupId);
    }


    public void validateOverPrice(final BigDecimal productSumPrice) {
        if (price.isBigger(productSumPrice)) {
            throw new IllegalArgumentException();
        }
    }

    public void addMenuProduct(final MenuProduct menuProduct) {
        menuProduct.setMenu(this);
        menuProducts.add(menuProduct);
    }


    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public BigDecimal getPrice() {
        return price.getValue();
    }


    public Long getMenuGroupId() {
        return menuGroupId;
    }


    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
