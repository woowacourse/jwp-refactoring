package kitchenpos.Menu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    @Column(name = "menu_group_id")
    private Long menuGroupId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_products_id")
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(Long id) {
        this(id, null, null, null, null);
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void enrollMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
