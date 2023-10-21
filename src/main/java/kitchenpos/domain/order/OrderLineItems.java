package kitchenpos.domain.order;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {

    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderLineItem> collection;

    public OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> collection) {
        validateSize(collection);
        validateDuplicatedMenu(collection);
        this.collection = collection;
    }

    private void validateDuplicatedMenu(List<OrderLineItem> collection) {
        int menuCount = collection.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toSet()).size();
        int orderLineItemSize = collection.size();
        if (menuCount != orderLineItemSize) {
            throw new IllegalArgumentException("한번의 주문에서 중복 메뉴를 주문할 수 없습니다.");
        }
    }

    private void validateSize(List<OrderLineItem> collection) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 비어있으면 생성할 수 없다.");
        }
    }

    public boolean isEmpty() {
        return collection.isEmpty();
    }

    public List<OrderLineItem> getCollection() {
        return new ArrayList<>(collection);
    }
}
