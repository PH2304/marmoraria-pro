package br.com.marmoraria;

public class Launcher {
    public static void main(String[] args) {
        System.out.println("=".repeat(50));
        System.out.println("🚀 LAUNCHER MARMORARIA PRO - WORKAROUND");
        System.out.println("=".repeat(50));

        try {
            // Inicia a aplicação JavaFX
            System.out.println("▶️  Iniciando aplicação JavaFX (interface manual)...");
            com.marmoraria.Main.main(args);

        } catch (Exception e) {
            System.err.println("❌ ERRO CRÍTICO:");
            e.printStackTrace();

            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Erro: " + e.getMessage() + "\n\n" +
                            "Execute via Maven: mvn clean javafx:run",
                    "Erro",
                    javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }
}