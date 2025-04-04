package Tuan3.LuuHoangNam.viewmodels;

import Tuan3.LuuHoangNam.entities.Book;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
@Builder
public record BookPostVm(String title, String author, Double price,
                         Long categoryId) {
    public static BookPostVm from(@NotNull Book book) {
        return new BookPostVm(book.getTitle(), book.getAuthor(),
                book.getPrice(), book.getCategory().getId());
    }
}

