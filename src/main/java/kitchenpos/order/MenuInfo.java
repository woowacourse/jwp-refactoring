package kitchenpos.order;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuInfo {

    @Column(name = "menu_id")
    private Long id;
    @Column(name = "menu_name")
    private String name;
    @Column(name = "menu_price")
    private BigDecimal price;

    public MenuInfo(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    protected MenuInfo() {
    }
}
