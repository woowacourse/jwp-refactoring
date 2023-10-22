package kitchenpos.support;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewTestSupporter {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    public Product createProduct() {
        final Product product = new Product("name", 10_000);
        return productRepository.save(product);
    }

    public MenuGroup createMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup("name");
        return menuGroupRepository.save(menuGroup);
    }
}
