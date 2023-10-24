package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "menu_menu_group"))
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        for (final MenuProduct menuProduct : menuProducts) {
            this.menuProducts.add(menuProduct);
            menuProduct.setMenu(this);
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
