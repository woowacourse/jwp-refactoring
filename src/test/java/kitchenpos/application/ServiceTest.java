package kitchenpos.application;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.config.DataSourceConfig;

@Sql("/truncate.sql")
@Import(DataSourceConfig.class)
public class ServiceTest {
}

