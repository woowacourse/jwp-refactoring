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
    private Menu(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
        setMenuGroup(menuGroup);
    }

    private void setMenuGroup(final MenuGroup menuGroup) {
        if (Objects.isNull(this.menuGroup) && Objects.nonNull(menuGroup)) {
            this.menuGroup = menuGroup;
            this.menuGroup.addMenu(this);
        }
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void addMenuProduct(final MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public boolean isNotValidPrice() {
        BigDecimal sum = calculateProductPrice();
        return price.compareTo(sum) > 0;
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
