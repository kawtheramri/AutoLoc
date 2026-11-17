package tn.esprit.autoloc.service;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.exception.ResourceNotFoundException;

import java.util.List;

public abstract class GenericServiceImpl<T, I> implements IGenericService<T, I> {

    private final JpaRepository<T, I> repository;

    protected GenericServiceImpl(JpaRepository<T, I> repository) {
        this.repository = repository;
    }

    protected abstract String getEntityName();

    protected abstract I getId(T entity);

    protected abstract void setFields(T existing, T updated);

    @Override
    public T create(T entity) {
        if (getId(entity) != null) {
            throw new IllegalArgumentException(
                    "L'identifiant de " + getEntityName() + " doit être vide à la création");
        }
        if (!check(entity, true)) {
            throw new IllegalArgumentException("La vérification de " + getEntityName() + " a échoué");
        }
        return repository.save(entity);
    }

    @Override
    public T save(T entity) {
        I id = getId(entity);
        if (id == null) {
            return create(entity);
        }
        return update(id, entity);
    }

    @Override
    public T findById(I id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName(), String.valueOf(id)));
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public T update(I id, T entity) {
        T existing = findById(id);
        if (!check(entity, false)) {
            throw new IllegalArgumentException("La vérification de " + getEntityName() + " a échoué");
        }
        setFields(existing, entity);
        return repository.save(existing);
    }

    @Override
    public void deleteById(I id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(getEntityName(), String.valueOf(id));
        }
        repository.deleteById(id);
    }
}
