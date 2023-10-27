package kitchenpos.menu.domain;

import kitchenpos.menu.domain.vo.Name;
import kitchenpos.menu.domain.vo.Price;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity
public class MenuHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(name = "fk_menu_history_to_order_id"))
    private Long orderId;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @Embedded
    private MenuProductHistories menuProductHistories;

    protected MenuHistory() {
    }

    public MenuHistory(final Long orderId,
                       final Name name,
                       final Price price,
                       final MenuProductHistories menuProductHistories
    ) {
        this(null, orderId, name, price, menuProductHistories);
    }

    protected MenuHistory(final Long id,
                          final Long orderId,
                          final Name name,
                          final Price price,
                          final MenuProductHistories menuProductHistories
    ) {
        this.id = id;
        this.orderId = orderId;
        this.name = name;
        this.price = price;
        this.menuProductHistories = menuProductHistories;
    }

    public static MenuHistory of(final Long orderId, final Menu menu) {
        return new MenuHistory(
                orderId,
                menu.getName(),
                menu.getPrice(),
                MenuProductHistories.from(menu.getMenuProducts())
        );
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public MenuProductHistories getMenuProductHistories() {
        return menuProductHistories;
    }
}
