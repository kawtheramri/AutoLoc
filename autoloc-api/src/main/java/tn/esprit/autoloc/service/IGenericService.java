package tn.esprit.autoloc.service;

import java.util.List;

public interface IGenericService<T, ID> {

    boolean check(T entity, boolean isNew);

    T create(T entity);

    default T save(T entity) {
        return create(entity);
    }

    T findById(ID id);

    List<T> findAll();

    T update(ID id, T entity);

    void deleteById(ID id);
}
