package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "MENU_GROUP_ID", foreignKey = @ForeignKey(name = "FK_MENU_MENU_GROUP"))
    private MenuGroup menuGroup;

    public Menu() {
    }

    public Menu(String name, Long price, MenuGroup menuGroup) {
        this.name = name;
        this.price = BigDecimal.valueOf(price);
        this.menuGroup = menuGroup;
        validate();
    }

    private void validate() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(String.format("%f : 가격은 0원 이상이어야 합니다.", price));
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }
}
