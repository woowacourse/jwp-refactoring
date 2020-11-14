package kitchenpos.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import kitchenpos.config.JpaAuditingConfig;
import kitchenpos.domain.Menu;
import kitchenpos.fixture.MenuFixture;

@DataJpaTest
@Import(JpaAuditingConfig.class)
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("Id range에 포함되는 menu 개수를 반환한다.")
    @Test
    void countByIdIn() {
        Menu menu1 = MenuFixture.createWithoutId(1L, 1000L);
        Menu menu2 = MenuFixture.createWithoutId(1L, 1000L);
        Menu menu3 = MenuFixture.createWithoutId(1L, 1000L);
        menuRepository.saveAll(Arrays.asList(menu1, menu2, menu3));

        assertThat(menuRepository.countByIdIn(Arrays.asList(1L, 3L))).isEqualTo(2L);
    }
}
