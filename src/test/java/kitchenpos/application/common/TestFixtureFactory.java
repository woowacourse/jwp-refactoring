package kitchenpos.application.common;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductPrice;
import kitchenpos.dto.menu.MenuProductDto;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.order.OrderLineItemDto;
import kitchenpos.dto.order.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class TestFixtureFactory {
    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected MenuRepository menuRepository;

    protected Menu makeSavedMenu(
            String menuName,
            int menuPrice,
            String menuGroupName,
            String productName,
            int productPrice,
            long productQuantity
    ) {
        MenuRequest menuRequest = makeMenuCreateRequest(menuName, menuPrice, menuGroupName, productName, productPrice, productQuantity);
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);
        Menu menuToSave = menuRequest.toMenu(menuGroup, BigDecimal.valueOf(productPrice * productQuantity));
        return menuRepository.save(menuToSave);
    }

    protected MenuRequest makeMenuCreateRequest(
            String menuName,
            int menuPrice,
            String menuGroupName,
            String productName,
            int productPrice,
            long productQuantity
    ) {
        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(menuGroupName));
        Product savedProduct = productRepository.save(new Product(productName, ProductPrice.of(productPrice)));

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

    protected OrderRequest makeOrderCreateRequest(OrderTable savedOrderTable) {
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
        return orderTableRepository.save(new OrderTable(numberOfGuests, isEmpty));
    }
}
