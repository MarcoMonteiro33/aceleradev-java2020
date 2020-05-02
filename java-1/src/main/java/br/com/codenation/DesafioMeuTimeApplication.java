package br.com.codenation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import br.com.codenation.desafio.annotation.Desafio;
import br.com.codenation.desafio.app.MeuTimeInterface;
import br.com.codenation.desafio.exceptions.CapitaoNaoInformadoException;
import br.com.codenation.desafio.exceptions.IdentificadorUtilizadoException;
import br.com.codenation.desafio.exceptions.JogadorNaoEncontradoException;
import br.com.codenation.desafio.exceptions.TimeNaoEncontradoException;

public class DesafioMeuTimeApplication implements MeuTimeInterface {

	List<Time> times = new ArrayList<>();

	List<Jogador> jogadores = new ArrayList<>();



	@Desafio("incluirTime")
	public void incluirTime(Long id, String nome, LocalDate dataCriacao, String corUniformePrincipal, String corUniformeSecundario) {
			if(timeExistente(id)) throw new IdentificadorUtilizadoException();
			if (!nome.isEmpty() && !dataCriacao.equals(null) && !corUniformePrincipal.isEmpty() && !corUniformeSecundario.isEmpty()){
				times.add(new Time(id, nome, dataCriacao, corUniformePrincipal, corUniformeSecundario));
			}
	}

	@Desafio("incluirJogador")
	public void incluirJogador(Long id, Long idTime, String nome, LocalDate dataNascimento, Integer nivelHabilidade, BigDecimal salario) {

		if(jogadorExistente(id)) throw new IdentificadorUtilizadoException();
		if(!timeExistente(idTime)) throw new TimeNaoEncontradoException();
		validarHabilidade(nivelHabilidade);

		jogadores.add(new Jogador(id, idTime, nome, dataNascimento, nivelHabilidade, salario));
	}

	@Desafio("definirCapitao")
	public void definirCapitao(Long idJogador) {

		Jogador jogador = localizarJogador(idJogador);
		jogadores.stream()
				.filter(capitao-> capitao.getIdTime().equals(jogador.getIdTime()) && capitao.isCapitao())
				.findFirst()
				.ifPresent(capitao -> capitao.setCapitao(false));
		jogadores.stream()
				.filter(novoCapitao -> novoCapitao.getId().equals(jogador.getId()))
				.findFirst()
				.ifPresent(novoCapitao -> novoCapitao.setCapitao(true));

	}

	@Desafio("buscarCapitaoDoTime")
	public Long buscarCapitaoDoTime(Long idTime) {
		if(!timeExistente(idTime)) throw new TimeNaoEncontradoException();
		return capitao(idTime).getId();
	}

	@Desafio("buscarNomeJogador")
	public String buscarNomeJogador(Long idJogador) {
		return  localizarJogador(idJogador).getNome();
	}

	@Desafio("buscarNomeTime")
	public String buscarNomeTime(Long idTime) {
		return localizarTime(idTime).getNome();
	}

	@Desafio("buscarJogadoresDoTime")
	public List<Long> buscarJogadoresDoTime(Long idTime) {
		if(!timeExistente(idTime)) throw new TimeNaoEncontradoException();
		return jogadores
				.stream()
				.filter(jogador -> jogador.getIdTime().equals(idTime))
				.sorted(Comparator.comparingLong(Jogador::getId))
				.map(Jogador::getId)
				.collect(Collectors.toList());
	}

	@Desafio("buscarMelhorJogadorDoTime")
	public Long buscarMelhorJogadorDoTime(Long idTime) {
		if(!timeExistente(idTime)) throw new TimeNaoEncontradoException();

		return jogadores.stream()
				.filter(jogador -> jogador.getIdTime().equals(idTime))
				.max(Comparator.comparing(Jogador::getNivelHabilidade)).get().getId();
	}

	@Desafio("buscarJogadorMaisVelho")
	public Long buscarJogadorMaisVelho(Long idTime) {
		if(!timeExistente(idTime)) throw new TimeNaoEncontradoException();
		return jogadores.stream()
				.filter(jogador -> jogador.getIdTime().equals(idTime))
				.min(Comparator.comparing(Jogador::getDataNascimento)).get().getId();
	}

	@Desafio("buscarTimes")
	public List<Long> buscarTimes() {

		return times.stream()
				.sorted(Comparator.comparing(Time::getId))
				.map(Time::getId)
				.collect(Collectors.toList());
	}

	@Desafio("buscarJogadorMaiorSalario")
	public Long buscarJogadorMaiorSalario(Long idTime) {

		if(!timeExistente(idTime)) throw new TimeNaoEncontradoException();

		return  jogadores.stream()
				.filter(j -> j.getIdTime().equals(idTime))
				.sorted(Comparator.comparingLong(Jogador::getId))
				.max(Comparator.comparing(Jogador::getSalario))
				.map(Jogador::getId)
				.get();
	}

	@Desafio("buscarSalarioDoJogador")
	public BigDecimal buscarSalarioDoJogador(Long idJogador) {
		return localizarJogador(idJogador).getSalario();
	}

	@Desafio("buscarTopJogadores")
	public List<Long> buscarTopJogadores(Integer top) {

		return jogadores.stream()
				.sorted(Comparator.comparing(Jogador::getNivelHabilidade)
						.reversed()
						.thenComparingLong(Jogador::getId))
				.limit(top)
				.map(Jogador::getId)
				.collect(Collectors.toList());
	}

	@Desafio("buscarCorCamisaTimeDeFora")
	public String buscarCorCamisaTimeDeFora(Long timeDaCasa, Long timeDeFora) {

		Time timeCasa = localizarTime(timeDaCasa);
		Time timeFora = localizarTime(timeDeFora);

		if(timeCasa.getCorUniformePrincipal().equals(timeFora.getCorUniformePrincipal())){
			return timeFora.getCorUniformeSecundario();
		}
		return timeFora.getCorUniformePrincipal();
	}

	public Boolean timeExistente(Long id) {
		return times.stream()
				.anyMatch(time -> time.getId().equals(id));
	}

	public Boolean jogadorExistente(Long id) {
		return jogadores.stream()
				.anyMatch(jogador -> jogador.getId().equals(id));
	}

	public Jogador localizarJogador(Long idJogador){
		return jogadores.stream()
				.filter(jogador -> jogador.getId().equals(idJogador))
				.findFirst()
				.orElseThrow(JogadorNaoEncontradoException::new);
	}

	public Jogador capitao(Long idTime) throws CapitaoNaoInformadoException{
		return jogadores.stream()
				.filter(jogador -> jogador.getIdTime().equals(idTime) && jogador.isCapitao())
				.findFirst()
				.orElseThrow(CapitaoNaoInformadoException::new);
	}

	public Time localizarTime(Long idTime){
		return times.stream()
				.filter(time -> time.getId().equals(idTime))
				.findFirst()
				.orElseThrow(TimeNaoEncontradoException::new);
	}

	private void validarHabilidade(Integer nivelHabilidade) {
		if (nivelHabilidade < 0 || nivelHabilidade > 100) {
			throw new IllegalArgumentException();
		}

	}
}
