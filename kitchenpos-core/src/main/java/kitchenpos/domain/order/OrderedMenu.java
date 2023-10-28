package kitchenpos.domain.order;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.vo.Price;

@Entity
public class OrderedMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @Column(nullable = false)
    private String menuGroupName;

    public OrderedMenu() {
    }

    public OrderedMenu(final String name, final Price price, final String menuGroupName) {
        this.name = name;
        this.price = price;
        this.menuGroupName = menuGroupName;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Price price() {
        return price;
    }

    public String menuGroupName() {
        return menuGroupName;
    }
}
