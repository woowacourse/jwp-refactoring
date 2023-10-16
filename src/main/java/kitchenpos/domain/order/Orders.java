package kitchenpos.domain.order;

import java.util.List;

public class Orders {

    private final List<Order> collection;

    public Orders(List<Order> collection) {
        this.collection = collection;
    }


    public boolean inCookingOrMeal() {
        return collection.stream()
                .anyMatch(Order::isCookingOrMeal);
    }
}
