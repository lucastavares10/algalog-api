package com.algaworks.algalog.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algalog.domain.exceptions.EmailJaCadastradoException;
import com.algaworks.algalog.domain.exceptions.EntidadeNaoEncontradaException;
import com.algaworks.algalog.domain.model.Cliente;
import com.algaworks.algalog.domain.repository.ClienteRepository;

import java.util.stream.Collectors;

@Service
public class CadastroClienteService {
	
	private ClienteRepository clienteRepository;
	
	public CadastroClienteService(ClienteRepository clienteRepository) {
		super();
		this.clienteRepository = clienteRepository;
	}

	@Transactional
	public Cliente save(Cliente cliente) {
		boolean emailEmUso = !clienteRepository.findByEmail(cliente.getEmail())
				.stream()
				.findAny()
				.isEmpty();

		if(emailEmUso) {
			throw new EmailJaCadastradoException("Já existe um cliente cadastrado com esse e-mail.");
		}
		
		return clienteRepository.save(cliente);
	}
	
	@Transactional
	public void delete(Long id) {
		clienteRepository.deleteById(id);
	}
	
	public Cliente findByIdOrError(Long id) {
		return clienteRepository
				.findById(id)
				.orElseThrow(() -> new EntidadeNaoEncontradaException(
						String.format(
								"Cliente de código %d não encontrado.", 
								id
						)));
	}
}
