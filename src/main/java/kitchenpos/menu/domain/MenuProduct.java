package kitchenpos.menu.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.value.Quantity;
import kitchenpos.menu.dto.MenuProductDto;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private Long menuId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long seq, Long menuId, Long product, Quantity quantity) {
        this.seq = seq;
        this.menuId = menuId;
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

    public Long getMenuId() {
        return menuId;
    }

    public Long getProduct() {
        return product.getId();
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
