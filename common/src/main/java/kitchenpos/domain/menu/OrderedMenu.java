package kitchenpos.domain.menu;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class OrderedMenu {

    @Column
    private Long tempMenuId;
    @Column
    private String tempMenuName;
    @Column
    private BigDecimal tempMenuPrice;

    protected OrderedMenu() {
    }

    public OrderedMenu(Long tempMenuId, String tempMenuName, BigDecimal tempMenuPrice) {
        this.tempMenuId = tempMenuId;
        this.tempMenuName = tempMenuName;
        this.tempMenuPrice = tempMenuPrice;
    }

    public Long getTempMenuId() {
        return tempMenuId;
    }

    public String getTempMenuName() {
        return tempMenuName;
    }

    public BigDecimal getTempMenuPrice() {
        return tempMenuPrice;
    }
}
