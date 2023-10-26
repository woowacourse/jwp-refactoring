package kitchenpos.domain.menu;

import org.springframework.data.annotation.Id;

import java.math.BigDecimal;

public class MenuProduct {

    @Id
    private final Long seq;
    private final Long productId;
    private final long quantity;
    private final String name;
    private final BigDecimal price;

    private MenuProduct(Long seq, Long productId, long quantity, String name, BigDecimal price) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
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

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public static MenuProductBuilder builder() {
        return new MenuProductBuilder();
    }

    public static final class MenuProductBuilder {
        private Long seq;
        private Long productId;
        private long quantity;
        private String name;
        private BigDecimal price;

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

        public MenuProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuProductBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public MenuProduct build() {
            return new MenuProduct(seq, productId, quantity, name, price);
        }
    }
}
