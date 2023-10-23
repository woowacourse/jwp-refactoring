package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
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
import javax.persistence.Table;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(
            final Long id,
            final String name,
            final BigDecimal price,
            final MenuGroup menuGroup,
            final List<MenuProduct> menuProducts
    ) {
        validateMenuPriceSum(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = Price.from(price);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private void validateMenuPriceSum(final BigDecimal price, final List<MenuProduct> menuProducts) {
        final BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::calculatePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("[ERROR] 총 금액이 각 상품의 합보다 큽니다.");
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
