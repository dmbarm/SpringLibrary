package org.springlibrary.repositories;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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

        return Optional.ofNullable(session.byId(Book.class).load(id));
    }

    public Optional<Book> findByTitle(String title) {
        Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> root = criteriaQuery.from(Book.class);
        criteriaQuery.select(root)
                        .where(criteriaBuilder.equal(root.get("title"), title));

        Query<Book> query = session.createQuery(criteriaQuery);
        return Optional.ofNullable(query.uniqueResult());
    }

    public List<Book> findAll() {
        Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        criteriaQuery.from(Book.class);

        return session.createQuery(criteriaQuery).list();
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

        Book book = session.byId(Book.class).loadOptional(id).orElse(null);
        if (book != null) {
            session.remove(book);
            return 1;
        }
        return 0;
    }

    public int deleteByTitle(String title) {
        Session session = sessionFactory.getCurrentSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaDelete<Book> criteriaDelete = criteriaBuilder.createCriteriaDelete(Book.class);
        Root<Book> root = criteriaDelete.from(Book.class);
        criteriaDelete.where(criteriaBuilder.equal(root.get("title"), title));

        return session.createMutationQuery(criteriaDelete).executeUpdate();
    }
}
