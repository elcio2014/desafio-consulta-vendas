package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.projections.SaleSummaryProjection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    // REPORT: Utilizado JPQL porque foi solicitado paginação
    @Query("SELECT new com.devsuperior.dsmeta.dto.SaleReportDTO(obj.id, obj.amount, obj.date, obj.seller.name) "
            + "FROM Sale obj "
            + "WHERE obj.date BETWEEN :minDate AND :maxDate "
            + "AND UPPER(obj.seller.name) "
            + "LIKE CONCAT('%', UPPER(:name), '%')")
    Page<SaleReportDTO> report(LocalDate minDate, LocalDate maxDate, String name, Pageable pageable);

    // SUMMARY: Utilizado SQL Nativo porque não foi solicitado paginação
    @Query(nativeQuery = true,
            value = "SELECT tb_seller.name, SUM(tb_sales.amount) AS sum "
            + "FROM tb_seller "
            + "INNER JOIN tb_sales ON tb_sales.seller_id = tb_seller.id "
            + "WHERE tb_sales.date BETWEEN :minDate AND :maxDate "
            + "GROUP BY tb_seller.name")
    List<SaleSummaryProjection> summary(LocalDate minDate, LocalDate maxDate);

}
