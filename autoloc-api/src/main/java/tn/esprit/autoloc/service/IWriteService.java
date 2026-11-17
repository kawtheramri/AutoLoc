package tn.esprit.autoloc.service;

public interface IWriteService<T, I> {
    T save(T entity);
}
