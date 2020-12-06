package kitchenpos.order.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderLineItems {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ORDER_ID")
    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    public void add(OrderLineItem orderLineItem) {
        if (this.orderLineItems.contains(orderLineItem)) {
            throw new IllegalArgumentException("이미 추가된 메뉴 상품입니다.");
        }
        this.orderLineItems.add(orderLineItem);
    }

    public List<OrderLineItem> showAll() {
        return Collections.unmodifiableList(this.orderLineItems);
    }

}
