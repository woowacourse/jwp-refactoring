package kitchenpos.dto;

import kitchenpos.domain.Product;

public class ProductResponse {

    private long productId;
    private String name;
    private String price;

    public static ProductResponse from(final Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName().getValue(),
                product.getPrice().getValue().toPlainString()
        );
    }

    public ProductResponse(final long productId, final String name, final String price) {
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    public long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}
