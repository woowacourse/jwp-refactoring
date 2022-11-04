package kitchenpos.domain.order;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class OrderedMenu {

    private Long menuId;

    private String menuName;

    private BigDecimal menuPrice; // TODO: Price로 통일 필요 (현재는 price 필드 이름때문에 BigDecimal로 설정)

    protected OrderedMenu() {
    }

    public OrderedMenu(final Long menuId, final String menuName, final BigDecimal menuPrice) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderedMenu that = (OrderedMenu) o;
        return Objects.equals(menuId, that.menuId) && Objects.equals(menuName, that.menuName)
                && Objects.equals(menuPrice, that.menuPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, menuName, menuPrice);
    }
}
