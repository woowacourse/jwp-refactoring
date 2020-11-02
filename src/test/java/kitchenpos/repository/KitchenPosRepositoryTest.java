package kitchenpos.repository;

import static kitchenpos.constants.Constants.TEST_MENU_GROUP_NAME;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_EMPTY_TRUE;
import static kitchenpos.constants.Constants.TEST_ORDER_TABLE_NUMBER_OF_GUESTS;
import static kitchenpos.constants.Constants.TEST_PRODUCT_NAME;
import static kitchenpos.constants.Constants.TEST_PRODUCT_PRICE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.table.Table;
import kitchenpos.domain.tablegroup.TableGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public abstract class KitchenPosRepositoryTest {

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
            .entityOf(TEST_ORDER_TABLE_NUMBER_OF_GUESTS, TEST_ORDER_TABLE_EMPTY_TRUE);
        Table savedTable = tableRepository.save(table);

        getCreatedTableGroup(Collections.singletonList(savedTable));
        assertThat(savedTable.getId()).isNotNull();
        return savedTable;
    }

    protected Product getCreatedProduct() {
        Product product = Product.entityOf(TEST_PRODUCT_NAME, TEST_PRODUCT_PRICE);
        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct.getId()).isNotNull();
        return savedProduct;
    }

    protected TableGroup getCreatedTableGroup(List<Table> tables) {
        TableGroup tableGroup = TableGroup.entityOf(tables);

        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        assertThat(savedTableGroup.getId()).isNotNull();
        return savedTableGroup;
    }
}
