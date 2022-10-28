package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    @OneToOne
    private MenuGroup menuGroup;

    public Menu() {}

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        validatePriceIsNotNullOrNegative(price);

        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    private void validatePriceIsNotNullOrNegative(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
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
}
