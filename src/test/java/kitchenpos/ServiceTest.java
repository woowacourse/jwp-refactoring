package kitchenpos;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest
@Transactional
public abstract class ServiceTest {

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected OrderTableDao tableDao;

    @Autowired
    protected TableGroupDao tableGroupDao;
}
