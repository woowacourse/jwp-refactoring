package kitchenpos.domain.order;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.ProductName;

@Entity
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_menu_id", foreignKey = @ForeignKey(name = "fk_order_product_to_order_menu"))
    private OrderMenu orderMenu;
    @Column(name = "product_id", nullable = false)
    private long productId;
    @Column(nullable = false)
    private ProductName productName;
    @Embedded
    private Price price;
    @Column(nullable = false)
    private long quantity;

    protected OrderProduct() {
    }

    public OrderProduct(final OrderMenu orderMenu,
                        final long productId,
                        final ProductName productName,
                        final Price price,
                        final long quantity) {
        this.orderMenu = orderMenu;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }
}
