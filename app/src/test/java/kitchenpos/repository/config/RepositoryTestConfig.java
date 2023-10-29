package kitchenpos.repository.config;

import kitchenpos.config.JpaConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

@Import(JpaConfig.class)
@DataJpaTest
public class RepositoryTestConfig {

    @Autowired
    protected EntityManager em;
}
