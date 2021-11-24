package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(final String name, final Long price, final MenuGroup menuGroup, final List<MenuProduct> menuProducts) {
        this(null, name, new BigDecimal(price), menuGroup, menuProducts);
    }

    public Menu(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup, final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        validate();
    }

    private void validate() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void validatePrice(final BigDecimal value) {
        if (price.compareTo(value) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public void changeMenuProduct(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public Long price() {
        return price.longValue();
    }

    public Long menuGroupId() {
        return menuGroup.getId();
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
