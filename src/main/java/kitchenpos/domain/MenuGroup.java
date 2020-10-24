package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "menuGroup")
    private List<Menu> menus = new ArrayList<>();

    @Builder
    public MenuGroup(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public void addMenu(final Menu menu) {
        menus.add(menu);
    }
}
