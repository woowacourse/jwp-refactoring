package kitchenpos.support;

import java.math.BigDecimal;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DataSupport {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private MenuGroupDao menuGroupDao;

    public Product saveProduct(final String name, final BigDecimal price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return productDao.save(product);
    }

    public MenuGroup saveMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroupDao.save(menuGroup);
    }
}
