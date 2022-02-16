package org.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.generation.blogpessoal.model.Usuario;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;

	/*
	 * A anotação @BeforeAll indica que o método deve ser executado uma única vez
	 * antes de todos os métodos da classe, para criar algumas pré-condições
	 * necessárias para todos os testes (criar objetos, por exemplo).
	 */

	@BeforeAll
	public void start() {

		usuarioRepository.save(new Usuario(0L, "João da Silva", "joao@email.com.br", "13465278", " "));

		usuarioRepository.save(new Usuario(0L, "Manuela da Silva", "manuela@email.com.br", "13465278", " "));

		usuarioRepository.save(new Usuario(0L, "Adriana da Silva", "adriana@email.com.br", "13465278", " "));

		usuarioRepository.save(new Usuario(0L, "Paulo Antunes", "paulo@email.com.br", "13465278", " "));
	}

	@Test
	@DisplayName("😎Retorna 1 Usuário")
	public void deveRetornarUmUsuario() {

		Optional<Usuario> usuario = usuarioRepository.findByUsuario("joao@email.com.br");

		assertTrue(usuario.get().getUsuario().equals("joao@email.com.br"));
	}

	@Test
	@DisplayName("😎Retorna 3 Usuários")
	public void deveRetornarTresUsuario() {

		List<Usuario> listaUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Silva");

		assertEquals(3, listaUsuarios.size());

		assertTrue(listaUsuarios.get(0).getNome().equals("João da Silva"));
	}

	/*
	 * A anotação @AfterAll indica que o método deve ser executado uma única vez
	 * depois de todos os testes da classe, para redefinir algumas condições após
	 * rodar todos os testes (redefinir objetos, por exemplo)
	 */

	@AfterAll
	public void end() {
		usuarioRepository.deleteAll();
	}

}
