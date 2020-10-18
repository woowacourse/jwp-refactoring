package kitchenpos.application.common;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.MenuProductDto;
import kitchenpos.dto.menu.menuCreateRequest;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestFixtureFactory {
    @Autowired
    protected MenuGroupDao menuGroupDao;

    @Autowired
    protected ProductDao productDao;

    @Autowired
    protected OrderTableDao orderTableDao;

    @Autowired
    protected MenuDao menuDao;

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

    protected OrderCreateRequest makeOrderCreateRequest() {
        OrderTable savedOrderTable = orderTableDao.save(new OrderTable(0, false));

        Menu savedMenu1 = menuDao.save(makeMenuToSave("추천메뉴", "양념", 12000));
        Menu savedMenu2 = menuDao.save(makeMenuToSave("추천메뉴", "후라이드", 11000));
        List<OrderLineItemDto> orderLineItems = Arrays.asList(
                new OrderLineItemDto(savedMenu1.getId(), 1),
                new OrderLineItemDto(savedMenu2.getId(), 1)
        );

        return new OrderCreateRequest(savedOrderTable.getId(), orderLineItems);
    }
}
