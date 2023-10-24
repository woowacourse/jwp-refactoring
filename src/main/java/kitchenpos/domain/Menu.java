package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.domain.vo.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    protected Menu() {
    }

    private Menu(
            final MenuGroup menuGroup,
            final List<MenuProduct> menuProducts,
            final String name,
            final BigDecimal price
    ) {
        this.menuGroup = menuGroup;
        this.name = name;
        this.price = new Price(price);

        BigDecimal amountSum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            amountSum = amountSum.add(menuProduct.calculateAmount());
            addMenuProduct(menuProduct);
        }
        validateMenuPrice(price, amountSum);
    }

    public static Menu of(
            final MenuGroup menuGroup,
            final List<MenuProduct> menuProducts,
            final String name,
            final BigDecimal price
    ) {
        BigDecimal amountSum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            amountSum = amountSum.add(menuProduct.calculateAmount());
        }
        validateMenuPrice(price, amountSum);
        return new Menu(menuGroup, menuProducts, name, price);
    }

    private static void validateMenuPrice(final BigDecimal price, final BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 금액(가격 * 수량)의 합보다 클 수 없습니다.");
        }
    }

    public void addMenuProduct(final MenuProduct menuProduct) {
        menuProduct.setMenu(this);
        this.menuProducts.add(menuProduct);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Menu menu = (Menu) o;
        return Objects.equals(getId(), menu.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public Long getId() {
        return id;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
