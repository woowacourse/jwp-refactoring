package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.exception.MenuException;

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
    private String orderedMenuGroupName;

    protected OrderedMenu() {
    }

    public OrderedMenu(String name, Price price, String orderedMenuGroupName) {
        this.name = name;
        this.price = price;
        this.orderedMenuGroupName = orderedMenuGroupName;
    }

    public OrderedMenu(Long id, String name, Price price, String orderedMenuGroupName) {
        validateName(name);

        this.id = id;
        this.name = name;
        this.price = price;
        this.orderedMenuGroupName = orderedMenuGroupName;
    }

    private void validateName(String name) {
        if (name.isBlank() || name.length() > 255) {
            throw new MenuException("메뉴의 이름이 유효하지 않습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public String getOrderedMenuGroupName() {
        return orderedMenuGroupName;
    }
}
