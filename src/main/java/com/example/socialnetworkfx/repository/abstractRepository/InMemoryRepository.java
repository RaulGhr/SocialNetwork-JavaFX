package com.example.socialnetworkfx.repository.abstractRepository;



import com.example.socialnetworkfx.domain.abstractDomain.Entity;
import com.example.socialnetworkfx.repository.paging.Page;
import com.example.socialnetworkfx.repository.paging.Pageable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    Map<ID, E> entities;

    public InMemoryRepository() {

        entities = new HashMap<ID, E>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public Page<E> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<E> save(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");

        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public Optional<E> delete(ID id) {

        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }
        return Optional.ofNullable(entities.remove(id));
    }

    public void deleteAll(){
        this.entities.clear();
    }

    @Override
    public Optional<E> update(E entity) {

        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");


//        if (entities.get(entity.getId()) != null) {
//            entities.put(entity.getId(), entity);
//            return null;
//        }

//        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
        return Optional.ofNullable(entities.replace(entity.getId(), entity));

    }

    @Override
    public Integer size(){
        return entities.size();
    }


}
