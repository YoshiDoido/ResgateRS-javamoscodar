package uol.compass.domain.service;

import lombok.extern.slf4j.Slf4j;
import uol.compass.domain.dao.DoacaoDAO;
import uol.compass.domain.dao.OrdemPedidoDAO;
import uol.compass.domain.exception.OrdemPedidoNaoEncontradaException;
import uol.compass.domain.model.*;
import uol.compass.infrastructure.dao_implementation.DoacaoDAOImpl;
import uol.compass.infrastructure.dao_implementation.OrdemPedidoDAOImpl;

import static uol.compass.domain.service.CentroDistribuicaoService.CATEGORIA_CENTRO_ESTOQUE_MAXIMO;

import java.util.List;
import java.util.Objects;

@Slf4j
public class OrdemPedidoService {

    public static final int CATEGORIA_ABRIGO_ESTOQUE_MAXIMO = 200;
    private final OrdemPedidoDAO ordemPedidoDAO = new OrdemPedidoDAOImpl();
    private final AbrigoService abrigoService = new AbrigoService();
    private final CentroDistribuicaoService centroDistribuicaoService = new CentroDistribuicaoService();
    private final DoacaoDAO doacaoDAO = new DoacaoDAOImpl();

    public List<OrdemPedido> findAll() {
        return ordemPedidoDAO.findAll();
    }

    public OrdemPedido findByIdOrException(Integer id) {
        Objects.requireNonNull(id);
        return ordemPedidoDAO.findById(id)
                .orElseThrow(() -> new OrdemPedidoNaoEncontradaException(id));
    }

    public OrdemPedido save(OrdemPedido ordemPedido) {
        OrdemPedido savedOrdemPedido = ordemPedidoDAO.save(ordemPedido);
        System.out.println();
        log.info("Nova Ordem de Pedido de id {} salva com sucesso!", savedOrdemPedido.getId());
        return savedOrdemPedido;
    }

    public OrdemPedido update(Integer id, OrdemPedido ordemPedido) {
        OrdemPedido update = ordemPedidoDAO.update(id, ordemPedido);
        System.out.println();
        log.info("Ordem de Pedido de id {} foi atualizada com sucesso!", id);
        return update;
    }

    public void deleteById(Integer id) {
        ordemPedidoDAO.deleteById(id);
        System.out.println();
        log.info("Ordem de Pedido de id {} apagada com sucesso!", id);
    }


    public void efetuarTransferenciaOrdemPedido(OrdemPedido ordemPedido) {
        Doavel target;
        int targetArmazemId;
        int estoqueMaximo;

        if (ordemPedido.getStatus().equals(OrdemPedido.Status.RECUSADO)) {
            var updatedOrdem = ordemPedidoDAO.update(ordemPedido.getId(), ordemPedido);
            log.info("Ordem pedido de id {} atualizada com sucesso!", updatedOrdem.getId());
            return;
        }

        var centroDistribuicao = centroDistribuicaoService.findByIdOrException(ordemPedido.getCentroDistribuicaoId());
        var doacao = centroDistribuicaoService.getDoacaoByCentroDistribuicaoIdAndItem(centroDistribuicao.getId(), ordemPedido.getItem());

        if (ordemPedido.getCentroDistribuicaoEnvioId() != null) {
            target = centroDistribuicaoService.findByIdOrException(ordemPedido.getCentroDistribuicaoEnvioId());
            targetArmazemId = centroDistribuicaoService.getCentroDistribuicaoArmazemId(target.getId());
            estoqueMaximo = CATEGORIA_CENTRO_ESTOQUE_MAXIMO;
        } else {
            target = abrigoService.findByIdOrException(ordemPedido.getAbrigoId());
            targetArmazemId = abrigoService.getAbrigoArmazemId(target.getId());
            estoqueMaximo = CATEGORIA_ABRIGO_ESTOQUE_MAXIMO;
        }

        int totalCategoria = doacaoDAO.totalCategoria(targetArmazemId, ordemPedido.getCategoria());

        if (totalCategoria + ordemPedido.getQuantidade() > estoqueMaximo) {
            ordemPedido.setQuantidade((totalCategoria + ordemPedido.getQuantidade()) - estoqueMaximo);
            System.out.printf("Categoria %s do Abrigo atingiu seu limite máximo. Quantidade de Ordem de Pedido alterado.\n", ordemPedido.getCategoria());
        }

        if (doacao.getQuantidade() < ordemPedido.getQuantidade()) {
            ordemPedido.setQuantidade(doacao.getQuantidade());
        }

        var doacaoParaOAbrigo = createNewDoacaoObject(ordemPedido, doacao);
        doacaoParaOAbrigo.setArmazemId(targetArmazemId);

        doacao.setQuantidade(doacao.getQuantidade() - ordemPedido.getQuantidade());
        doacaoDAO.atualizarDoacao(doacao.getId(), doacao);
        log.info("Doação de id {} atualizada com sucesso! {}", doacao.getId(), doacao);

        doacaoParaOAbrigo = doacaoDAO.inserirDoacao(targetArmazemId, doacaoParaOAbrigo);
        log.info("Doação de id {} inserida com sucesso! {}", doacaoParaOAbrigo.getId(), doacaoParaOAbrigo);

        update(ordemPedido.getId(), ordemPedido);

        if (doacao.getQuantidade() < ordemPedido.getQuantidade()) {
            doacaoDAO.removerDoacao(doacao.getId());
            log.info("Doação de id {} foi apagada com sucesso", doacao.getId());
        }
    }


    private Doacao createNewDoacaoObject(OrdemPedido ordemPedido, Doacao doacao) {
        var doacaoParaOAbrigo = new Doacao();
        doacaoParaOAbrigo.setQuantidade(ordemPedido.getQuantidade());
        doacaoParaOAbrigo.setCategoria(doacao.getCategoria());
        doacaoParaOAbrigo.setItem(doacao.getItem());
        if (doacao.getCategoria().equals(Doacao.Categoria.ROUPA)) {
            doacaoParaOAbrigo.setSexo(doacao.getSexo());
            doacaoParaOAbrigo.setTamanho(doacao.getTamanho());
        }
        return doacaoParaOAbrigo;
    }

}
