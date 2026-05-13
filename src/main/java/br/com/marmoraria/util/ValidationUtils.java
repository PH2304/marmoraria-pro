package br.com.marmoraria.util;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Utilitário de validação centralizado.
 *
 * @author Marmoraria Pro Team
 * @version 3.0.0
 */
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$");

    private static final Pattern CNPJ_PATTERN =
            Pattern.compile("^\\d{2}\\.?\\d{3}\\.?\\d{3}/?\\d{4}-?\\d{2}$");

    /**
     * Valida se uma string não está vazia
     */
    public static Optional<String> validarNaoVazio(String valor, String nomeCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            return Optional.of(nomeCampo + " é obrigatório");
        }
        return Optional.empty();
    }

    /**
     * Valida se é um número positivo
     */
    public static Optional<String> validarNumeroPositivo(String valor, String nomeCampo) {
        try {
            double num = Double.parseDouble(valor.replace(",", "."));
            if (num <= 0) {
                return Optional.of(nomeCampo + " deve ser maior que zero");
            }
            return Optional.empty();
        } catch (NumberFormatException e) {
            return Optional.of(nomeCampo + " deve ser um número válido");
        }
    }

    /**
     * Valida se é um inteiro positivo
     */
    public static Optional<String> validarInteiroPositivo(String valor, String nomeCampo) {
        try {
            int num = Integer.parseInt(valor.trim());
            if (num < 0) {
                return Optional.of(nomeCampo + " deve ser um número positivo");
            }
            return Optional.empty();
        } catch (NumberFormatException e) {
            return Optional.of(nomeCampo + " deve ser um número inteiro");
        }
    }

    /**
     * Valida formato de email
     */
    public static Optional<String> validarEmail(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return Optional.empty(); // Email é opcional
        }
        if (!EMAIL_PATTERN.matcher(valor).matches()) {
            return Optional.of("Email inválido");
        }
        return Optional.empty();
    }

    /**
     * Valida formato de telefone
     */
    public static Optional<String> validarTelefone(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return Optional.empty(); // Telefone é opcional
        }
        String limpo = valor.replaceAll("[^0-9]", "");
        if (limpo.length() < 10 || limpo.length() > 11) {
            return Optional.of("Telefone deve ter 10 ou 11 dígitos");
        }
        return Optional.empty();
    }

    /**
     * Valida medidas (largura/comprimento em mm)
     */
    public static Optional<String> validarMedida(String valor, String nomeCampo) {
        try {
            double medida = Double.parseDouble(valor.replace(",", "."));
            if (medida <= 0) {
                return Optional.of(nomeCampo + " deve ser maior que zero");
            }
            if (medida > 10000) {
                return Optional.of(nomeCampo + " muito grande (máx. 10.000mm)");
            }
            return Optional.empty();
        } catch (NumberFormatException e) {
            return Optional.of(nomeCampo + " inválido");
        }
    }

    /**
     * Valida percentual (0-100)
     */
    public static Optional<String> validarPercentual(String valor, String nomeCampo) {
        try {
            double pct = Double.parseDouble(valor.replace(",", "."));
            if (pct < 0 || pct > 100) {
                return Optional.of(nomeCampo + " deve estar entre 0 e 100");
            }
            return Optional.empty();
        } catch (NumberFormatException e) {
            return Optional.of(nomeCampo + " inválido");
        }
    }

    /**
     * Valida se um valor está em uma faixa
     */
    public static Optional<String> validarFaixa(double valor, double min, double max, String nomeCampo) {
        if (valor < min || valor > max) {
            return Optional.of(String.format("%s deve estar entre %.2f e %.2f", nomeCampo, min, max));
        }
        return Optional.empty();
    }

    /**
     * Agrupa múltiplas validações
     */
    @SafeVarargs
    public static Optional<String> validarTodos(Optional<String>... validacoes) {
        for (Optional<String> validacao : validacoes) {
            if (validacao.isPresent()) {
                return validacao;
            }
        }
        return Optional.empty();
    }

    /**
     * Formata mensagens de erro
     */
    public static String formatarErros(java.util.List<String> erros) {
        StringBuilder sb = new StringBuilder();
        sb.append("⚠️ Corrija os seguintes erros:\n\n");
        for (int i = 0; i < erros.size(); i++) {
            sb.append(String.format("%d. %s\n", i + 1, erros.get(i)));
        }
        return sb.toString();
    }
}
