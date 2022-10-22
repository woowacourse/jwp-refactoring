package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    MenuGroupDao menuGroupDao;

    @Autowired
    MenuGroupService sut;

    @Test
    @DisplayName("MenuGroup을 생성한다")
    void delegateSaveAndReturnSavedEntity() {
        // given
        MenuGroup expected = new MenuGroup();
        expected.setName("추천메뉴");

        // when
        MenuGroup actual = sut.create(expected);

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("MenuGroup 목록을 조회한다")
    void returnAllSavedEntities() {
        List<MenuGroup> actual = sut.list();

        assertThat(actual).hasSize(4);
    }
}
