package Tuan3.LuuHoangNam.repositories;

import Tuan3.LuuHoangNam.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IInvoiceRepository extends JpaRepository<Invoice,
        Long>{
}