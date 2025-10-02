package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.model.Area;
import br.com.fiap.mottomap.model.Filial;
import br.com.fiap.mottomap.model.PosicaoPatio;
import br.com.fiap.mottomap.model.Usuario;
import br.com.fiap.mottomap.repository.FilialRepository;
import br.com.fiap.mottomap.repository.PosicaoPatioRepository;
import br.com.fiap.mottomap.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilialService {

    private static final Logger log = LoggerFactory.getLogger(FilialService.class);

    @Autowired
    private FilialRepository filialRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PosicaoPatioRepository posicaoPatioRepository;

    @Transactional
    public Filial createFilialAndGenerateSpots(Filial filial) {
        log.info("Iniciando o serviço para criar filial e gerar vagas...");
        Filial savedFilial = filialRepository.save(filial);
        log.info("Filial salva com sucesso. ID: {}", savedFilial.getId());

        List<PosicaoPatio> posicoesParaSalvar = new ArrayList<>();
        int linhas = savedFilial.getNumeroLinha();
        int colunas = savedFilial.getNumeroColuna();
        log.info("Gerando vagas para {} linhas e {} colunas.", linhas, colunas);

        for (int i = 0; i < linhas; i++) {
            char letraLinha = (char) ('A' + i);
            for (int j = 0; j < colunas; j++) {
                String identificacao = String.format("%c%02d", letraLinha, j + 1);

                PosicaoPatio novaPosicao = PosicaoPatio.builder()
                        .identificacao(identificacao)
                        .numeroLinha(i + 1)
                        .numeroColuna(j + 1)
                        .area(Area.PRONTAS) // Usando o 'PRONTAS' que você definiu
                        .ocupado(false)
                        .filial(savedFilial)
                        .build();

                posicoesParaSalvar.add(novaPosicao);
            }
        }

        log.info("{} vagas foram criadas na memória. Salvando no banco de dados...", posicoesParaSalvar.size());
        posicaoPatioRepository.saveAll(posicoesParaSalvar);
        log.info("Vagas salvas com sucesso no banco de dados!");

        return savedFilial;
    }

    @Transactional
    public void delete(Long id) {
        log.info("Iniciando exclusão da filial ID: {}", id);

        // 1. Encontra a filial que será excluída
        Filial filial = filialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Filial não encontrada"));

        // 2. Desvincula os usuários ANTES de apagar a filial
        List<Usuario> usuariosParaDesvincular = filial.getUsuarios();
        if (usuariosParaDesvincular != null && !usuariosParaDesvincular.isEmpty()) {
            log.info("Desvinculando {} usuários da filial.", usuariosParaDesvincular.size());
            for (Usuario usuario : usuariosParaDesvincular) {
                usuario.setFilial(null);
            }
            usuarioRepository.saveAll(usuariosParaDesvincular);
        }

        // 3. Agora, apaga a filial. O 'cascade' cuidará de apagar as motos e vagas.
        log.info("Excluindo a filial e suas dependências (motos e vagas)...");
        filialRepository.delete(filial);
        log.info("Filial ID: {} excluída com sucesso.", id);
    }
}