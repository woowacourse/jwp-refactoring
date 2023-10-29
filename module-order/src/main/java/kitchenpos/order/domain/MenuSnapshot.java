package kitchenpos.order.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class MenuSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long menuId;

    private String name;

    private String menuGroupName;

    private LocalDateTime menuUpdatedAt;

    @Embedded
    private Price price;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "menu_snapshot_id", updatable = false, nullable = false)
    private List<MenuProductSnapshot> menuProductSnapshots;

    protected MenuSnapshot() {
    }

    public MenuSnapshot(
            final Long menuId,
            final String name,
            final String menuGroupName,
            final Price price,
            final LocalDateTime menuUpdatedAt,
            final List<MenuProductSnapshot> menuProductSnapshots
    ) {
        this.menuId = menuId;
        this.name = name;
        this.menuGroupName = menuGroupName;
        this.price = price;
        this.menuUpdatedAt = menuUpdatedAt;
        this.menuProductSnapshots = menuProductSnapshots;
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public String getMenuGroupName() {
        return menuGroupName;
    }

    public Price getPrice() {
        return price;
    }

    public List<MenuProductSnapshot> getMenuItemSnapshots() {
        return menuProductSnapshots;
    }
}
