package br.com.fiap.mottomap.config;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.mottomap.model.Area;
import br.com.fiap.mottomap.model.CargoUsuario;
import br.com.fiap.mottomap.model.Filial;
import br.com.fiap.mottomap.model.ModeloMoto;
import br.com.fiap.mottomap.model.Moto;
import br.com.fiap.mottomap.model.PosicaoPatio;
import br.com.fiap.mottomap.model.Problema;
import br.com.fiap.mottomap.model.StatusMoto;
import br.com.fiap.mottomap.model.TipoProblema;
import br.com.fiap.mottomap.model.Usuario;
import br.com.fiap.mottomap.repository.FilialRepository;
import br.com.fiap.mottomap.repository.MotoRepository;
import br.com.fiap.mottomap.repository.PosicaoPatioRepository;
import br.com.fiap.mottomap.repository.ProblemaRepository;
import br.com.fiap.mottomap.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;

@Configuration
public class DatabaseSeeder {
    
    @Autowired
    FilialRepository filialRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PosicaoPatioRepository posicaoPatioRepository;

    @Autowired
    MotoRepository motoRepository;

    @Autowired
    ProblemaRepository problemaRepository;

    @PostConstruct
    public void init(){
        var filiais = List.of(
            Filial.builder()
                .nome("Mottu São Paulo")
                .endereco("Av. Paulista, 1000")
                .cidade("São Paulo")
                .siglaEstado("SP")
                .numeroColuna(10)
                .numeroLinha(5)
                .capacidadeMaxima(150)
                .build(),

            Filial.builder()
                .nome("Mottu Rio de Janeiro")
                .endereco("Av. Atlântica, 500")
                .cidade("Rio de Janeiro")
                .siglaEstado("RJ")
                .numeroColuna(8)
                .numeroLinha(5)
                .capacidadeMaxima(120)
                .build(),

            Filial.builder()
                .nome("Mottu Belo Horizonte")
                .endereco("Av. Afonso Pena, 2000")
                .cidade("Belo Horizonte")
                .siglaEstado("MG")
                .numeroColuna(6)
                .numeroLinha(4)
                .capacidadeMaxima(80)
                .build(),

            Filial.builder()
                .nome("Mottu Porto Alegre")
                .endereco("Rua da Praia, 300")
                .cidade("Porto Alegre")
                .siglaEstado("RS")
                .numeroColuna(5)
                .numeroLinha(3)
                .capacidadeMaxima(60)
                .build(),

            Filial.builder()
                .nome("Mottu Brasília")
                .endereco("Setor Comercial Sul")
                .cidade("Brasília")
                .siglaEstado("DF")
                .numeroColuna(7)
                .numeroLinha(4)
                .capacidadeMaxima(100)
                .build()
        );

        filialRepository.saveAll(filiais);

        var usuarios = List.of(
            Usuario.builder()
                .nome("Cauã Aragão")
                .email("aragao@mottomap.com")
                .senha("caua12345")
                .cargoUsuario(CargoUsuario.COL_MECANICO)
                .filial(filiais.get(0))
                .build(),

            Usuario.builder()
                .nome("Gustavo Oliveira")
                .email("gustavo@mottomap.com")
                .senha("gustavo12345")
                .cargoUsuario(CargoUsuario.ADM_GERAL)
                .filial(filiais.get(1))
                .build(),

            Usuario.builder()
                .nome("Pedro Felix")
                .email("p.felix@mottomap.com")
                .senha("pedrito12345")
                .cargoUsuario(CargoUsuario.COL_PATIO)
                .filial(filiais.get(2))
                .build(),

            Usuario.builder()
                .nome("Pedro Barbosa")
                .email("p.barbosa@mottomap.com")
                .senha("barbosa12345")
                .cargoUsuario(CargoUsuario.ADM_LOCAL)
                .filial(filiais.get(3))
                .build(),

            Usuario.builder()
                .nome("Mateus Estevam")
                .email("mafeus@mottomap.com")
                .senha("mateus12345")
                .cargoUsuario(CargoUsuario.COL_MECANICO)
                .filial(filiais.get(4))
                .build()
        );

        usuarioRepository.saveAll(usuarios);

        var posicoes = List.of(
            PosicaoPatio.builder()
                .identificacao("A1")
                .numeroLinha(1)
                .numeroColuna(1)
                .area(Area.MINHA_MOTTU)
                .ocupado(true)
                .filial(filiais.get(0))
                .build(),

            PosicaoPatio.builder()
                .identificacao("B1")
                .numeroLinha(2)
                .numeroColuna(1)
                .area(Area.PRONTAS)
                .ocupado(false)
                .filial(filiais.get(0))
                .build(),

            PosicaoPatio.builder()
                .identificacao("C1")
                .numeroLinha(3)
                .numeroColuna(1)
                .area(Area.PROBLEMAS_SIMPLES)
                .ocupado(true)
                .filial(filiais.get(0))
                .build(),

            PosicaoPatio.builder()
                .identificacao("D2")
                .numeroLinha(4)
                .numeroColuna(2)
                .area(Area.PROBLEMAS_GRAVES)
                .ocupado(false)
                .filial(filiais.get(1))
                .build(),

            PosicaoPatio.builder()
                .identificacao("E3")
                .numeroLinha(5)
                .numeroColuna(3)
                .area(Area.IRRECUPERAVEIS)
                .ocupado(false)
                .filial(filiais.get(1))
                .build()
        );

        posicaoPatioRepository.saveAll(posicoes);

        var motos = List.of(
            Moto.builder()
                .placa("QRA2J58")
                .chassi("9C2KC0810KR302457")
                .modeloMoto(ModeloMoto.POP_110I)
                .ano(2024)
                .statusMoto(StatusMoto.ATIVA)
                .filial(filiais.get(0))
                .build(),

            Moto.builder()
                .placa("DVL9B31")
                .chassi("93HPCF520PJ000879")
                .modeloMoto(ModeloMoto.MOTTU_SPORT)
                .ano(2023)
                .statusMoto(StatusMoto.INATIVA)
                .filial(filiais.get(1))
                .build(),

            Moto.builder()
                .placa("GRK7H02")
                .chassi("9CDZD1500MM001234")
                .modeloMoto(ModeloMoto.MOTTU_SPORT_ESD)
                .ano(2025)
                .statusMoto(StatusMoto.ATIVA)
                .filial(filiais.get(2))
                .build(),

            Moto.builder()
                .placa("JKU3C77")
                .chassi("3C3B2F47XKT900321")
                .modeloMoto(ModeloMoto.MOTTU_E_MAX)
                .ano(2025)
                .statusMoto(StatusMoto.ATIVA)
                .filial(filiais.get(3))
                .build(),

            Moto.builder()
                .placa("LZV4M66")
                .chassi("9BWPX1100N1003456")
                .modeloMoto(ModeloMoto.POP_110I)
                .ano(2022)
                .statusMoto(StatusMoto.INATIVA)
                .filial(filiais.get(4))
                .build()
        );

        motoRepository.saveAll(motos);

        var problemas = List.of(
            Problema.builder()
                .tipoProblema(TipoProblema.CARROCERIA)
                .descricao("Retrovisor quebrado")
                .dataRegistro(LocalDate.parse("2025-04-27" , DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .resolvido(true)
                .moto(motos.get(0))
                .usuario(usuarios.get(0))
                .build(),

            Problema.builder()
                .tipoProblema(TipoProblema.MECANICO)
                .descricao("Problema na suspensão dianteira")
                .dataRegistro(LocalDate.parse("2025-05-03", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .resolvido(false)
                .moto(motos.get(1))
                .usuario(usuarios.get(1))
                .build(),
            
            Problema.builder()
                .tipoProblema(TipoProblema.LEGAL)
                .descricao("Placa faltando")
                .dataRegistro(LocalDate.parse("2025-05-09", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .resolvido(false)
                .moto(motos.get(1))
                .usuario(usuarios.get(2))
                .build(),
            
            Problema.builder()
                .tipoProblema(TipoProblema.CARROCERIA)
                .descricao("Amassado na lateral esquerda")
                .dataRegistro(LocalDate.parse("2025-05-13", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .resolvido(false)
                .moto(motos.get(3))
                .usuario(usuarios.get(3))
                .build(),
                
            Problema.builder()
                .tipoProblema(TipoProblema.MECANICO)
                .descricao("Vazamento de óleo no motor")
                .dataRegistro(LocalDate.parse("2025-05-17", DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .resolvido(false)
                .moto(motos.get(4))
                .usuario(usuarios.get(4))
                .build()
        );

        problemaRepository.saveAll(problemas);
    }
}
