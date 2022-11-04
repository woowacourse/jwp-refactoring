package kitchenpos.domain.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private Price price;

    protected Menu() {
    }

    public Menu(String name, MenuGroup menuGroup, Price menuPrice, Price sumPrice) {
        validatePrice(menuPrice, sumPrice);
        this.name = name;
        this.menuGroup = menuGroup;
        this.price = menuPrice;
    }

    public Menu(Long id, String name, MenuGroup menuGroup, Price price) {
        this.id = id;
        this.name = name;
        this.menuGroup = menuGroup;
        this.price = price;
    }

    private void validatePrice(Price menuPrice, Price sumPrice) {
        if (menuPrice.isExpensiveThan(sumPrice)) {
            throw new IllegalArgumentException();
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }
}
