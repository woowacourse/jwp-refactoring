package kitchenpos.order.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import kitchenpos.Money;

@Embeddable
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "menu_id")),
        @AttributeOverride(name = "name", column = @Column(name = "menu_name"))
})
public class MenuVo {

    private Long id;
    private String name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "menu_price"))
    private Money price;

    protected MenuVo() {
    }

    public MenuVo(final Long id, final String name, final Money price) {
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

    public Money getPrice() {
        return price;
    }

}
