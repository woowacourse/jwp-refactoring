package kitchenpos.domain.menu;

import kitchenpos.domain.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@AttributeOverride(name = "id", column = @Column(name = "MENU_ID"))
public class Menu extends BaseEntity {
    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "MENU_GROUP_ID")
    private MenuGroup menuGroup;

    protected Menu() {
    }

    public Menu(String name, Price price, MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public Menu(String name, Long price, MenuGroup menuGroup) {
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public String getName() {
        return name;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }
}
