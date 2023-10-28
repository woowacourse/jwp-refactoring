package kitchenpos.menugroup.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.menugroup.application.dto.vo.MenuGroupName;

@Entity
@Table(name = "menu_group")
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    private MenuGroupName name;

    protected MenuGroup() {
    }

    private MenuGroup(String name) {
        this.name = new MenuGroupName(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public static MenuGroupBuilder builder() {
        return new MenuGroupBuilder();
    }

    public static class MenuGroupBuilder {

        private String name;

        public MenuGroupBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuGroup build() {
            return new MenuGroup(name);
        }
    }
}
