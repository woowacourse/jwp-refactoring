package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private long quantity;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        validQuantity(quantity);
        this.product = product;
        this.quantity = quantity;
    }

    public void updateMenu(Menu menu) {
        this.menu = menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    private void validQuantity(long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("0 이하의 양을 등록할 수 없습니다.");
        }
    }
}
