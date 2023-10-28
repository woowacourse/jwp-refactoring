package kitchenpos.support;

import java.util.List;
import java.util.Optional;

public class BaseRepository<ENTITY, DATA, ACCESS extends BasicDataAccessor<DATA>, CONVERT extends Converter<ENTITY, DATA>> {

    protected final ACCESS dataAccessor;
    protected final CONVERT converter;

    protected BaseRepository(final ACCESS dataAccessor, final CONVERT converter) {
        this.dataAccessor = dataAccessor;
        this.converter = converter;
    }

    public ENTITY save(final ENTITY entity) {
        final DATA savedData = dataAccessor.save(converter.entityToData(entity));
        return converter.dataToEntity(savedData);
    }

    public Optional<ENTITY> findById(final Long id) {
        return dataAccessor.findById(id)
                .map(converter::dataToEntity);
    }

    public List<ENTITY> findAll() {
        return converter.dataToEntity(dataAccessor.findAll());
    }
}
