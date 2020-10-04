package kitchenpos.application;

import static kitchenpos.constants.Constants.TEST_MENU_GROUP_NAME;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_EMPTY_FALSE;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_EMPTY_TRUE;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_NUMBER_OF_GUESTS;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_NUMBER_OF_GUESTS_EMPTY;
import static kitchenpos.constants.Constants.TEST_PRODUCT_NAME;
import static kitchenpos.constants.Constants.TEST_PRODUCT_PRICE;
import static kitchenpos.constants.Constants.TEST_TABLE_GROUP_CREATED_DATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class KitchenPosServiceTest {

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected ProductService productService;

    protected void setCreatedTableGroup(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(TEST_TABLE_GROUP_CREATED_DATE);
        tableGroup.setOrderTables(orderTables);

        tableGroupService.create(tableGroup);
    }

    protected long getCreatedEmptyOrderTableId() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_TRUE);
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS_EMPTY);
        OrderTable createdOrderTable = tableService.create(orderTable);

        Long createdOrderTableId = createdOrderTable.getId();
        assertThat(createdOrderTable).isNotNull();
        return createdOrderTableId;
    }

    protected long getCreatedNotEmptyOrderTableId() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        OrderTable createdOrderTable = tableService.create(orderTable);

        Long createdOrderTableId = createdOrderTable.getId();
        assertThat(createdOrderTable).isNotNull();
        return createdOrderTableId;
    }

    protected long getCreatedMenuGroupId() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(TEST_MENU_GROUP_NAME);
        MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        Long createdMenuGroupId = createdMenuGroup.getId();
        assertThat(createdMenuGroupId).isNotNull();
        return createdMenuGroupId;
    }

    protected long getCreatedProductId() {
        Product product = new Product();
        product.setName(TEST_PRODUCT_NAME);
        product.setPrice(TEST_PRODUCT_PRICE);
        Product createdProduct = productService.create(product);

        Long createdProductId = createdProduct.getId();
        assertThat(createdProductId).isNotNull();
        return createdProductId;
    }
}
