package tn.esprit.autoloc.service;

public interface IWriteService<T, ID> {
    T save(T entity);
}
