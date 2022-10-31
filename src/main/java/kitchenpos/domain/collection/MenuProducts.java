package kitchenpos.domain.collection;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.entity.MenuProduct;
import kitchenpos.domain.entity.Product;

public class MenuProducts {

    private List<MenuProduct> elements;

    public MenuProducts(List<MenuProduct> elements) {
        this.elements = elements;
    }

    public void add(MenuProduct menuProduct) {
        elements.add(menuProduct);
    }

    public List<Long> getProductIds() {
        return elements.stream()
                .map(MenuProduct::getProduct)
                .map(Product::getId)
                .collect(Collectors.toList());
    }

    public long sumPrice(List<Product> products) {
        long sum = 0;
        for (Product product : products) {
            MenuProduct menuProduct = findMenuProduct(product);
            sum += (product.getPrice() * menuProduct.getQuantity());
        }
        return sum;
    }

    private MenuProduct findMenuProduct(Product product) {
        for (MenuProduct menuProduct : elements) {
            if (menuProduct.isRelatedTo(product)) {
                return menuProduct;
            }
        }
        throw new IllegalArgumentException();
    }

    public List<MenuProduct> getElements() {
        return elements;
    }
}
