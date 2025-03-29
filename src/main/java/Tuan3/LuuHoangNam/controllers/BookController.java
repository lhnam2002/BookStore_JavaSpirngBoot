package Tuan3.LuuHoangNam.controllers;
import Tuan3.LuuHoangNam.entities.Book;
import Tuan3.LuuHoangNam.entities.Item;
import Tuan3.LuuHoangNam.services.BookService;
import Tuan3.LuuHoangNam.services.CartService;
import Tuan3.LuuHoangNam.services.CategoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService   bookService;
    private final CategoryService categoryService;
    private final CartService cartService;

    @GetMapping
    public String showAllBooks(@NotNull Model model,
                               @RequestParam(defaultValue = "0")
                               Integer pageNo,
                               @RequestParam(defaultValue = "3")
                               Integer pageSize,
                               @RequestParam(defaultValue = "id")
                               String sortBy) {
        model.addAttribute("books", bookService.getAllBooks(pageNo, pageSize, sortBy));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", bookService.getAllBooks(pageNo, pageSize, sortBy).size() / pageSize);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "book/list";
    }

    @GetMapping("/add")
    public String addBookForm(@NotNull Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories",
                categoryService.getAllCategories());
        return "book/add";
    }

    @PostMapping("/add")
    public String addBook(
            @Valid @ModelAttribute("book") Book book,
            @RequestPart("imageFile") MultipartFile imageFile,
            @org.antlr.v4.runtime.misc.NotNull BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "book/add";
        }

        if (!imageFile.isEmpty()) {
            try {
                // Lưu dữ liệu ảnh vào thuộc tính imageData của đối tượng book
                book.setImageData(imageFile.getBytes());
            } catch (IOException e) {
                // Xử lý ngoại lệ nếu có lỗi khi đọc dữ liệu ảnh
                e.printStackTrace();
                // Thêm thông báo lỗi vào model
                model.addAttribute("error", "Failed to upload image.");
                model.addAttribute("categories", categoryService.getAllCategories());
                return "book/add";
            }
        }

        // Lưu thông tin sản phẩm vào cơ sở dữ liệu
        bookService.addBook(book);
        return "redirect:/books";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(HttpSession session,
                            @RequestParam long id,
                            @RequestParam String name,
                            @RequestParam double price,
                            @RequestParam(defaultValue = "1") int
                                    quantity) {
        var cart = cartService.getCart(session);
        cart.addItems(new Item(id, name, price, quantity));
        cartService.updateCart(session, cart);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editBookForm(@NotNull Model model, @PathVariable long id) {
        var book = bookService.getBookById(id);
        model.addAttribute("book", book.orElseThrow(() -> new IllegalArgumentException("Book not found")));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "book/edit";
    }

    @PostMapping("/edit")
    public String editBook(@Valid @ModelAttribute("bo   ok") Book book,
                           @RequestPart("imageFile") MultipartFile imageFile,
                           @NotNull BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            model.addAttribute("categories",
                    categoryService.getAllCategories());
            return "book/edit";
        }

        if (!imageFile.isEmpty()) {
            try {
                book.setImageData(imageFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "Failed to upload image.");
                model.addAttribute("categories", categoryService.getAllCategories());
                return "book/edit";
            }
        }
        bookService.updateBook(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        bookService.getBookById(id)
                .ifPresentOrElse(
                        book -> bookService.deleteBookById(id),
                        () -> {
                            throw new IllegalArgumentException("Book not found");
                        });
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchBook(
            @NotNull Model model,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("books", bookService.searchBook(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages",
                bookService
                        .getAllBooks(pageNo, pageSize, sortBy)
                        .size() / pageSize);
        model.addAttribute("categories",
                categoryService.getAllCategories());
        return "book/list";
    }

    @GetMapping("/image/{id}")
    @ResponseBody
    public byte[] getBookImage(@PathVariable("id") Long id) {
        return bookService.getBookById(id)
                .map(Book::getImageData)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
    }
}
