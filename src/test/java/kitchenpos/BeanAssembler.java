package kitchenpos;

import javax.sql.DataSource;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableService;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderLineItemDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.dao.JdbcTemplateTableGroupDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;

public class BeanAssembler {

    public static ProductService createProductService(DataSource dataSource) {
        return new ProductService(createProductDao(dataSource));
    }

    public static MenuGroupService createMenuGroupService(DataSource dataSource) {
        return new MenuGroupService(createMenuGroupDao(dataSource));
    }

    public static TableService createTableService(DataSource dataSource) {
        return new TableService(createOrderDao(dataSource), createOrderTableDao(dataSource));
    }

    public static ProductDao createProductDao(DataSource dataSource) {
        return new JdbcTemplateProductDao(dataSource);
    }

    public static MenuGroupDao createMenuGroupDao(DataSource dataSource) {
        return new JdbcTemplateMenuGroupDao(dataSource);
    }

    public static MenuDao createMenuDao(DataSource dataSource) {
        return new JdbcTemplateMenuDao(dataSource);
    }

    public static MenuProductDao createMenuProductDao(DataSource dataSource) {
        return new JdbcTemplateMenuProductDao(dataSource);
    }

    public static OrderDao createOrderDao(DataSource dataSource) {
        return new JdbcTemplateOrderDao(dataSource);
    }

    public static OrderLineItemDao createOrderLineItemDao(DataSource dataSource) {
        return new JdbcTemplateOrderLineItemDao(dataSource);
    }

    public static OrderTableDao createOrderTableDao(DataSource dataSource) {
        return new JdbcTemplateOrderTableDao(dataSource);
    }

    public static TableGroupDao createTableGroupDao(DataSource dataSource) {
        return new JdbcTemplateTableGroupDao(dataSource);
    }
}
