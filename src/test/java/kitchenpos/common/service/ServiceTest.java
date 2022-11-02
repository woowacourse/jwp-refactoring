package kitchenpos.common.service;

import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.ordertable.repository.TableRepository;
import kitchenpos.product.resitory.ProductRepository;
import kitchenpos.support.DatabaseCleaner;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    protected TableRepository tableRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuProductRepository menuProductRepository;

    @Autowired
    protected OrderValidator orderValidator;

    @BeforeEach
    void cleanUp() {
        databaseCleaner.clear();
    }
}
