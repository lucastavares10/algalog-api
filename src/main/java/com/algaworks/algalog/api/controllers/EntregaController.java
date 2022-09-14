package com.algaworks.algalog.api.controllers;

import com.algaworks.algalog.api.assembler.EntregaAssembler;
import com.algaworks.algalog.api.assembler.EntregaDisassembler;
import com.algaworks.algalog.api.model.EntregaModel;
import com.algaworks.algalog.api.model.input.EntregaInput;
import com.algaworks.algalog.domain.repository.EntregaRepository;
import com.algaworks.algalog.domain.service.CancelamentoEntregaService;
import com.algaworks.algalog.domain.service.FinalizacaoEntregaService;
import com.algaworks.algalog.domain.service.SolicitacaoEntregaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/entregas")
public class EntregaController {

    private final FinalizacaoEntregaService finalizacaoEntregaService;
    private final CancelamentoEntregaService cancelamentoEntregaService;
    private final SolicitacaoEntregaService solicitacaoEntregaService;
    private final EntregaRepository entregaRepository;
    private final EntregaAssembler entregaAssembler;
    private final EntregaDisassembler entregaDisassembler;

    public EntregaController(FinalizacaoEntregaService finalizacaoEntregaService,
                             CancelamentoEntregaService cancelamentoEntregaService, SolicitacaoEntregaService solicitacaoEntregaService,
                             EntregaRepository entregaRepository, EntregaAssembler entregaAssembler, EntregaDisassembler entregaDisassembler) {
        super();
        this.finalizacaoEntregaService = finalizacaoEntregaService;
        this.cancelamentoEntregaService = cancelamentoEntregaService;
        this.solicitacaoEntregaService = solicitacaoEntregaService;
        this.entregaRepository = entregaRepository;
        this.entregaAssembler = entregaAssembler;
        this.entregaDisassembler = entregaDisassembler;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntregaModel solicitar(@Valid @RequestBody EntregaInput entregaInput) {
        return entregaAssembler
				.toModel(solicitacaoEntregaService
						.solicitar(entregaDisassembler.toDomainModel(entregaInput)));
    }

    @GetMapping
    public List<EntregaModel> index() {
        return entregaAssembler.toCollectionModel(entregaRepository.findAll());
    }

    @GetMapping("/{entregaId}")
    public ResponseEntity<EntregaModel> show(@PathVariable Long entregaId) {
        return entregaRepository.findById(entregaId)
                .map(entrega -> ResponseEntity.ok(entregaAssembler.toModel(entrega)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{entregaId}/finalizacao")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void finalizar(@PathVariable Long entregaId) {
        finalizacaoEntregaService.finalizar(entregaId);
    }

    @PutMapping("/{entregaId}/cancelamento")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void cancelar(@PathVariable Long entregaId) {
        cancelamentoEntregaService.cancelar(entregaId);
    }
}
