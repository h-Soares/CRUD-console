package model.dao;

import java.util.List;
import java.util.Optional;
import model.entities.enums.Categories;

public interface GenericDAO<T> {
    void insert(T object);
    List<T> findAll();
    void update(T object);
    void delete(Integer ID);
    Optional<T> findById(Integer ID);
    List<T> findByCategory(Categories category);
}