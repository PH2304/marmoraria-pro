package br.com.marmoraria.view;

import br.com.marmoraria.util.GerenciadorBackup;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GestaoBackupView extends BorderPane {

    private ListView<String> listaBackups;
    private TextArea infoArea;

    public GestaoBackupView() {
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #ecf0f1;");

        // Título
        Label titulo = new Label("💾 Gerenciamento de Backups");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Painel esquerdo - Lista de backups
        VBox painelLista = criarPainelLista();

        // Painel direito - Informações
        VBox painelInfo = criarPainelInfo();

        // Botões
        HBox botoes = criarBotoes();

        // Layout - usar HBox para os painéis
        HBox conteudo = new HBox(20);
        conteudo.getChildren().addAll(painelLista, painelInfo);

        setTop(titulo);
        setCenter(conteudo);
        setBottom(botoes);

        // Carregar lista de backups
        carregarListaBackups();
    }

    private VBox criarPainelLista() {
        VBox box = new VBox(10);
        box.setPrefWidth(350);

        Label label = new Label("📋 Backups Disponíveis");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        listaBackups = new ListView<>();
        listaBackups.setPrefHeight(350);
        listaBackups.setStyle("-fx-background-color: white; -fx-background-radius: 8px;");

        // Evento de seleção
        listaBackups.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mostrarInfoBackup(newVal);
            }
        });

        // Usar VBox.setVgrow em vez de Box
        VBox.setVgrow(listaBackups, Priority.ALWAYS);
        box.getChildren().addAll(label, listaBackups);
        return box;
    }

    private VBox criarPainelInfo() {
        VBox box = new VBox(10);
        box.setPrefWidth(400);

        Label label = new Label("ℹ️ Informações do Backup");
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        infoArea = new TextArea();
        infoArea.setEditable(false);
        infoArea.setPrefHeight(350);
        infoArea.setStyle("-fx-font-family: monospace; -fx-font-size: 12px;");
        infoArea.setWrapText(true);

        box.getChildren().addAll(label, infoArea);
        return box;
    }

    private HBox criarBotoes() {
        HBox box = new HBox(15);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20, 0, 0, 0));

        Button btnCriarBackup = new Button("🔄 Criar Backup Agora");
        btnCriarBackup.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-weight: bold;");
        btnCriarBackup.setOnAction(e -> criarBackup());

        Button btnRestaurar = new Button("📂 Restaurar Backup");
        btnRestaurar.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-weight: bold;");
        btnRestaurar.setOnAction(e -> restaurarBackup());

        Button btnAtualizar = new Button("🔄 Atualizar Lista");
        btnAtualizar.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; -fx-padding: 10 20;");
        btnAtualizar.setOnAction(e -> carregarListaBackups());

        Button btnFechar = new Button("❌ Fechar");
        btnFechar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 10 20;");
        btnFechar.setOnAction(e -> ((Stage) getScene().getWindow()).close());

        box.getChildren().addAll(btnCriarBackup, btnRestaurar, btnAtualizar, btnFechar);
        return box;
    }

    private void carregarListaBackups() {
        listaBackups.getItems().clear();
        List<File> backups = GerenciadorBackup.listarBackups();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        if (backups.isEmpty()) {
            listaBackups.getItems().add("Nenhum backup encontrado");
            infoArea.setText("Nenhum backup disponível.\n\nClique em 'Criar Backup Agora' para criar o primeiro backup.\n\n💡 Dicas:\n• Backups automáticos são feitos a cada 24 horas\n• São mantidos os últimos 30 backups\n• Restaurar um backup criará um backup dos dados atuais primeiro");
        } else {
            for (File backup : backups) {
                String data = sdf.format(new Date(backup.lastModified()));
                String tamanho = String.format("%.2f KB", backup.length() / 1024.0);
                listaBackups.getItems().add(String.format("%s | %s | %s", backup.getName(), data, tamanho));
            }

            // Mostrar informações do último backup
            File ultimo = backups.get(0);
            long dias = (System.currentTimeMillis() - ultimo.lastModified()) / (1000 * 60 * 60 * 24);
            infoArea.setText(String.format(
                    "📊 INFORMAÇÕES\n\n" +
                            "Último backup: %s\n" +
                            "Data: %s\n" +
                            "Tamanho: %.2f KB\n" +
                            "Dias desde último backup: %d\n\n" +
                            "💡 Dicas:\n" +
                            "• Backups automáticos são feitos a cada 24 horas\n" +
                            "• São mantidos os últimos 30 backups\n" +
                            "• Restaurar um backup criará um backup dos dados atuais primeiro",
                    ultimo.getName(),
                    sdf.format(new Date(ultimo.lastModified())),
                    ultimo.length() / 1024.0,
                    dias
            ));
        }
    }

    private void mostrarInfoBackup(String nomeBackup) {
        List<File> backups = GerenciadorBackup.listarBackups();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        for (File backup : backups) {
            if (nomeBackup.startsWith(backup.getName())) {
                long dias = (System.currentTimeMillis() - backup.lastModified()) / (1000 * 60 * 60 * 24);

                String info = String.format(
                        "📁 NOME: %s\n\n" +
                                "📅 DATA: %s\n\n" +
                                "📏 TAMANHO: %.2f KB\n\n" +
                                "⏱️ DIAS DESDE CRIAÇÃO: %d\n\n" +
                                "📍 LOCAL: %s\n\n" +
                                "═══════════════════════════════════════\n\n" +
                                "⚠️ ATENÇÃO:\n\n" +
                                "• Restaurar este backup substituirá TODOS os orçamentos atuais\n" +
                                "• Um backup dos dados atuais será criado automaticamente antes da restauração\n" +
                                "• Esta ação NÃO pode ser desfeita\n\n" +
                                "✅ Recomenda-se criar um backup manual antes de restaurar",
                        backup.getName(),
                        sdf.format(new Date(backup.lastModified())),
                        backup.length() / 1024.0,
                        dias,
                        backup.getAbsolutePath()
                );

                infoArea.setText(info);
                break;
            }
        }
    }

    private void criarBackup() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Criar Backup");
        confirm.setHeaderText("Confirmar criação de backup");
        confirm.setContentText("Deseja criar um backup dos orçamentos atuais?");

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            boolean sucesso = GerenciadorBackup.criarBackup();

            if (sucesso) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText(null);
                alert.setContentText("✅ Backup criado com sucesso!\n\nOs arquivos foram salvos na pasta 'backups'.");
                alert.showAndWait();

                carregarListaBackups();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText(null);
                alert.setContentText("❌ Erro ao criar backup.\n\nVerifique se a pasta 'orcamentos' existe e tem permissão de escrita.");
                alert.showAndWait();
            }
        }
    }

    private void restaurarBackup() {
        String selecionado = listaBackups.getSelectionModel().getSelectedItem();
        if (selecionado == null || selecionado.equals("Nenhum backup encontrado")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("Selecione um backup para restaurar.");
            alert.showAndWait();
            return;
        }

        // Encontrar o arquivo selecionado
        List<File> backups = GerenciadorBackup.listarBackups();
        File backupSelecionado = null;

        for (File backup : backups) {
            if (selecionado.startsWith(backup.getName())) {
                backupSelecionado = backup;
                break;
            }
        }

        if (backupSelecionado == null) {
            mostrarErro("Erro ao localizar o arquivo de backup.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Restaurar Backup");
        confirm.setHeaderText("⚠️ ATENÇÃO! Esta ação é irreversível.");
        confirm.setContentText(
                "Você está prestes a restaurar o backup:\n\n" +
                        backupSelecionado.getName() + "\n\n" +
                        "📌 O que vai acontecer:\n" +
                        "• TODOS os orçamentos atuais serão substituídos\n" +
                        "• Um backup dos dados atuais será criado automaticamente\n" +
                        "• Os orçamentos restaurados estarão disponíveis imediatamente\n\n" +
                        "Tem certeza que deseja continuar?"
        );

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // Criar backup antes de restaurar
            GerenciadorBackup.criarBackup();

            boolean sucesso = GerenciadorBackup.restaurarBackup(backupSelecionado);

            if (sucesso) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText(null);
                alert.setContentText("✅ Backup restaurado com sucesso!\n\nOs orçamentos foram recuperados.\n\nUm backup dos dados anteriores foi criado automaticamente.");
                alert.showAndWait();

                carregarListaBackups();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText(null);
                alert.setContentText("❌ Erro ao restaurar backup.\n\nVerifique se o arquivo de backup não está corrompido.");
                alert.showAndWait();
            }
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}