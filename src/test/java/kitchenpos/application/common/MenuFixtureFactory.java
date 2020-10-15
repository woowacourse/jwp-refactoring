package kitchenpos.application.common;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.CreateMenuRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class MenuFixtureFactory {
    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    protected CreateMenuRequest makeCreateMenuRequest(String menuGroupName, String productName, int productPrice) {
        MenuGroup savedMenuGroup = menuGroupDao.save(TestObjectFactory.createMenuGroupDto(menuGroupName));
        Product savedProduct = productDao.save(TestObjectFactory.createProductDto(productName, productPrice));

        List<MenuProduct> menuProducts = Arrays.asList(TestObjectFactory.createMenuProduct(savedProduct.getId(), 2));
        return new CreateMenuRequest(menuGroupName + " " + menuGroupName,
                BigDecimal.valueOf(productPrice * 2),
                savedMenuGroup.getId(),
                menuProducts);
    }

    protected Menu makeMenuToSave(String menuGroupName, String productName, int productPrice) {
        CreateMenuRequest createMenuRequest = makeCreateMenuRequest(menuGroupName, productName, productPrice);
        MenuGroup menuGroup = menuGroupDao.findById(createMenuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        return createMenuRequest.toMenu(menuGroup);
    }
}
