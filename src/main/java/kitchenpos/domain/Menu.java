package kitchenpos.domain;

import kitchenpos.domain.vo.Price;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", updatable = false)
    private MenuGroup menuGroup;

    @Embedded
    private Price price;

    private String name;

    public Menu() {
    }

    public Menu(
            final MenuGroup menuGroup,
            final Price price,
            final String name
    ) {
        this.menuGroup = menuGroup;
        this.price = price;
        this.name = name;
    }

    public static Menu of(
            final MenuGroup menuGroup,
            final String name,
            final BigDecimal price
    ) {
        return new Menu(menuGroup, new Price(price), name);
    }

    public Long getId() {
        return id;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
