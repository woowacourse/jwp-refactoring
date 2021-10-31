package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import kitchenpos.domain.validator.MenuValidator;

@Entity
public class Menu {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    @Transient
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    private Menu(Long id, String name, BigDecimal price, Long menuGroupId,
                 List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = new MenuProducts(menuProducts);
    }

    public void register(MenuValidator menuValidator) {
        menuValidator.validate(this);
        menuProducts.setMenu(this);
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

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(MenuProducts menuProducts) {
        this.menuProducts = menuProducts;
    }
}
