package kitchenpos.repository.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.MenuFixtures;
import kitchenpos.domain.menu.Menu;
import kitchenpos.repository.MenuRepository;
import kitchenpos.support.RepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class MenuEntityRepositoryTest {

    private final MenuRepository menuRepository;
    private final MenuEntityRepository menuEntityRepository;

    @Autowired
    public MenuEntityRepositoryTest(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
        this.menuEntityRepository = new MenuEntityRepositoryImpl(menuRepository);
    }

    @Test
    void existsAllByIdIn() {
        // given
        Menu menu = menuRepository.save(MenuFixtures.createMenu());
        // when
        boolean exists = menuEntityRepository.existsAllByIdIn(List.of(menu.getId()));
        // then
        assertThat(exists).isTrue();
    }

    @Test
    void existsAlLByIdInWithInvalidId() {
        // given
        Long invalidId = 999L;
        // when
        boolean exists = menuEntityRepository.existsAllByIdIn(List.of(invalidId));
        // then
        assertThat(exists).isFalse();
    }
}
