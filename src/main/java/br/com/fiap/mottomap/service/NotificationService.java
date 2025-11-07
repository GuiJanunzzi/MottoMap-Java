package br.com.fiap.mottomap.service;

import br.com.fiap.mottomap.model.Usuario;
import br.com.fiap.mottomap.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final String EXPO_PUSH_URL = "https://api.expo.dev/v2/push/send";

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void sendNotificationToAll(String title, String body) {
        log.info("Buscando tokens de push para enviar notificação...");

        // 1. Busca todos os usuários que possuem um token de push
        List<String> pushTokens = usuarioRepository.findAll().stream()
                .map(Usuario::getPushToken)
                .filter(token -> token != null && !token.isEmpty())
                .collect(Collectors.toList());

        if (pushTokens.isEmpty()) {
            log.warn("Nenhum token de push encontrado. Nenhuma notificação será enviada.");
            return;
        }

        log.info("Enviando notificação para {} dispositivos.", pushTokens.size());

        // 2. Monta o corpo da requisição para a API do Expo
        // A API do Expo espera um JSON no formato: { "to": ["token1", "token2"], "title": "...", "body": "..." }
        Map<String, Object> requestBody = Map.of(
                "to", pushTokens,
                "title", title,
                "body", body
        );

        // 3. Envia a requisição
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.postForEntity(EXPO_PUSH_URL, requestBody, String.class);
            log.info("Notificações enviadas para a API do Expo com sucesso.");
        } catch (Exception e) {
            log.error("Erro ao enviar notificações para a API do Expo: {}", e.getMessage());
        }
    }
}