package kitchenpos.domain.menu;

import org.springframework.data.annotation.Id;

public class MenuProduct {

    @Id
    private final Long seq;
    private final Long productId;
    private final long quantity;

    private MenuProduct(Long seq, Long productId, long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
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

    public static MenuProductBuilder builder() {
        return new MenuProductBuilder();
    }

    public static final class MenuProductBuilder {
        private Long seq;
        private Long productId;
        private long quantity;

        private MenuProductBuilder() {
        }

        public MenuProductBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public MenuProductBuilder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public MenuProductBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            return new MenuProduct(seq, productId, quantity);
        }
    }
}
