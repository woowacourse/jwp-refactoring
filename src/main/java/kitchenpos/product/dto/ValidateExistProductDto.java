package kitchenpos.product.dto;

public class ValidateExistProductDto {

    private Long productId;

    public ValidateExistProductDto(final Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
