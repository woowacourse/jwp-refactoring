package kitchenpos.domain;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

public class MenuProduct {

    @Id
    private final Long seq;
    private final Product product;
    private final long quantity;

    private MenuProduct(Long seq, Product product, long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public static MenuProductBuilder builder() {
        return new MenuProductBuilder();
    }

    public static final class MenuProductBuilder {
        private Long seq;
        private Product product;
        private long quantity;

        private MenuProductBuilder() {
        }

        public MenuProductBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public MenuProductBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public MenuProductBuilder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            return new MenuProduct(seq, product, quantity);
        }
    }
}
