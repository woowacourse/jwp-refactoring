package kitchenpos.application;

import kitchenpos.fixture.ServiceDependencies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("classpath:truncate.sql")
@Import(ServiceDependencies.class)
abstract class ServiceTestEnvironment {

    @Autowired
    protected ServiceDependencies serviceDependencies;
}
