package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "menu_id")
    private Long menuId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private long quantity;

    public static MenuProductBuilder builder() {
        return new MenuProductBuilder();
    }

    public static class MenuProductBuilder {
        private Long seq;
        private Long menuId;
        private Long productId;
        private long quantity;

        public MenuProductBuilder() {
        }

        public MenuProductBuilder(Long seq, Long menuId, Long productId, long quantity) {
            this.seq = seq;
            this.menuId = menuId;
            this.productId = productId;
            this.quantity = quantity;
        }

        public MenuProductBuilder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public MenuProductBuilder menuId(Long menuId) {
            this.menuId = menuId;
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
            return new MenuProduct(seq, menuId, productId, quantity);
        }
    }
}
