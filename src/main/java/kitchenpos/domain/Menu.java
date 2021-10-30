package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Menu {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(null, name, price, menuGroup);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
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
