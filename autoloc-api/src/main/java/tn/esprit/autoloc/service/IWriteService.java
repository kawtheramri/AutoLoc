package tn.esprit.autoloc.service;

public interface IWriteService<T> {
    T save(T entity);
}
