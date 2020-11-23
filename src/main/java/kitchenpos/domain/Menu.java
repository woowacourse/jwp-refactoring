package kitchenpos.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

    @Embedded
    private Price price;

    @Column(nullable = false)
    private Long menuGroupId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts;

    public static MenuBuilder builder() {
        return new MenuBuilder();
    }

    public void validateCheaperThan(final Price price) {
        if (this.price.compareTo(price) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<Long> extractProductIds() {
        return menuProducts.stream()
            .map(MenuProduct::getProductId)
            .collect(Collectors.toList());
    }

    public Map<Long, Long> extractProductQuantity() {
        return menuProducts.stream()
            .collect(Collectors.toMap(MenuProduct::getProductId, MenuProduct::getQuantity));
    }

    public static class MenuBuilder {
        private Long id;
        private String name;
        private Price price;
        private Long menuGroupId;
        private List<MenuProduct> menuProducts;

        public MenuBuilder() {
        }

        public MenuBuilder(Long id, String name, Price price, Long menuGroupId,
            List<MenuProduct> menuProducts) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.menuGroupId = menuGroupId;
            this.menuProducts = menuProducts;
        }

        public MenuBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public MenuBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public MenuBuilder price(final Price price) {
            this.price = price;
            return this;
        }

        public MenuBuilder menuGroupId(final Long menuGroupId) {
            this.menuGroupId = menuGroupId;
            return this;
        }

        public MenuBuilder menuProducts(final List<MenuProduct> menuProducts) {
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(id, name, price, menuGroupId, menuProducts);
        }
    }
}
