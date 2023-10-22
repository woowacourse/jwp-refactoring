package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup) {
        this(null, name, price, menuGroup, null);
    }

    public Menu(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup) {
        this(id, name, price, menuGroup, null);
    }

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        addMenuProducts(menuProducts);
    }

    private void addMenuProducts(final List<MenuProduct> menuProducts) {
        if (menuProducts == null) {
            return;
        }
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.updateMenu(this);
        }
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
