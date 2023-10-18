package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    @ManyToOne
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final MenuGroup menuGroup) {
        this(null, name, price, menuGroup);
    }

    public Menu(final Long id, final String name, final BigDecimal price,
        final MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public boolean isOverPrice() {
        final long sum = menuProducts.stream()
            .map(MenuProduct::calculateTotalPrice)
            .mapToLong(BigDecimal::longValue)
            .sum();
        final BigDecimal totalPrice = BigDecimal.valueOf(sum);

        return Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0 ||
            totalPrice.compareTo(price) < 0;
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
