package kitchenpos.domain.order;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import kitchenpos.support.money.Money;

@Embeddable
public class OrderMenu {

    private String name;
    private Money price;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "order_menu_product", joinColumns = @JoinColumn)
    private List<OrderMenuProduct> orderMenuProducts = new ArrayList<>();

    protected OrderMenu() {
    }

    public OrderMenu(String name, Money price, List<OrderMenuProduct> orderMenuProducts) {
        this.name = name;
        this.price = price;
        this.orderMenuProducts.addAll(new ArrayList<>(orderMenuProducts));
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderMenuProduct> getOrderMenuProducts() {
        return orderMenuProducts;
    }
}
