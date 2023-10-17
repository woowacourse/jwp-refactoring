package kitchenpos.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.dto.request.menu.MenuProductDto;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long seq, Long menu, Long product, long quantity) {
        this.seq = seq;
        this.menu = Menu.builder().id(menu).build();//menu;
        this.product = new Product(product);//product;
        this.quantity = quantity;
    }

    public static MenuProduct from(final MenuProductDto dto) {
        return new MenuProduct(
                dto.getSeq(),
                dto.getMenuId(),
                dto.getProductId(),
                dto.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenu() {
        return menu.getId();
    }

    public Long getProduct() {
        return product.getId();
    }

    public long getQuantity() {
        return quantity;
    }
}
