package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Long menuGroupId;

    @OneToMany(mappedBy = "menuId")
    private List<MenuProduct> menuProducts;

    public static MenuBuilder builder() {
        return new MenuBuilder();
    }

    public MenuBuilder toBuilder() {
        return new MenuBuilder(id, name, price, menuGroupId, menuProducts);
    }

    public static class MenuBuilder {
        private Long id;
        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private List<MenuProduct> menuProducts;

        public MenuBuilder() {
        }

        public MenuBuilder(Long id, String name, BigDecimal price, Long menuGroupId,
            List<MenuProduct> menuProducts) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.menuGroupId = menuGroupId;
            this.menuProducts = menuProducts;
        }

        public MenuBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MenuBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public MenuBuilder menuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public MenuBuilder menuProducts(List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(id, name, price, menuGroupId, menuProducts);
        }
    }
}
