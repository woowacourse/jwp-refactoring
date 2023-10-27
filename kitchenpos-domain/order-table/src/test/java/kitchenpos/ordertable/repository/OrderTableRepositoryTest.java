package kitchenpos.ordertable.repository;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.support.RepositoryTest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class OrderTableRepositoryTest {

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    TableGroupRepository tableGroupRepository;

    @Nested
    class findAllByIdIn {

        @Test
        void 식별자_목록으로_모든_엔티티_조회() {
            // given
            List<Long> ids = List.of(1L, 2L, 3L);
            for (int i = 0; i < 3; i++) {
                OrderTable orderTable = new OrderTable(null, false, 0);
                orderTableRepository.save(orderTable);
            }

            // when
            List<OrderTable> actual = orderTableRepository.findAllByIdIn(ids);

            // then
            Assertions.assertThat(actual).hasSize(3);
        }
    }

    @Nested
    class findAllByTableGroupId {

        @Test
        void 테이블_그룹_식별자로_모든_엔티티_조회() {
            // given
            LocalDateTime createdDate = LocalDateTime.parse("2023-10-15T22:40:00");
            TableGroup tableGroup = tableGroupRepository.save(new TableGroup(null, createdDate));
            for (int i = 0; i < 3; i++) {
                OrderTable orderTable = new OrderTable(null, false, 0);
                orderTable.changeTableGroup(tableGroup);
                orderTableRepository.save(orderTable);
            }

            // when
            List<OrderTable> actual = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

            // then
            Assertions.assertThat(actual).hasSize(3);
        }
    }
}
