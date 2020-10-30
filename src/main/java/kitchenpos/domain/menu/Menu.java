package kitchenpos.domain.menu;

import kitchenpos.domain.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@AttributeOverride(name = "id", column = @Column(name = "id"))
@Table(name = "menu")
@Entity
public class Menu extends BaseEntity {
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    public Menu() {
    }

    public Menu(Long id) {
        this.id = id;
    }

    public Menu(Price price) {
        this.price = price;
    }

    public Menu(String name, Price price, MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public BigDecimal getPriceValue() {
        return this.price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }
}
