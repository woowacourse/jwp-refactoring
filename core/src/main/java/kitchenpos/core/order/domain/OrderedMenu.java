package kitchenpos.core.order.domain;

import kitchenpos.core.menu.domain.Menu;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class OrderedMenu {

    @Column(name = "ordered_menu_id", nullable = false)
    private Long id;

    @Column(name = "ordered_menu_name", nullable = false)
    private String name;

    @Column(name = "ordered_price", nullable = false)
    private BigDecimal price;

    protected OrderedMenu() {
    }

    public OrderedMenu(final Long id,
                       final String name,
                       final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static OrderedMenu from(final Menu menu){
        return new OrderedMenu(menu.getId(),menu.getName().getName(),menu.getPrice().value());
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
}
