package Tuan3.LuuHoangNam.entities;

import Tuan3.LuuHoangNam.validators.annotations.ValidCategoryId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 50, nullable = false)
    @Size(min = 1, max = 50, message = "Title must be between 1 and 50 characters")
    @NotBlank(message = "Title must not be blank")
    private String title;

    @Column(name = "author", length = 50, nullable = false)
    @Size(min = 1,max = 50, message = "Author must be between 1 and 50 characters")
    @NotBlank(message = "Author must not be blank")
    private String author;

    @Column(name = "price")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ValidCategoryId
    @ToString.Exclude
    private Category category;
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ItemInvoice> itemInvoices = new ArrayList<>();

//    @Column(name = "image_url")
//    private String imageUrl;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) !=
                Hibernate.getClass(o)) return false;
        Book book = (Book) o;
        return getId() != null && Objects.equals(getId(),
                book.getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Lob
    @Column(name = "image_data", nullable = true, columnDefinition = "LONGBLOB")
    private byte[] imageData;

    @Transient
    private MultipartFile imageFile;
}



