package kitchenpos.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class TableGroupDaoTest {

    @Autowired
    private TableGroupDao tableGroupDao;

    @Nested
    class save_메서드는 {

        private final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), null);

        @Nested
        class 테이블이_주어지면 {

            @Test
            void 저장한다() {
                final TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

                assertThat(savedTableGroup.getId()).isNotNull();
            }
        }
    }

    @Nested
    class findById_메서드는 {

        @Nested
        class 단체지정_id가_주어지면 {

            private final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), null);
            private TableGroup savedTableGroup;

            @BeforeEach
            void setUp() {
                savedTableGroup = tableGroupDao.save(tableGroup);
            }

            @Test
            void 해당하는_단체지정을_반환한다() {
                final Optional<TableGroup> foundTableGroup = tableGroupDao.findById(savedTableGroup.getId());

                assertAll(
                        () -> assertThat(foundTableGroup).isPresent(),
                        () -> assertThat(foundTableGroup.get()).usingRecursiveComparison()
                                .isEqualTo(savedTableGroup)
                );
            }
        }
    }

    @Nested
    class findAll_메서드는 {

        @Nested
        class 호출되면 {

            private final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), null);
            private TableGroup savedTableGroup;

            @BeforeEach
            void setUp() {
                savedTableGroup = tableGroupDao.save(tableGroup);
            }

            @Test
            void 모든_단체지정을_반환한다() {
                final List<TableGroup> tableGroups = tableGroupDao.findAll();

                assertThat(tableGroups).usingFieldByFieldElementComparator()
                        .containsAll(List.of(savedTableGroup));
            }
        }
    }
}
