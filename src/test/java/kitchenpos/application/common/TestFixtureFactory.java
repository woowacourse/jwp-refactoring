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
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.order.OrderLineItemDto;
import kitchenpos.dto.order.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    protected Menu makeSavedMenu(
            String menuName,
            int menuPrice,
            String menuGroupName,
            String productName,
            int productPrice,
            long productQuantity
    ) {
        MenuRequest menuRequest = makeMenuCreateRequest(menuName, menuPrice, menuGroupName, productName, productPrice, productQuantity);
        MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);
        Menu menuToSave = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup, new ArrayList<>());
        return menuDao.save(menuToSave);
    }

    protected MenuRequest makeMenuCreateRequest(
            String menuName,
            int menuPrice,
            String menuGroupName,
            String productName,
            int productPrice,
            long productQuantity
    ) {
        MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup(menuGroupName));
        Product savedProduct = productDao.save(new Product(productName, BigDecimal.valueOf(productPrice)));

        List<MenuProductDto> menuProductDtos = Arrays.asList(new MenuProductDto(savedProduct.getId(), productQuantity));

        return new MenuRequest(menuName,
                BigDecimal.valueOf(menuPrice),
                savedMenuGroup.getId(),
                menuProductDtos);
    }

    protected OrderRequest makeOrderCreateRequest() {
        OrderTable savedOrderTable = makeSavedOrderTable(0, false);
        Menu savedMenu1 = makeSavedMenu(
                "양념2마리",
                20000,
                "추천메뉴",
                "양념",
                11000,
                2
        );
        Menu savedMenu2 = makeSavedMenu(
                "후라이드2마리",
                20000,
                "추천메뉴",
                "후라이드",
                11000,
                2
        );
        List<OrderLineItemDto> orderLineItems = Arrays.asList(
                new OrderLineItemDto(savedMenu1.getId(), 1),
                new OrderLineItemDto(savedMenu2.getId(), 1)
        );
        return new OrderRequest(savedOrderTable.getId(), null, orderLineItems);
    }

    protected OrderTable makeSavedOrderTable(int numberOfGuests, boolean isEmpty) {
        return orderTableDao.save(new OrderTable(numberOfGuests, isEmpty));
    }
}
