package kitchenpos.domain;

import kitchenpos.domain.vo.Price;

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

    @Embedded
    private Price price;

    private String name;

    public Menu() {
    }

    public Menu(
            final MenuGroup menuGroup,
            final Price price,
            final String name
    ) {
        this.menuGroup = menuGroup;
        this.price = price;
        this.name = name;
    }

    public static Menu of(
            final MenuGroup menuGroup,
            final String name,
            final BigDecimal price
    ) {
        return new Menu(menuGroup, new Price(price), name);
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
        return price.getValue();
    }
}
