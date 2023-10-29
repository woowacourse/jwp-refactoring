package kitchenpos.core.domain.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.core.domain.product.Product;
import kitchenpos.core.domain.vo.MenuName;
import kitchenpos.core.domain.vo.Price;
import kitchenpos.core.domain.vo.ProductName;

@Entity
public class OrderMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "menu_id", nullable = false)
    private long menuId;
    @Embedded
    private MenuName name;
    @Embedded
    private Price price;
    @Embedded
    private OrderProducts orderProducts = new OrderProducts();

    protected OrderMenu() {
    }

    public OrderMenu(final long menuId,
                     final String name,
                     final BigDecimal price,
                     final Map<Product, Long> quantityByProduct) {
        this.menuId = menuId;
        this.name = MenuName.from(name);
        this.price = Price.from(price);
        this.orderProducts = new OrderProducts(createOrderProducts(quantityByProduct));
    }

    private List<OrderProduct> createOrderProducts(final Map<Product, Long> quantityByProduct) {
        return quantityByProduct.keySet()
                .stream()
                .map(product -> new OrderProduct(
                        this,
                        product.getId(),
                        ProductName.from(product.getName()),
                        product.getPrice(),
                        quantityByProduct.get(product)))
                .collect(Collectors.toList());
    }

    public long getMenuId() {
        return menuId;
    }
}
