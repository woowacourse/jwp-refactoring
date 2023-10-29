package kitchenpos.core.domain.order;

import static javax.persistence.CascadeType.PERSIST;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.OneToMany;

public class OrderProducts {

    @OneToMany(mappedBy = "orderMenu", cascade = PERSIST)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    protected OrderProducts() {
    }

    public OrderProducts(final List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
