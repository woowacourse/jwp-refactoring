package kitchenpos.menu.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(fetch = FetchType.LAZY, cascade = ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id", updatable = false, nullable = false)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(String name,
                BigDecimal price,
                MenuGroup menuGroup,
                List<MenuProduct> menuProducts,
                MenuValidator menuValidator
    ) {
        menuValidator.validateCreate(price, menuGroup, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
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
