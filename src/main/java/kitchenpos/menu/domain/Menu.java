package kitchenpos.menu.domain;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.common.vo.Money;
import kitchenpos.menu.exception.MenuPriceIsBiggerThanActualPriceException;

@Table(name = "menu")
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_group_id")
    private Long menuGroupId;

    private String name;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private Money price;

    protected Menu() {
    }

    public Menu(Long menuGroupId, String name, BigDecimal price) {
        this.menuGroupId = menuGroupId;
        this.name = name;
        this.price = Money.fromNonNegative(price);
    }

    public void setupMenuProducts(List<MenuProduct> menuProducts) {
        validateMenuPriceIsNotBiggerThanActualPrice(menuProducts);
        this.menuProducts = menuProducts;
    }

    private void validateMenuPriceIsNotBiggerThanActualPrice(List<MenuProduct> menuProducts) {
        Money actualPrice = calculateActualPrice(menuProducts);

        if (price.isGreaterThan(actualPrice)) {
            throw new MenuPriceIsBiggerThanActualPriceException();
        }
    }

    private Money calculateActualPrice(List<MenuProduct> menuProducts) {
        Money actualPrice = Money.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            BigDecimal menuProductPrice = menuProduct.getPrice()
                    .multiply(BigDecimal.valueOf(menuProduct.getQuantity()));

            actualPrice = actualPrice.add(Money.fromNonNegative(menuProductPrice));
        }
        return actualPrice;
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
