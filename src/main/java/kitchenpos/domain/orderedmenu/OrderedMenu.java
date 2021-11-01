package kitchenpos.domain.orderedmenu;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.price.Price;

@Entity
public class OrderedMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long menuId;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    protected OrderedMenu() {
    }

    public OrderedMenu(Long menuId, String name, Price price) {
        this(null, menuId, name, price);
    }

    public OrderedMenu(Long id, Long menuId, String name, Price price) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderedMenu that = (OrderedMenu) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
