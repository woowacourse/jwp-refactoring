package kitchenpos.order;

import kitchenpos.common.Price;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderMenu {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    protected OrderMenu() {

    }

    public OrderMenu(final Long menuId, final String name, final Price price) {
        this(null, menuId, name, price);
    }

    public OrderMenu(final Long id, final Long menuId, final String name, final Price price) {
        this.id = id;
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
