package kitchenpos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableGroupDao;

@SpringBootTest
@Sql("classpath:truncate.sql")
public class ServiceTest {

    @Autowired
    protected MenuService menuService;

    @Autowired
    protected MenuGroupService menuGroupService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected OrderDao orderDao;

    @Autowired
    protected TableService tableService;

    @Autowired
    protected TableGroupService tableGroupService;

    @Autowired
    protected TableGroupDao tableGroupDao;
}
