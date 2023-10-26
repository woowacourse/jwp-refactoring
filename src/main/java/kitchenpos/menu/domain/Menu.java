package kitchenpos.menu.domain;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    private Long menuGroupId;

    protected Menu() {
    }

    public Menu(
            final String name,
            final BigDecimal price,
            final Long menuGroupId
    ) {
        this(null, name, price, menuGroupId);
    }

    public Menu(
            final Long id,
            final String name,
            final BigDecimal price,
            final Long menuGroupId
    ) {
        this.id = id;
        this.name = name;
        this.price = Money.valueOf(price);
        this.menuGroupId = menuGroupId;
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}
