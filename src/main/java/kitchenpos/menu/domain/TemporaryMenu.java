package kitchenpos.menu.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class TemporaryMenu {

    @Column
    private Long id;

    @Column
    private String name;
    @Column
    private BigDecimal price;

    protected TemporaryMenu() {
    }

    public TemporaryMenu(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
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
