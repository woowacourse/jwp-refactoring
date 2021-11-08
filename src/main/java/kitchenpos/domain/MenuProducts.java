package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> getMenuProducts() {
        return new ArrayList<>(menuProducts);
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    public void checkValidityOfMenuPrice(List<Product> products, BigDecimal menuPrice) {
        final HashMap<Long, Product> repository = new HashMap<>();
        for (Product product1 : products) {
            repository.put(product1.getId(), product1);
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Long productId = menuProduct.getProductId();
            if (!repository.containsKey(productId)) {
                throw new IllegalArgumentException();
            }
            final Product product = repository.get(productId);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (menuPrice.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public void updateMenu(Menu menu) {
        menuProducts.forEach(it -> it.updateMenu(menu));
    }
}
