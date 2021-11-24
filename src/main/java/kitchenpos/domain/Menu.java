package kitchenpos.domain;

import java.math.BigDecimal;
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
import kitchenpos.exception.InvalidPriceException;

@Entity
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private MenuPrice price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = CascadeType.PERSIST,
        orphanRemoval = true
    )
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(
        String name,
        BigDecimal price,
        MenuGroup menuGroup,
        List<MenuProduct> menuProducts
    ) {
        this(null, name, price, menuGroup, menuProducts);
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
        this.price = new MenuPrice(price);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        validateMenuPrice();
    }

    private void validateMenuPrice() {
        BigDecimal totalMenuPrice = this.menuProducts.stream()
            .map(MenuProduct::calculatePrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.isBigger(totalMenuPrice)) {
            throw new InvalidPriceException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
