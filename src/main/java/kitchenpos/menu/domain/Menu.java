package kitchenpos.menu.domain;

import kitchenpos.generic.Price;
import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MENU_GROUP_ID", foreignKey = @ForeignKey(name = "FK_MENU_MENU_GROUP"))
    private MenuGroup menuGroup;

    public Menu() {
    }

    public Menu(String name, Long price, MenuGroup menuGroup) {
        this.name = name;
        this.price = Price.of(price);
        this.menuGroup = menuGroup;
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
}
