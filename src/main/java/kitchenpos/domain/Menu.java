package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    private Menu(final Long id,
                 final String name,
                 final BigDecimal price,
                 final MenuGroup menuGroup,
                 final List<MenuProduct> menuProducts) {
        validatePrice(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu of(final String name,
                          final Long price,
                          final MenuGroup menuGroup) {
        validateNull(price);
        return new Menu(null, name, BigDecimal.valueOf(price), menuGroup, new ArrayList<>());
    }

    public static Menu of(final String name,
                          final Long price,
                          final MenuGroup menuGroup,
                          final List<MenuProduct> menuProducts) {
        validateNull(price);
        return new Menu(null, name, BigDecimal.valueOf(price), menuGroup, menuProducts);
    }

    public static Menu of(final Long id,
                          final String name,
                          final BigDecimal price,
                          final MenuGroup menuGroup) {
        return new Menu(id, name, price, menuGroup, new ArrayList<>());
    }

    private static void validateNull(final Long price) {
        if (price == null) {
            throw new IllegalArgumentException("메뉴의 가격은 null일 수 없습니다.");
        }
    }

    private void validatePrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isGreaterThan(final BigDecimal other) {
        return price.compareTo(other) > 0;
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

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void updateMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}
