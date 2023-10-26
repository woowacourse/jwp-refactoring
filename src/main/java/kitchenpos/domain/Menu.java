package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    public Menu() {
    }

    public Menu(final String name, final Price price, final MenuGroup menuGroup) {
        this(null, name, price, menuGroup);
    }

    public Menu(final Long id, final String name, final Price price, final MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }
}
