package org.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.generation.blogpessoal.model.Usuario;
import org.generation.blogpessoal.repository.UsuarioRepository;
import org.generation.blogpessoal.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	@Test
	@Order(1)
	@DisplayName("Cadastrar Usuário")
	public void deveCriarUmUsuario() {
		
		//Requisição
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, 
				"Paulo Antunes", "paulo_antunes@email.com.br", "13465278", " "));
		
		//Enviando a requisição e recebendo uma resposta
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST,
				requisicao, Usuario.class);

		//Checando se a reposta foi a esperada
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		
	}
	
	@Test
	@Order(2)
	@DisplayName("Não deve permitir a duplicação do Usuário")
	public void naoDeveDuplicarUsuario() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Maria da Silva", "maria_silva@email.com.br", "13465278", " "));
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, 
				"Maria da Silva", "maria_silva@email.com.br", "13465278", " "));
		
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST,
				requisicao, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
		
	}
	
	
	
	@Test
	@Order(3)
	@DisplayName("Atualizar Usuário")
	public void deveAtualizarUmUsuario() {
		
		Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Juliana Andrews", "juliana_andrews@email.com.br", "juliana123", " "));
		
		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(), 
				"Juliana Andrews Ramos", "juliana_ramos@email.com.br", "juliana123", " ");
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);
		
		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/atualizar", HttpMethod.PUT,requisicao, Usuario.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		
	}
	
	
	@Test
	@Order(4)
	@DisplayName("Listar todos os Usuário")
	public void deveListarTodosOsUsuarios() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Sabrina Sanches", "sabrina_sanches@email.com.br", "sabrina123", " "));
			
			usuarioService.cadastrarUsuario(new Usuario(0L, 
				"Ricardo Marques", "ricardo_marques@email.com.br", "ricardo123", " "));
		
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
				
	}
	
	@Test
	@Order(5)
	@DisplayName("Procurar usuário por ID")
	public void procurarUsuariosPorId() {
		
		Usuario usuario = usuarioRepository.save(new Usuario(0L, 
				"Sara Braga", "sara_braga@email.com.br", "sara1234", " "));
	
		ResponseEntity<String> resposta = testRestTemplate
			.withBasicAuth("root", "root")
			.exchange("/usuarios/"+usuario.getId(), HttpMethod.GET, null, String.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	
}