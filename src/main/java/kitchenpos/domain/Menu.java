package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.exception.MenuPriceIsBiggerThanActualPriceException;
import kitchenpos.exception.MenuPriceIsNegativeException;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private Money price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        validatePrice(price);
        this.name = name;
        this.price = new Money(price);
        this.menuGroup = menuGroup;
    }

    private void validatePrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new MenuPriceIsNegativeException();
        }
    }

    public void setupMenuProduct(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
        validateMenuPriceIsNotBiggerThanActualPrice();
    }

    private void validateMenuPriceIsNotBiggerThanActualPrice() {
        Money actualPrice = menuProducts.stream()
                .map(MenuProduct::calculatePrice)
                .map(Money::new)
                .reduce(Money::add)
                .orElse(Money.ZERO);

        if (price.isGreaterThan(actualPrice)) {
            throw new MenuPriceIsBiggerThanActualPriceException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
