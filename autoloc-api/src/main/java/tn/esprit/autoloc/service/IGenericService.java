package tn.esprit.autoloc.service;

import java.util.List;

public interface IGenericService<T, I>
        extends IReadService<T, I>, IWriteService<T, I>, IDeleteService<I> {

    boolean check(T entity, boolean isNew);

    T create(T entity);

    default T save(T entity) {
        return create(entity);
    }

    T findById(I id);

    List<T> findAll();

    T update(I id, T entity);

    void deleteById(I id);
}
