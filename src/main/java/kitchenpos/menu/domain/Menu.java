package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.validator.MenuValidator;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;
    private Long menuGroupId;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> products;

    protected Menu() {
    }

    private Menu(String name, Price price, Long menuGroupId, List<MenuProduct> products) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.products = products;
        for (MenuProduct menuProduct : products) {
            menuProduct.setMenu(this);
        }
    }

    public static Menu of(String name, BigDecimal menuPrice, Long menuGroupId,
                          List<MenuProduct> menuProducts, MenuValidator menuValidator) {
        Price price = new Price(menuPrice);
        menuValidator.validateCreation(price, menuGroupId, menuProducts);
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getProducts() {
        return products;
    }
}
