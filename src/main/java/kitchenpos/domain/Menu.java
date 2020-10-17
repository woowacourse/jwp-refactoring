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

@Builder
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
    @Builder.Default
    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    @Builder
    private Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup,
        List<MenuProduct> menuProducts) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void addMenuProduct(MenuProduct menuProduct) {
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

    public List<MenuProduct> getMenuProducts() {
        return new ArrayList<>(menuProducts);
    }
}
