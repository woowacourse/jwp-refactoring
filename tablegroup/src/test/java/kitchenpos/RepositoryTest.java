package kitchenpos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

@DataJpaTest
public class RepositoryTest extends BaseTest {

    @Autowired
    protected EntityManager em;
}
