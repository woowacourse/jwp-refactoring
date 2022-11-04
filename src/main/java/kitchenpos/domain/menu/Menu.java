package kitchenpos.domain.menu;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.domain.common.Price;

@Table(name = "menu")
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(final String name, final Price price, final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts, final MenuValidator menuValidator) {
        this(null, name, price, menuGroup, menuProducts, menuValidator);
    }

    public Menu(final Long id, final String name, final Price price, final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts, final MenuValidator menuValidator) {
        menuValidator.validatePrice(menuProducts, price);

        this.id = id;
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

    public Price getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
