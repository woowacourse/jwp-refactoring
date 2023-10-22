package kitchenpos.domain.order;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Orders {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "orderTableId")
    private List<Order> collection = new ArrayList<>();

    public Orders() {
    }

    public Orders(List<Order> collection) {
        this.collection = collection;
    }

    public boolean inCookingOrMeal() {
        return collection.stream()
                .anyMatch(Order::isCookingOrMeal);
    }

    public void add(Order order) {
        collection.add(order);
    }
}
