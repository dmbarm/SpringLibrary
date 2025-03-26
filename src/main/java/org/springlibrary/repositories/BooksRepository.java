package org.springlibrary.repositories;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Repository;
import org.springlibrary.models.Book;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Repository
public class BooksRepository {

    private static final File file = new File("books.csv");
    private static final CsvSchema schema = CsvSchema.emptySchema()
            .withHeader()
            .withColumnSeparator(',');

    private final CsvMapper mapper = new CsvMapper();

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
