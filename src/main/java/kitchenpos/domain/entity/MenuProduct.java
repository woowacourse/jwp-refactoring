package kitchenpos.domain.entity;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.value.Quantity;
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

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long seq, Long menu, Long product, Quantity quantity) {
        this.seq = seq;
        this.menu = Menu.builder().id(menu).build();
        this.product = new Product(product);
        this.quantity = quantity;
    }

    public static MenuProduct from(final MenuProductDto dto) {
        return new MenuProduct(
                dto.getSeq(),
                dto.getMenuId(),
                dto.getProductId(),
                new Quantity(dto.getQuantity())
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

    public Quantity getQuantity() {
        return quantity;
    }
}
