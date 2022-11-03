package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column
    private Long menuGroupId;

    @Column
    private LocalDateTime createdTime;

    protected MenuHistory() {
    }

    public MenuHistory(Long id, String name, Price price, Menu menu, Long menuGroupId, LocalDateTime localDateTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menu = menu;
        this.menuGroupId = menuGroupId;
        this.createdTime = localDateTime;
    }

    public static MenuHistory of(Menu menu) {
        return new MenuHistory(null, menu.getName(), menu.getPrice(), menu, menu.getMenuGroupId(), LocalDateTime.now());
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
}
