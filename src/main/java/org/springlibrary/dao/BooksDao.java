package org.springlibrary.dao;

import org.springframework.stereotype.Repository;
import org.springlibrary.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
public class BooksDao {


    public BooksDao() {
    }

    public Optional<Book> findById(long id) {

    }

    public Optional<Book> findByTitle(String title) {

    }

    public List<Book> findAll() {

    }

    public int create(Book book) {

    }

    public int update(Book book) {

    }

    public int deleteById(long id) {

    }

    public int deleteByTitle(String title) {

    }
}
