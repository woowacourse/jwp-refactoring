package kitchenpos.menu.domain;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.vo.Money;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price", nullable = false))
    private Money price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", nullable = false, updatable = false)
    private MenuGroup menuGroup;

    protected Menu() {
    }

    public Menu(
            final String name,
            final BigDecimal price,
            final MenuGroup menuGroup
    ) {
        this(null, name, price, menuGroup);
    }

    private Menu(
            final Long id,
            final String name,
            final BigDecimal price,
            final MenuGroup menuGroup
    ) {
        this.id = id;
        this.name = name;
        this.price = Money.valueOf(price);
        this.menuGroup = menuGroup;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }
}
