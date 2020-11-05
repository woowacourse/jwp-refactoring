package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.builder.MenuProductBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private long quantity;

    public static MenuProductBuilder builder() {
        return new MenuProductBuilder();
    }

    public MenuProductBuilder toBuilder() {
        return new MenuProductBuilder(seq, menuId, productId, quantity);
    }
}
