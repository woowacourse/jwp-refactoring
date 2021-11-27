package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.exception.InvalidPriceException;

@Entity
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private MenuPrice price;

    @Column(name = "menu_group_id")
    private Long menuGroupId;

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
        Long menuGroupId,
        List<MenuProduct> menuProducts
    ) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(
        Long id,
        String name,
        BigDecimal price,
        Long menuGroupId,
        List<MenuProduct> menuProducts
    ) {
        this.id = id;
        this.name = name;
        this.price = new MenuPrice(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public void validateMenuPrice(MenuProductCalculator menuProductCalculator) {
        BigDecimal totalMenuPrice = this.menuProducts.stream()
            .map(menuProductCalculator::totalPrice)
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
