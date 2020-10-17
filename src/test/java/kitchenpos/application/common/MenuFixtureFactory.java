package kitchenpos.application.common;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.MenuProductDto;
import kitchenpos.dto.menu.menuCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class MenuFixtureFactory {
    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected ProductDao productDao;

    protected Menu makeMenuToSave(String menuGroupName, String productName, int productPrice) {
        menuCreateRequest menuCreateRequest = makeMenuCreateRequest(menuGroupName, productName, productPrice);
        MenuGroup menuGroup = menuGroupDao.findById(menuCreateRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        return new Menu(menuCreateRequest.getName(),
                menuCreateRequest.getPrice(),
                menuGroup);
    }

    protected menuCreateRequest makeMenuCreateRequest(String menuGroupName, String productName, int productPrice) {
        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup(null, menuGroupName));
        Product savedProduct = productDao.save(new Product(productName, BigDecimal.valueOf(productPrice)));

        List<MenuProductDto> menuProductDtos = Arrays.asList(new MenuProductDto(savedProduct.getId(), 2L));

        return new menuCreateRequest(productName + "+" + productName,
                BigDecimal.valueOf(productPrice * 2),
                savedMenuGroup.getId(),
                menuProductDtos);
    }
}
