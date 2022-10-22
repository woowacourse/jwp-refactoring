package kitchenpos;

import javax.sql.DataSource;
import kitchenpos.application.ProductService;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.dao.ProductDao;

public class BeanAssembler {

    public static ProductService createProductService(DataSource dataSource) {
        return new ProductService(createProductDao(dataSource));
    }

    private static ProductDao createProductDao(DataSource dataSource) {
        return new JdbcTemplateProductDao(dataSource);
    }
}
