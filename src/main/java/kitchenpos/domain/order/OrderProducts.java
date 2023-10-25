package kitchenpos.domain.order;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;

public class OrderProducts {

    @OneToMany(mappedBy = "orderMenu", cascade = CascadeType.PERSIST)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    protected OrderProducts() {
    }

    public OrderProducts(final List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
