package kitchenpos.domain.order;

import kitchenpos.domain.menu.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "MENU_ID")
    private Menu menu;

    private Long quantity;

    public OrderLineItem(Order order, Menu menu, Long quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }
}
