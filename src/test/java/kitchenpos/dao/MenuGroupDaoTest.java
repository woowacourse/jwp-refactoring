package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class MenuGroupDaoTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    void 메뉴_그룹_엔티티를_저장한다() {
        MenuGroup menuGroupEntity = createMenuGroupEntity();

        MenuGroup saveMenuGroup = menuGroupDao.save(menuGroupEntity);

        assertThat(saveMenuGroup.getId()).isPositive();
    }

    @Test
    void 메뉴_그룹_엔티티를_조회한다() {
        MenuGroup menuGroupEntity = createMenuGroupEntity();
        MenuGroup saveMenuGroup = menuGroupDao.save(menuGroupEntity);

        assertThat(menuGroupDao.findById(saveMenuGroup.getId())).isPresent();
    }

    @Test
    void 모든_메뉴_그룹_엔티티를_조회한다() {
        MenuGroup menuGroupEntityA = createMenuGroupEntity();
        MenuGroup menuGroupEntityB = createMenuGroupEntity();
        MenuGroup saveMenuGroupA = menuGroupDao.save(menuGroupEntityA);
        MenuGroup saveMenuGroupB = menuGroupDao.save(menuGroupEntityB);

        List<MenuGroup> menuGroups = menuGroupDao.findAll();

        assertThat(menuGroups).usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(saveMenuGroupA, saveMenuGroupB);
    }

    @Test
    void 메뉴_그룹_엔티티가_존재하면_TRUE_반환한다() {
        MenuGroup menuGroupEntity = createMenuGroupEntity();
        MenuGroup saveMenuGroup = menuGroupDao.save(menuGroupEntity);

        assertThat(menuGroupDao.existsById(saveMenuGroup.getId())).isTrue();
    }

    @Test
    void 메뉴_그룹_엔티티가_존재하지_않으면_FALSE_반환한다() {
        assertThat(menuGroupDao.existsById(-1L)).isFalse();
    }

    private MenuGroup createMenuGroupEntity() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("chicken");
        return menuGroup;
    }
}
