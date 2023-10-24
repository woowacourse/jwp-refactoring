package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.REMOVE})
    private List<MenuProduct> menuProducts;

    private String name;

    private BigDecimal price;

    public Menu() {
    }

    public Menu(
            final MenuGroup menuGroup,
            final String name,
            final BigDecimal price
    ) {
        this.menuGroup = menuGroup;
        this.name = name;
        this.price = price;
    }

    public void setMenuProducts(final List<MenuProduct> savedMenuProducts) {
        this.menuProducts = savedMenuProducts;
    }

    public Long getId() {
        return id;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
