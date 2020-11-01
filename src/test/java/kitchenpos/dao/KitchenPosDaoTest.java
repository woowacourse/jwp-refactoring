package kitchenpos.dao;

import static kitchenpos.constants.Constants.TEST_MENU_GROUP_NAME;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_EMPTY_FALSE;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_NUMBER_OF_GUESTS;
import static kitchenpos.constants.Constants.TEST_PRODUCT_NAME;
import static kitchenpos.constants.Constants.TEST_PRODUCT_PRICE;
import static kitchenpos.constants.Constants.TEST_TABLE_GROUP_CREATED_DATE;
import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.domain.Table;
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
    protected TableRepository tableRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    protected MenuGroup getCreatedMenuGroup() {
        MenuGroup menuGroup = MenuGroup.entityOf(TEST_MENU_GROUP_NAME);
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);
        assertThat(savedMenuGroup.getId()).isNotNull();
        return savedMenuGroup;
    }

    protected Table getCreatedTable() {
        Table table = Table
            .entityOf(TEST_ORDER_TABLE_NUMBER_OF_GUESTS, TEST_ORDER_TABLE_EMPTY_FALSE);
        table.setTableGroup(getCreatedTableGroup());

        Table savedTable = tableRepository.save(table);

        assertThat(savedTable.getId()).isNotNull();
        return savedTable;
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

    protected TableGroup getCreatedTableGroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(TEST_TABLE_GROUP_CREATED_DATE);

        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        assertThat(savedTableGroup.getId()).isNotNull();
        return savedTableGroup;
    }
}
