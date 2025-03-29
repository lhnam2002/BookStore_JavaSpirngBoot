package Tuan3.LuuHoangNam.repositories;

import Tuan3.LuuHoangNam.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ICategoryRepository extends
        JpaRepository<Category, Long> {
}