package kitchenpos.application.config;

import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@TestConfiguration
public class DaoConfig {

    @Bean
    public ProductDao productDao(final DataSource dataSource) {
        return new JdbcTemplateProductDao(dataSource);
    }

    @Bean
    public MenuDao menuDao(final DataSource dataSource) {
        return new JdbcTemplateMenuDao(dataSource);
    }

    @Bean
    public MenuGroupDao menuGroupDao(final DataSource dataSource) {
        return new JdbcTemplateMenuGroupDao(dataSource);
    }

    @Bean
    public OrderDao orderDao(final DataSource dataSource) {
        return new JdbcTemplateOrderDao(dataSource);
    }

    @Bean
    public OrderLineItemDao orderLineItemDao(final DataSource dataSource) {
        return new JdbcTemplateOrderLineItemDao(dataSource);
    }

    @Bean
    public OrderTableDao orderTableDao(final DataSource dataSource) {
        return new JdbcTemplateOrderTableDao(dataSource);
    }

    @Bean
    public TableGroupDao tableGroupDao(final DataSource dataSource) {
        return new JdbcTemplateTableGroupDao(dataSource);
    }
}
