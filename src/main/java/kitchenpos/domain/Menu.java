package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    private Menu(final Long id,
                 final String name,
                 final Price price,
                 final MenuGroup menuGroup,
                 final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu of(final String name,
                          final BigDecimal price,
                          final MenuGroup menuGroup) {

        return new Menu(null, name, new Price(price), menuGroup, new ArrayList<>());
    }

    public static Menu of(final String name,
                          final BigDecimal price,
                          final MenuGroup menuGroup,
                          final List<MenuProduct> menuProducts) {
        return new Menu(null, name, new Price(price), menuGroup, menuProducts);
    }

    public boolean isGreaterThan(final Price other) {
        return price.isGreaterThan(other);
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
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
