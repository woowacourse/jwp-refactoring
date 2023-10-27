package kitchenpos.menu.application.dto.response;

import java.util.Objects;

public class MenuProductResponse {
    private Long seq;
    private Long productId;
    private long quantity;

    public MenuProductResponse() {
    }

    public MenuProductResponse(final Long seq, final Long productId, final long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuProductResponse that = (MenuProductResponse) o;
        return getQuantity() == that.getQuantity() && Objects.equals(getSeq(), that.getSeq()) && Objects.equals(getProductId(), that.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSeq(), getProductId(), getQuantity());
    }

    public Long getSeq() {
        return seq;
    }


    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
