package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Sql(scripts = "classpath:truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class ServiceTest {

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected MenuProductRepository menuProductRepository;

    protected void 복수_상품_저장(final Product... products) {
        productRepository.saveAll(List.of(products));
    }

    protected MenuGroup 단일_메뉴그룹_저장(final MenuGroup menuGroup) {
        return menuGroupRepository.save(menuGroup);
    }

    protected Menu 단일_메뉴_저장(final Menu menu) {
        return menuRepository.save(menu);
    }
}
