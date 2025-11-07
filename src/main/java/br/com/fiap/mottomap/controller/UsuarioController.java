package br.com.fiap.mottomap.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import br.com.fiap.mottomap.dto.LoginRequest;
import br.com.fiap.mottomap.dto.PushTokenRequest;
import br.com.fiap.mottomap.dto.Token;
import br.com.fiap.mottomap.dto.UsuarioRequestDto;
import br.com.fiap.mottomap.dto.UsuarioResponse;
import br.com.fiap.mottomap.model.Filial;
import br.com.fiap.mottomap.model.Usuario;
import br.com.fiap.mottomap.repository.FilialRepository;
import br.com.fiap.mottomap.repository.UsuarioRepository;
import br.com.fiap.mottomap.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
@CrossOrigin
@Tag(name = "Autenticação")
public class UsuarioController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private FilialRepository filialRepository;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/registrar")
    @Operation(summary = "Registrar um novo usuário")
    public ResponseEntity<UsuarioResponse> create(@RequestBody @Valid UsuarioRequestDto dto) {
        log.info("Registrando um novo usuário");

        Filial filial = filialRepository.findById(dto.getFilial())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Filial não encontrada"));

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha())) // Criptografar a senha
                .cargoUsuario(dto.getCargoUsuario())
                .filial(filial)
                .build();

        repository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.fromUsuario(usuario));
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário e obter token")
    public ResponseEntity<Token> login(@RequestBody @Valid LoginRequest loginRequest) {
        log.info("Tentativa de login para o usuário: " + loginRequest.email());

        Authentication auth = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.senha());
        auth = authManager.authenticate(auth);

        Usuario usuario = repository.findByEmail(auth.getName()).get();

        String token = tokenService.generateToken(usuario);

        return ResponseEntity.ok(new Token(token, "JWT", "Bearer"));
    }

    @PostMapping("/register-token")
    @Operation(summary = "Registra o token de push do usuário logado")
    public ResponseEntity<Void> registerPushToken(@RequestBody @Valid PushTokenRequest pushTokenRequest) {
        // Pega o email do usuário logado (pelo token JWT)
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        
        Usuario usuario = repository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        
        usuario.setPushToken(pushTokenRequest.token());
        repository.save(usuario);
        
        return ResponseEntity.ok().build();
    }
}
