package kitchenpos.menu.domain;

import kitchenpos.menu.domain.vo.Name;
import kitchenpos.menu.domain.vo.Price;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @Embedded
    private MenuProductHistories menuProductHistories;

    protected MenuHistory() {
    }

    public MenuHistory(final Name name,
                       final Price price,
                       final MenuProductHistories menuProductHistories
    ) {
        this(null, name, price, menuProductHistories);
    }

    protected MenuHistory(final Long id,
                          final Name name,
                          final Price price,
                          final MenuProductHistories menuProductHistories
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProductHistories = menuProductHistories;
    }

    public static MenuHistory from(final Menu menu) {
        return new MenuHistory(
                menu.getName(),
                menu.getPrice(),
                MenuProductHistories.from(menu.getMenuProducts())
        );
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public MenuProductHistories getOrderProductHistories() {
        return menuProductHistories;
    }
}
