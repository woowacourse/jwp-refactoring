package kitchenpos.application;

import static kitchenpos.support.DomainFixture.빈_테이블_생성;
import static kitchenpos.support.DomainFixture.채워진_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.request.TableGroupCreateWithTableRequest;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:truncate.sql")
class TableGroupServiceTest {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    private final TableGroupService tableGroupService;

    @Autowired
    public TableGroupServiceTest(final OrderTableRepository orderTableRepository,
                                 final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupService = new TableGroupService(tableGroupRepository, orderTableRepository);
    }

    private OrderTable tableA;
    private OrderTable tableB;

    @BeforeEach
    void setUp() {
        tableA = orderTableRepository.save(빈_테이블_생성());
        tableB = orderTableRepository.save(빈_테이블_생성());
    }

    @Test
    void 단체_지정을_생성한다() {
        // given
        final var request = new TableGroupCreateRequest(List.of(
                new TableGroupCreateWithTableRequest(tableA.getId()),
                new TableGroupCreateWithTableRequest(tableB.getId()))
        );

        // when
        final var response = tableGroupService.create(request);

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getCreatedDate()).isBefore(LocalDateTime.now()),
                () -> assertThat(response.getOrderTables()).hasSize(2)
        );
    }

    @Test
    void 단체_지정하는_테이블의_수가_2보다_작으면_예외를_던진다() {
        // given
        final var request = new TableGroupCreateRequest(List.of(new TableGroupCreateWithTableRequest(tableA.getId())));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomError.TABLE_GROUP_MIN_TABLES_ERROR);
    }

    @Test
    void 단체_지정하는_테이블이_비어있지_않은_경우_예외를_던진다() {
        // given
        final var tableC = orderTableRepository.save(채워진_테이블_생성());
        final var request = new TableGroupCreateRequest(List.of(
                new TableGroupCreateWithTableRequest(tableA.getId()),
                new TableGroupCreateWithTableRequest(tableC.getId()))
        );

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomError.TABLE_GROUP_TABLE_NOT_EMPTY_ERROR);
    }

    @Test
    void 단체_지정하는_테이블이_이미_단체_지정되어_있는_경우_예외를_던진다() {
        // given
        tableGroupRepository.save(
                new TableGroup(List.of(tableA, tableB), LocalDateTime.now())
        );

        final var request = new TableGroupCreateRequest(List.of(
                new TableGroupCreateWithTableRequest(tableA.getId()),
                new TableGroupCreateWithTableRequest(tableB.getId()))
        );

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomError.TABLE_ALREADY_GROUPED_ERROR);
    }

    @Test
    void 단체_지정을_삭제한다() {
        // given
        final var tableGroup = tableGroupRepository.save(
                new TableGroup(List.of(tableA, tableB), LocalDateTime.now())
        );

        // when & then
        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
    }
}
