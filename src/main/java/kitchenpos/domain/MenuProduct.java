package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private long quantity;

    public MenuProduct() {
    }

    private MenuProduct (MenuProductBuilder menuProductBuilder) {
        this.seq = menuProductBuilder.seq;
        this.menu = menuProductBuilder.menu;
        this.product = menuProductBuilder.product;
        this.quantity = menuProductBuilder.quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }

    public static class MenuProductBuilder {
        private Long seq;
        private Menu menu;
        private Product product;
        private long quantity;

        public MenuProductBuilder setSeq(Long seq) {
            this.seq = seq;
            return this;
        }

        public MenuProductBuilder setMenu(Menu menu) {
            this.menu = menu;
            return this;
        }

        public MenuProductBuilder setProduct(Product product) {
            this.product = product;
            return this;
        }

        public MenuProductBuilder setQuantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            return new MenuProduct(this);
        }
    }
}
