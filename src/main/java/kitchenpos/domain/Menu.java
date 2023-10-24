package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
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
    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(
        Long id,
        String name,
        BigDecimal price,
        MenuGroup menuGroup,
        List<MenuProduct> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts == null ? new ArrayList<>() : menuProducts;
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
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
