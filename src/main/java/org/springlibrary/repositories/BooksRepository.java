package org.springlibrary.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springlibrary.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
public class BooksRepository {

    private final SessionFactory sessionFactory;

    public BooksRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Book> findById(long id) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "SELECT b FROM Book b WHERE b.id = :id";
        Query<Book> query = session.createQuery(hql, Book.class);
        query.setParameter("id", id);

        return Optional.ofNullable(query.uniqueResult());
    }

    public Optional<Book> findByTitle(String title) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "SELECT b FROM Book b WHERE b.title = :title";
        Query<Book> query = session.createQuery(hql, Book.class);
        query.setParameter("title", title);
        return Optional.ofNullable(query.uniqueResult());
    }

    public List<Book> findAll() {
        Session session = sessionFactory.getCurrentSession();

        String hql = "SELECT b FROM Book b";
        Query<Book> query = session.createQuery(hql, Book.class);
        return query.getResultList();
    }

    public void create(Book book) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(book);
    }

    public boolean update(Book book) {
        Session session = sessionFactory.getCurrentSession();
        var result = session.merge(book);

        return result != null;
    }

    public int deleteById(long id) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "DELETE FROM Book b WHERE b.id = :id";
        Query<?> query = (Query<?>) session.createMutationQuery(hql);
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    public int deleteByTitle(String title) {
        Session session = sessionFactory.getCurrentSession();

        String hql = "DELETE FROM Book b WHERE b.title = :title";
        Query<?> query = (Query<?>) session.createMutationQuery(hql);
        query.setParameter("title", title);
        return query.executeUpdate();
    }
}
