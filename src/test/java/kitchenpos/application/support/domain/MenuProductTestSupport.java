package kitchenpos.application.support.domain;

import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.domain.MenuProduct;

public class MenuProductTestSupport {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private static Long autoCount = 0L;

        private Long seq = ++autoCount;
        private Long menuId = null;
        private Long productId = ProductTestSupport.builder().build().getId();
        private long quantity = 2;

        public Builder seq(final Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder menuId(final Long menuId) {
            this.menuId = menuId;
            return this;
        }

        public Builder productId(final Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(final long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            final var result = new MenuProduct();
            result.setSeq(seq);
            result.setMenuId(menuId);
            result.setProductId(productId);
            result.setQuantity(quantity);
            return result;
        }
    }
}
