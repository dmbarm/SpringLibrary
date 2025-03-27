package org.springlibrary.repositories;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Repository;
import org.springlibrary.models.Book;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Repository
public class BooksRepository {

    private static final File file = new File("src/main/resources/books.csv");
    private final CsvMapper mapper = new CsvMapper();
    private final CsvSchema schema = CsvSchema.builder()
            .addColumn("id", CsvSchema.ColumnType.NUMBER)
            .addColumn("title")
            .addColumn("author")
            .addColumn("description")
            .setUseHeader(true)
            .setColumnSeparator(',')
            .build();

    public BooksRepository() {
    }

    public List<Book> findAll() throws IOException {
        MappingIterator<Book> it = mapper.readerFor(Book.class)
                .with(schema)
                .readValues(file);
        return it.readAll();
    }

    public void saveAll(List<Book> books) throws IOException {
        mapper.writerFor(Book.class)
                .with(schema)
                .writeValues(file)
                .writeAll(books);
    }
}
