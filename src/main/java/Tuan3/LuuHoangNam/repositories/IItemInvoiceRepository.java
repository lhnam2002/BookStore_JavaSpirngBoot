package Tuan3.LuuHoangNam.repositories;

import Tuan3.LuuHoangNam.entities.ItemInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IItemInvoiceRepository extends
        JpaRepository<ItemInvoice, Long>{
}
