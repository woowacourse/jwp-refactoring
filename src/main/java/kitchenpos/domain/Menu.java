package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.exception.ApplicationException;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    private Long menuGroupId;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private List<MenuProduct> products;

    protected Menu() {
    }

    public Menu(final Name name, final Price price, final Long menuGroupId, final List<MenuProduct> products) {
        this(null, name, price, menuGroupId, products);
    }

    public Menu(final Long id, final Name name, final Price price, final Long menuGroupId,
                final List<MenuProduct> products) {
        validateNotOverMenuProductAmounts(price, products);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.products = products;
    }

    private void validateNotOverMenuProductAmounts(final Price price, final List<MenuProduct> products) {
        final Price sum = sumMenuProductAmounts(products);
        if (price.isGreaterThan(sum)) {
            throw new DomainLogicException(CustomError.MENU_PRICE_ERROR);
        }
    }

    private Price sumMenuProductAmounts(final List<MenuProduct> products) {
        return products.stream()
                .map(MenuProduct::calculateAmount)
                .reduce(Price::sum)
                .orElseThrow(() -> new ApplicationException(CustomError.APPLICATION_ERROR));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getProducts() {
        return products;
    }
}
