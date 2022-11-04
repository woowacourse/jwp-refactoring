package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup,
                final List<MenuProduct> menuProducts) {
        validatePrice(price);
        validateMenuPrice(price, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        addMenuProducts(menuProducts);
    }

    public Menu(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴 가격은 0원 미만일 수 없습니다.");
        }
    }

    private void validateMenuPrice(final BigDecimal price, final List<MenuProduct> menuProducts) {
        final BigDecimal sum = menuProducts.stream()
                .map(MenuProduct::calculatePrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴 상품 가격들의 총합보다 클 수 없습니다.");
        }
    }

    private void addMenuProducts(final List<MenuProduct> menuProducts) {
        menuProducts.forEach(menuProduct -> menuProduct.designateMenu(this));
        this.menuProducts.addAll(menuProducts);
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
}
