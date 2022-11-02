package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "order_menu")
public class OrderMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private BigDecimal menuPrice;

    @Column(name = "menu_group_name")
    private String menuGroupName;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_menu_id")
    private List<OrderMenuProduct> orderMenuProducts = new ArrayList<>();

    protected OrderMenu() {
    }

    public OrderMenu(final String menuName, final BigDecimal menuPrice, final String menuGroupName) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuGroupName = menuGroupName;
    }

    public OrderMenu(final String menuName, final BigDecimal menuPrice, final String menuGroupName,
                     final List<OrderMenuProduct> orderMenuProducts) {
        this.menuName = menuName;
        this.menuPrice = menuPrice;
        this.menuGroupName = menuGroupName;
        this.orderMenuProducts = orderMenuProducts;
    }

    public Long getId() {
        return id;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public String getMenuGroupName() {
        return menuGroupName;
    }

    public List<OrderMenuProduct> getOrderMenuProducts() {
        return orderMenuProducts;
    }
}
