package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.menugroup.domain.MenuGroup;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts) {
        this.price = new Price(price);
        this.name = name;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        validatePrice(price, menuProducts);
    }

    public Menu() {
    }

    private void validatePrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        final BigDecimal sum = calculateTotalPrice(menuProducts);

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격과 메뉴 상품의 총 가격은 동일해야합니다.");
        }
    }

    private BigDecimal calculateTotalPrice(final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            final BigDecimal menuProductPrice = menuProduct.calculateTotalPrice();
            sum = sum.add(menuProductPrice);
        }
        return sum;
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
        return menuGroup.getId();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
