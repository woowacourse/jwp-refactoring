package kitchenpos.order.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.vo.Price;

@Entity
public class MenuSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @OneToMany
    @JoinColumn(
        name = "menu_snapshot_id",
        updatable = false, nullable = false
    )
    private List<MenuProductSnapshot> menuProductSnapshots = new ArrayList<>();

    protected MenuSnapshot() {
    }

    public MenuSnapshot(
        final String name,
        final Price price,
        final List<MenuProductSnapshot> menuProductSnapshots
    ) {
        this.name = name;
        this.price = price;
        this.menuProductSnapshots = menuProductSnapshots;
    }

    public Long getId() {
        return id;
    }
}
