package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kitchenpos.menugroup.domain.MenuGroup;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup,
            final List<MenuProduct> menuProducts) {
        validateByPrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup,
            final List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    private void validateByPrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("금액이 0미만일수 없습니다.");
        }
    }

    public void validateByPriceWithMenuProductSum() {
        final BigDecimal sumMenuProduct = menuProducts.stream()
                .map(MenuProduct::getTotalPrice)
                .reduce(BigDecimal::add)
                .orElseThrow(IllegalArgumentException::new);

        if (price.compareTo(sumMenuProduct) > 0) {
            throw new IllegalArgumentException("메뉴 가격이 메뉴 상품 가격의 합보다 작을 수 없습니다.");
        }
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
