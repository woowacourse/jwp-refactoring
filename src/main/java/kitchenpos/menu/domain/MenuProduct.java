package kitchenpos.menu.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.exception.NoSuchDataException;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.value.Quantity;

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

    public MenuProduct(
            final Long seq,
            final Long menuId,
            final Product product,
            final Quantity quantity
    ) {
        this.seq = seq;
        this.menuId = menuId;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(final MenuProductDto dto, final List<Product> products) {
        final Product product = products.stream()
                .filter(pr -> pr.getId().equals(dto.getMenuId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchDataException("해당하는 id의 상품이 없습니다."));
        return new MenuProduct(
                dto.getSeq(),
                dto.getMenuId(),
                product,
                new Quantity(dto.getQuantity())
        );
    }

    public BigDecimal calculateTotal() {
        return product.getPrice().multiply(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Product getProduct() {
        return product;
    }

    public Long getProductId() {
        return product.getId();
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
