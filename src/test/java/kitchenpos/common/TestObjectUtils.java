package kitchenpos.common;

import java.math.BigDecimal;

import org.springframework.test.util.ReflectionTestUtils;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;

public class TestObjectUtils {
    public static Product createProduct(Long id, String name, BigDecimal price) {
        Product product = new Product();
        ReflectionTestUtils.setField(product, "id", id);
        ReflectionTestUtils.setField(product, "name", name);
        ReflectionTestUtils.setField(product, "price", price);

        return product;
    }

    public static MenuGroup createMenuGroup(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        ReflectionTestUtils.setField(menuGroup, "id", id);
        ReflectionTestUtils.setField(menuGroup, "name", name);
        
        return menuGroup;
    }
}
