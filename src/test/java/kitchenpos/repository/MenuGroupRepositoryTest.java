package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class MenuGroupRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴_그룹_엔티티를_저장한다() {
        MenuGroup menuGroupEntity = createMenuGroupEntity();

        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroupEntity);

        assertThat(savedMenuGroup.getId()).isPositive();
    }

    @Test
    void 메뉴_그룹_엔티티를_조회한다() {
        MenuGroup menuGroupEntity = createMenuGroupEntity();
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroupEntity);

        assertThat(menuGroupRepository.findById(savedMenuGroup.getId())).isPresent();
    }

    @Test
    void 모든_메뉴_그룹_엔티티를_조회한다() {
        MenuGroup menuGroupEntityA = createMenuGroupEntity();
        MenuGroup menuGroupEntityB = createMenuGroupEntity();
        MenuGroup savedMenuGroupA = menuGroupRepository.save(menuGroupEntityA);
        MenuGroup savedMenuGroupB = menuGroupRepository.save(menuGroupEntityB);

        List<MenuGroup> menuGroups = menuGroupRepository.findAll();

        assertThat(menuGroups).usingRecursiveFieldByFieldElementComparatorOnFields("id")
                .contains(savedMenuGroupA, savedMenuGroupB);
    }

    @Test
    void 메뉴_그룹_엔티티가_존재하면_TRUE_반환한다() {
        MenuGroup menuGroupEntity = createMenuGroupEntity();
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroupEntity);

        assertThat(menuGroupRepository.existsById(savedMenuGroup.getId())).isTrue();
    }

    @Test
    void 메뉴_그룹_엔티티가_존재하지_않으면_FALSE_반환한다() {
        assertThat(menuGroupRepository.existsById(-1L)).isFalse();
    }

    private MenuGroup createMenuGroupEntity() {
        return MenuGroup.builder()
                .name("chicken")
                .build();
    }
}
