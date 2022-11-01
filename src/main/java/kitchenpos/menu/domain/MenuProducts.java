package kitchenpos.menu.domain;

import static javax.persistence.FetchType.EAGER;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;

@Embeddable
public class MenuProducts {

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "menu_product", joinColumns = @JoinColumn(name = "menu_id"))
    private List<RelatedProduct> relatedProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(final List<RelatedProduct> relatedProducts) {
        this.relatedProducts = relatedProducts;
    }

    public List<RelatedProduct> getRelatedProducts() {
        return relatedProducts;
    }

    public BigDecimal getProductsSumPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (RelatedProduct relatedProduct : relatedProducts) {
            final BigDecimal price = relatedProduct.getProduct().getPrice();
            final BigDecimal quantity = BigDecimal.valueOf(relatedProduct.getQuantity());

            sum = sum.add(price.multiply(quantity));
        }

        return sum;
    }
}
