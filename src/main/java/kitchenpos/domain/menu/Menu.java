package kitchenpos.domain.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "MENU_GROUP_ID", nullable = false)
    private MenuGroup menuGroup;

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        validate(name, price, menuGroup);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    private void validate(String name, BigDecimal price, MenuGroup menuGroup) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("잘못된 메뉴 이름입니다.");
        }
        if (Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException("잘못된 메뉴 그룹입니다.");
        }
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("잘못된 메뉴 가격입니다.");
        }
    }
}
