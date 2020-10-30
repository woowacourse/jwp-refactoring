package kitchenpos.dao;

import static kitchenpos.constants.Constants.TEST_MENU_GROUP_NAME;
import static kitchenpos.constants.Constants.TEST_MENU_NAME;
import static kitchenpos.constants.Constants.TEST_MENU_PRICE;
import static kitchenpos.constants.Constants.TEST_ORDER_ORDERED_TIME;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_EMPTY_FALSE;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_NUMBER_OF_GUESTS;
import static kitchenpos.constants.Constants.TEST_PRODUCT_NAME;
import static kitchenpos.constants.Constants.TEST_PRODUCT_PRICE;
import static kitchenpos.constants.Constants.TEST_TABLE_GROUP_CREATED_DATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class KitchenPosDaoTest {

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    protected Long getCreatedMenuId() {
        Menu menu = Menu.entityOf(TEST_MENU_NAME, TEST_MENU_PRICE, getCreatedMenuGroup(),
            Collections.singletonList(MenuProduct.entityOf(getCreatedProduct(), 1)));

        Menu savedMenu = menuRepository.save(menu);

        Long savedMenuId = savedMenu.getId();
        assertThat(savedMenuId).isNotNull();
        return savedMenuId;
    }

    protected MenuGroup getCreatedMenuGroup() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(TEST_MENU_GROUP_NAME);

        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        assertThat(savedMenuGroup.getId()).isNotNull();
        return savedMenuGroup;
    }

    protected Long getCreatedOrderId() {
        Order order = new Order();
        order.setOrderedTime(TEST_ORDER_ORDERED_TIME);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(getCreatedOrderTableId());

        Order savedOrder = orderRepository.save(order);

        Long savedOrderId = savedOrder.getId();
        assertThat(savedOrderId).isNotNull();
        return savedOrderId;
    }

    protected Long getCreatedOrderTableId() {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(TEST_ORDER_TABLE_NUMBER_OF_GUESTS);
        orderTable.setEmpty(TEST_ORDER_TABLE_EMPTY_FALSE);
        orderTable.setTableGroupId(getCreatedTableGroupId());

        OrderTable savedOrderTable = orderTableRepository.save(orderTable);

        Long savedOrderTableId = savedOrderTable.getId();
        assertThat(savedOrderTableId).isNotNull();
        return savedOrderTableId;
    }

    protected Product getCreatedProduct() {
        Product product = new Product();
        product.setName(TEST_PRODUCT_NAME);
        product.setPrice(TEST_PRODUCT_PRICE);

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct.getId()).isNotNull();
        return savedProduct;
    }

    protected Long getCreatedTableGroupId() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(TEST_TABLE_GROUP_CREATED_DATE);

        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        Long savedTableGroupId = savedTableGroup.getId();
        assertThat(savedTableGroupId).isNotNull();
        return savedTableGroupId;
    }
}
