package kitchenpos.order.domain;

import static kitchenpos.order.domain.OrderTableFixture.단체_지정_빈_주문_테이블;
import static kitchenpos.order.domain.OrderTableFixture.단체_지정_없는_빈_주문_테이블;
import static kitchenpos.tablegroup.domain.TableGroupFixture.단체_지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest
class OrderTableRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    void ID_목록으로_모든_주문_테이블을_조회한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(단체_지정_없는_빈_주문_테이블());

        // when
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(List.of(orderTable.getId()));

        // then
        assertThat(orderTables).usingRecursiveComparison()
                .isEqualTo(List.of(orderTable));
    }

    @Test
    void 단체_지정_ID로_주문_테이블을_조회한다() {
        // given
        Long tableGroupId = tableGroupRepository.save(단체_지정()).getId();
        OrderTable orderTable = orderTableRepository.save(단체_지정_빈_주문_테이블(tableGroupId));

        // when
        List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(tableGroupId);

        // then
        assertThat(orderTables).usingRecursiveComparison()
                .isEqualTo(List.of(orderTable));
    }
}
