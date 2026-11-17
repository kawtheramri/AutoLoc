package tn.esprit.autoloc.service;

import java.util.List;

public interface IReadService<T, I> {
    List<T> findAll();
    T findById(I id);
}
