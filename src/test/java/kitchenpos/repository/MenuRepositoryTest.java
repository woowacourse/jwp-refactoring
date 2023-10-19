package kitchenpos.repository;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void countByIdIn() {
        // given
        final MenuGroup japanese = createMenuGroup("일식");
        final Menu sushi = createMenu("초밥", BigDecimal.valueOf(15000), japanese);
        final Menu ramen = createMenu("라면", BigDecimal.valueOf(9000), japanese);
        createMenu("텐동", BigDecimal.valueOf(10000), japanese);

        em.flush();
        em.close();

        // when
        final long notSavedMenuId = -1L;
        final long actual = menuRepository.countByIdIn(List.of(sushi.getId(), ramen.getId(), notSavedMenuId));

        // then
        assertThat(actual).isEqualTo(2);
    }
}
