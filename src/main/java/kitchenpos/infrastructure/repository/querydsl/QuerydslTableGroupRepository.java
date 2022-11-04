package kitchenpos.infrastructure.repository.querydsl;

import static kitchenpos.domain.table.QTableGroup.tableGroup;
import static kitchenpos.infrastructure.repository.querydsl.QuerydslUtils.*;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import org.springframework.stereotype.Repository;

@Repository
public class QuerydslTableGroupRepository implements TableGroupRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public QuerydslTableGroupRepository(final EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public TableGroup save(final TableGroup entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(
                queryFactory.selectFrom(tableGroup)
                        .where(idEq(id))
                        .fetchOne()
        );
    }

    @Override
    public List<TableGroup> findAll() {
        return queryFactory.selectFrom(tableGroup)
                .fetch();
    }

    private BooleanBuilder idEq(final Long id) {
        return nullSafeBuilder(() -> tableGroup.id.eq(id));
    }
}
