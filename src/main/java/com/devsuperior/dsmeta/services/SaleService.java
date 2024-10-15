package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.projections.SaleSummaryProjection;
import com.devsuperior.dsmeta.services.exceptions.InvalidDateException;
import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SaleService {

	private LocalDate startDate;
	private LocalDate endDate;
	private String name;

	private final SaleRepository repository;

	public SaleService(SaleRepository repository) {
		this.repository = repository;
	}

	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	/*
	 * RELATÓRIO
	 */
	public Page<SaleReportDTO> report(String minDate, String maxDate, String name, Pageable pageable) {

		setPeriod(minDate, maxDate);
		setName(name);

		Page<SaleReportDTO> reportDTOPages = repository.report(startDate, endDate, this.name, pageable);

		return reportDTOPages;

	}

	/*
	 * SUMÁRIO
	 */
	public List<SaleSummaryDTO> summary(String minDate, String maxDate) {

		setPeriod(minDate, maxDate);

		List<SaleSummaryProjection> projectionList = repository.summary(startDate, endDate);

		List<SaleSummaryDTO> summaryDTOList = projectionList.stream().map(x -> new SaleSummaryDTO(x)).collect(Collectors.toList());

		return summaryDTOList;

	}

	/*
	 * REGRA DE NEGÓCIO PARA O PERÍODO
	 *
	 * Validar data inicial e final conforme desafio solicitado.
	 * Validar data inicial e final se foi preenchida corretamente para não aceitar datas inválidas.
	 * Validar data inicial e final referente ao período deve ser um período válido.
	 */
	private void setPeriod(String minDate, String maxDate) {

		// Se a data final não for informada, considerar a data atual do sistema
		try {
			if (maxDate.isEmpty()) {
				maxDate = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()).toString();
			}
			endDate = LocalDate.parse(maxDate);
		} catch (DateTimeParseException e) {
			throw new InvalidDateException("Data final inválida.");
		}

		// Se a data inicial não for informada, considerar a data de 1 ano antes da data final
		try {
			if (minDate.isEmpty()) {
				minDate = LocalDate.parse(maxDate).minusYears(1L).toString();
			}
			startDate = LocalDate.parse(minDate);
		} catch (DateTimeParseException e) {
			throw new InvalidDateException("Data inicial inválida.");
		}

		// A data inicial deve ser menor ou igual a data final
		if (startDate.isAfter(endDate) || endDate.isBefore(startDate)) {
			throw new InvalidDateException("Período inválido, a data inicial deve ser menor ou igual a data final.");
		}

	}

	/*
	 * REGRA DE NEGÓCIO PARA O PREENCHIMENTO DO NOME
	 */
	private void setName(String name) {

		// Se o nome não for informado, considerar o texto vazio

		if (name.isEmpty()) {
			this.name = "";
		} else {
			this.name = name;
		}

	}
}
