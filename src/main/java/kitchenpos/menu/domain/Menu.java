package kitchenpos.menu.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.vo.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    protected Menu() {
    }

    public Menu(String name, Price price, MenuGroup menuGroup) {
        this(null, name, price, menuGroup);
    }

    public Menu(Long id, String name, Price price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
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

    public MenuGroup menuGroup() {
        return menuGroup;
    }
}
