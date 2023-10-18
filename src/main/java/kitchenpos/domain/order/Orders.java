package kitchenpos.domain.order;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class Orders {

    @OneToMany
    private List<Order> collection;

    public Orders() {
    }

    public Orders(List<Order> collection) {
        this.collection = collection;
    }

    public boolean inCookingOrMeal() {
        return collection.stream()
                .anyMatch(Order::isCookingOrMeal);
    }
}
