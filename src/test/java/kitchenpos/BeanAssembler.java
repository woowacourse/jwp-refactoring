package kitchenpos;

import javax.sql.DataSource;
import kitchenpos.application.ProductService;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.ProductDao;

public class BeanAssembler {

    public static ProductService createProductService(DataSource dataSource) {
        return new ProductService(createProductDao(dataSource));
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
}
