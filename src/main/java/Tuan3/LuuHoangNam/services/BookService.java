package Tuan3.LuuHoangNam.services;

import Tuan3.LuuHoangNam.entities.Book;
import Tuan3.LuuHoangNam.repositories.IBookRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class BookService {
    private final IBookRepository bookRepository;
    public List<Book> getAllBooks(Integer pageNo, Integer pageSize, String sortBy) {
        return bookRepository.findAllBooks(pageNo, pageSize, sortBy);
    }
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
    public void addBook(Book book)
    {
        bookRepository.save(book);
    }
    public void updateBook(@NotNull Book book) {
        Book existingBook = bookRepository.findById(book.getId()).orElse(null);
        Objects.requireNonNull(existingBook).setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());
        existingBook.setCategory(book.getCategory());
        if (book.getImageData() != null) {
            existingBook.setImageData(book.getImageData());
        }
        bookRepository.save(existingBook);
    }
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
    public List<Book> searchBook(String keyword) {
        return bookRepository.searchBook(keyword);
    }
    public String getImageBase64(byte[] imageData) {
        return Base64.getEncoder().encodeToString(imageData);
    }
}
