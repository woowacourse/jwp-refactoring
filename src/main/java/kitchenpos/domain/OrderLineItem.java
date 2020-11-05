package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.builder.OrderLineItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private long quantity;

    public static OrderLineItemBuilder builder() {
        return new OrderLineItemBuilder();
    }

    public OrderLineItemBuilder toBuilder() {
        return new OrderLineItemBuilder(seq, orderId, menuId, quantity);
    }
}
