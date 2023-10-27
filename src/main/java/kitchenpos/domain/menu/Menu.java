package kitchenpos.domain.menu;

import kitchenpos.common.BaseDate;
import kitchenpos.domain.menugroup.MenuGroup;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Menu extends BaseDate {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup, final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup, final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    protected Menu() {
    }

    public void validate(final MenuValidator menuValidator) {
        menuValidator.validate(this);
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
