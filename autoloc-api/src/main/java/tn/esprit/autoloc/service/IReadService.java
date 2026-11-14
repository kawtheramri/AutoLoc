package tn.esprit.autoloc.service;

import java.util.List;

public interface IReadService<T, ID> {
    List<T> findAll();
    T findById(ID id);
}
