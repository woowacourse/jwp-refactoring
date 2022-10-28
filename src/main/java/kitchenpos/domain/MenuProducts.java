package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;

@Embeddable
public class MenuProducts {

    @ElementCollection
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
}
