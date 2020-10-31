package kitchenpos.dao;

import kitchenpos.domain.menu.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MenuRepositoryTest {
    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("findAllByIdIn 기능 테스트")
    @Test
    void countByIdIn() {
        Menu savedMenu1 = menuRepository.save(new Menu(null, "a", null, null, new ArrayList<>()));
        Menu savedMenu2 = menuRepository.save(new Menu(null, "b", null, null, new ArrayList<>()));
        menuRepository.save(new Menu(null, "c", null, null, new ArrayList<>()));

        List<Long> ids = Arrays.asList(savedMenu1.getId(), savedMenu2.getId(), 9999L);

        assertThat(menuRepository.findAllByIdIn(ids).size()).isEqualTo(2);
    }
}
