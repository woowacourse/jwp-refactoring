package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @OneToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(nullable = false)
    private long quantity;

    protected OrderLineItem() {
    }
}
