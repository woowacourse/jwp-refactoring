package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
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
    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    @Builder
    public Menu(
        final String name,
        final BigDecimal price,
        final MenuGroup menuGroup,
        final List<MenuProduct> menuProducts
    ) {
        validatePrice(price);
        this.name = name;
        this.price = price;
        setMenuGroup(menuGroup);
        setMenuProducts(menuProducts);
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void setMenuGroup(final MenuGroup menuGroup) {
        if (Objects.isNull(this.menuGroup) && Objects.nonNull(menuGroup)) {
            this.menuGroup = menuGroup;
            this.menuGroup.addMenu(this);
        }
    }

    private void setMenuProducts(final List<MenuProduct> menuProducts) {
        if (Objects.nonNull(menuProducts)) {
            menuProducts.forEach(this::addMenuProduct);
        }
        validateMenuProductsPrice();
    }

    public void addMenuProduct(final MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
        menuProduct.setMenu(this);
    }

    private void validateMenuProductsPrice() {
        BigDecimal sum = calculateProductPrice();
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal calculateProductPrice() {
        return menuProducts.stream()
            .map(MenuProduct::calculatePrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isSameId(final Long menuId) {
        return menuId.equals(this.id);
    }

    public List<MenuProduct> getMenuProducts() {
        return new ArrayList<>(menuProducts);
    }
}
