import javax.swing.*;
import java.util.ArrayList;

public class SistemaBancario {
    static ArrayList<Cliente> clientes = new ArrayList<>();
    static ArrayList<ContaBancaria> contas = new ArrayList<>();

    public static void main(String[] args) {
        String opcao;
        do {
            opcao = JOptionPane.showInputDialog(
                "1. Cadastrar Cliente\n" +
                "2. Cadastrar Conta\n" +
                "3. Depositar\n" +
                "4. Sacar\n" +
                "5. Listar Contas\n" +
                "6. Remover Cliente\n" +
                "0. Sair"
            );

            switch (opcao) {
                case "1":
                    cadastrarCliente();
                    break;
                case "2":
                    cadastrarConta();
                    break;
                case "3":
                    depositar();
                    break;
                case "4":
                    sacar();
                    break;
                case "5":
                    listarContas();
                    break;
                case "6":
                    removerCliente();
                    break;
            }

        } while (!opcao.equals("0"));
    }

    static void cadastrarCliente() {
        String tipo = JOptionPane.showInputDialog("Tipo (F para física, J para jurídica):");
        String nome = JOptionPane.showInputDialog("Nome:");
        String documento = JOptionPane.showInputDialog(tipo.equalsIgnoreCase("F") ? "CPF:" : "CNPJ:");
        Cliente cliente = tipo.equalsIgnoreCase("F") ? new PessoaFisica(nome, documento) : new PessoaJuridica(nome, documento);
        clientes.add(cliente);
    }

    static void cadastrarConta() {
        String nomeCliente = JOptionPane.showInputDialog("Nome do cliente:");
        Cliente cliente = buscarCliente(nomeCliente);
        if (cliente == null) {
            JOptionPane.showMessageDialog(null, "Cliente não encontrado.");
            return;
        }

        String tipoConta = JOptionPane.showInputDialog("Tipo (C - Corrente, P - Poupança, R - Rendimento):");
        ContaBancaria conta;
        if (tipoConta.equalsIgnoreCase("C")) {
            String temLimite = JOptionPane.showInputDialog("Deseja adicionar limite? (S/N)");
            if (temLimite.equalsIgnoreCase("S")) {
                double limite = Double.parseDouble(JOptionPane.showInputDialog("Limite:"));
                conta = new ContaCorrente(cliente, limite);
            } else {
                conta = new ContaCorrente(cliente);
            }
        } else if (tipoConta.equalsIgnoreCase("P")) {
            conta = new ContaPoupanca(cliente);
        } else {
            conta = new ContaRendimento(cliente);
        }

        contas.add(conta);
    }

    static void depositar() {
        String nome = JOptionPane.showInputDialog("Nome do cliente:");
        ContaBancaria conta = buscarConta(nome);
        if (conta != null) {
            double valor = Double.parseDouble(JOptionPane.showInputDialog("Valor do depósito:"));
            conta.depositar(valor);
        }
    }

    static void sacar() {
        String nome = JOptionPane.showInputDialog("Nome do cliente:");
        ContaBancaria conta = buscarConta(nome);
        if (conta != null) {
            double valor = Double.parseDouble(JOptionPane.showInputDialog("Valor do saque:"));
            conta.sacar(valor);
        }
    }

    static void listarContas() {
        contas.forEach(c -> c.exibirInformacoes());
    }

    static void removerCliente() {
        String nome = JOptionPane.showInputDialog("Nome do cliente a remover:");
        Cliente cliente = buscarCliente(nome);
        if (cliente != null) {
            contas.removeIf(c -> c.getCliente().equals(cliente));
            clientes.remove(cliente);
            JOptionPane.showMessageDialog(null, "Cliente removido com sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Cliente não encontrado.");
        }
    }

    static Cliente buscarCliente(String nome) {
        for (Cliente c : clientes) {
            if (c.nome.equalsIgnoreCase(nome)) return c;
        }
        return null;
    }

    static ContaBancaria buscarConta(String nomeCliente) {
        for (ContaBancaria conta : contas) {
            if (conta.getCliente().nome.equalsIgnoreCase(nomeCliente)) {
                return conta;
            }
        }
        JOptionPane.showMessageDialog(null, "Conta não encontrada.");
        return null;
    }
}
