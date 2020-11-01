package kitchenpos.application;

import static kitchenpos.constants.Constants.TEST_MENU_GROUP_NAME;
import static kitchenpos.constants.Constants.TEST_MENU_NAME;
import static kitchenpos.constants.Constants.TEST_MENU_PRODUCT_QUANTITY;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_EMPTY_FALSE;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_EMPTY_TRUE;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_NUMBER_OF_GUESTS;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_NUMBER_OF_GUESTS_EMPTY;
import static kitchenpos.constants.Constants.TEST_PRODUCT_NAME;
import static kitchenpos.constants.Constants.TEST_PRODUCT_PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.dao.TableRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuGroupRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;
import kitchenpos.ui.dto.TableCreateRequest;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class KitchenPosServiceTest {

    @Autowired
    protected TableService tableService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected TableRepository tableRepository;

    @Autowired
    protected OrderRepository orderRepository;

    protected void setCreatedTableGroup(List<Long> tableIds) {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(tableIds);
        tableGroupService.create(tableGroupRequest);
    }

    protected long getCreatedEmptyOrderTableId() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(
            TEST_ORDER_TABLE_NUMBER_OF_GUESTS_EMPTY, TEST_ORDER_TABLE_EMPTY_TRUE);
        TableResponse createdTable = tableService.create(tableCreateRequest);

        Long createdOrderTableId = createdTable.getId();
        assertThat(createdTable).isNotNull();
        return createdOrderTableId;
    }

    protected long getCreatedNotEmptyOrderTableId() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(
            TEST_ORDER_TABLE_NUMBER_OF_GUESTS, TEST_ORDER_TABLE_EMPTY_FALSE);
        TableResponse createdTable = tableService.create(tableCreateRequest);

        Long createdOrderTableId = createdTable.getId();
        assertThat(createdTable).isNotNull();
        return createdOrderTableId;
    }

    protected long getCreatedMenuGroupId() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(TEST_MENU_GROUP_NAME);
        MenuGroupResponse createdMenuGroup = menuGroupService.create(menuGroupRequest);

        Long createdMenuGroupId = createdMenuGroup.getId();
        assertThat(createdMenuGroupId).isNotNull();
        return createdMenuGroupId;
    }

    protected Product getCreatedProduct() {
        Product product = Product.entityOf(TEST_PRODUCT_NAME, TEST_PRODUCT_PRICE);
        Product createdProduct = productService.create(product);

        assertThat(createdProduct.getId()).isNotNull();
        return createdProduct;
    }

    protected long getCreatedMenuId() {
        MenuProduct menuProduct = getMenuProduct();

        MenuRequest menuRequest = new MenuRequest(TEST_MENU_NAME, getMenuProductPrice(menuProduct),
            getCreatedMenuGroupId(), Collections.singletonList(
            new MenuProductRequest(menuProduct.getProduct().getId(), menuProduct.getQuantity())));

        MenuResponse createdMenu = menuService.create(menuRequest);

        Long createdMenuId = createdMenu.getId();
        assertThat(createdMenuId).isNotNull();
        return createdMenuId;
    }

    protected BigDecimal getMenuProductPrice(MenuProduct menuProduct) {
        BigDecimal productPrice = productRepository.findById(menuProduct.getProduct().getId())
            .orElseThrow(() -> new IllegalArgumentException(
                menuProduct.getProduct().getId() + "ID에 해당하는 Product가 없습니다."))
            .getPrice();
        BigDecimal quantity = BigDecimal.valueOf(menuProduct.getQuantity());
        return productPrice.multiply(quantity);
    }

    protected MenuProduct getMenuProduct() {
        return MenuProduct.entityOf(getCreatedProduct(), TEST_MENU_PRODUCT_QUANTITY);
    }
}
