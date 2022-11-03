package kitchenpos.domain.menu;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Embedded
    private Price price;

    @Column
    private Long menuId;

    @Column
    private Long menuGroupId;

    protected MenuHistory() {
    }

    public MenuHistory(Long id, String name, Price price, Long menuId, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuId = menuId;
        this.menuGroupId = menuGroupId;
    }

    public static MenuHistory of(Menu menu) {
        return new MenuHistory(null, menu.getName(), menu.getPrice(), menu.getId(), menu.getMenuGroupId());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}
