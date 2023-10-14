package kitchenpos.dao.jpa;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.EntityManager;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JpaTableGroupDao implements TableGroupDao {

    private final EntityManager em;

    public JpaTableGroupDao(EntityManager em) {
        this.em = em;
    }

    @Transactional
    @Override
    public TableGroup save(TableGroup entity) {
        if (Objects.isNull(entity.getId())) {
            em.persist(entity);
            return entity;
        }

        return em.merge(entity);
    }

    @Override
    public Optional<TableGroup> findById(Long id) {
        return Optional.ofNullable(em.find(TableGroup.class, id));
    }

    @Override
    public List<TableGroup> findAll() {
        return em.createQuery("SELECT tg FROM TableGroup tg", TableGroup.class)
            .getResultList();
    }
}
