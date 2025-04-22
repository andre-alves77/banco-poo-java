package poo.ui;

import poo.banco.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MenuInicial {

    private final Banco banco = new Banco("Mogi International Bank");
    private final Navegador navegador = new Navegador();

    private Cliente atualCliente;
    private Conta atualConta;

    public void iniciar() {
        // Menu principal
        Pagina menuPrincipal = new Pagina("Menu Principal", () -> {
            ArrayList<Pagina> opcoes = new ArrayList<>();
            opcoes.add(new Pagina("Criar Cliente", this::criarCliente));
            opcoes.add(new Pagina("Selecionar Cliente", this::selecionarCliente));
            opcoes.add(new Pagina("Listar Clientes", this::listarClientes));
            opcoes.add(Pagina.SAIR);
            navegador.mostrarMenu(banco.getName(), "Escolha uma opção:", opcoes);
        });

        navegador.irPara(menuPrincipal);
    }

    private void criarCliente() {
        String nome = navegador.pedirInput("Criar Cliente", "Nome do cliente:", String.class);
    
        String tipo;
        do {
            tipo = navegador.pedirInput("Criar Cliente", "Tipo (f) Física ou (j) Jurídica:", String.class);
            if (!tipo.equalsIgnoreCase("f") && !tipo.equalsIgnoreCase("j")) {
                navegador.mostrarMensagem("Erro", "Opção inválida! Digite 'f' para Física ou 'j' para Jurídica.");
            }
        } while (!tipo.equalsIgnoreCase("f") && !tipo.equalsIgnoreCase("j"));
    
        Cliente cliente;
    
        if (tipo.equalsIgnoreCase("f")) {
            String cpf;
            do {
                cpf = navegador.pedirInput("CPF", "Digite o CPF:", String.class);
                if (!Util.isCpf(cpf)) navegador.mostrarMensagem("Erro", "CPF inválido!");
            } while (!Util.isCpf(cpf));
            cliente = new PessoaFisica(nome, cpf);
        } else {
            String cnpj = navegador.pedirInput("CNPJ", "Digite o CNPJ:", String.class);
            cliente = new PessoaJuridica(nome, cnpj);
        }
    
        banco.addCliente(cliente);
        atualCliente = cliente;
        atualConta = null;
    
        navegador.mostrarMensagem("Sucesso", "Cliente criado com sucesso!");
        navegador.voltar();
    }

    private void selecionarCliente() {
            List<Cliente> clientes = banco.getClientes();
            if (clientes.isEmpty()) {
                navegador.mostrarMensagem("Aviso", "Nenhum cliente cadastrado.");
                navegador.voltar();
                return;
            }

            ArrayList<Pagina> opcoes = new ArrayList<>();
            for (Cliente c : clientes) {
                opcoes.add(new Pagina(c.toString(), () -> {
                    atualCliente = c;
                    atualConta = null;
                    navegador.mostrarMensagem("Cliente selecionado", c.getName());

                    Pagina menuConta = new Pagina("Menu Conta", this::selecionarConta);
                    navegador.irPara(menuConta);
                }));
            }
            opcoes.add(Pagina.SAIR);

            navegador.mostrarMenu("Selecionar Cliente", "Escolha um cliente:", opcoes);
        }

    private void listarClientes() {
        StringBuilder lista = new StringBuilder("Clientes:\n");
        for (Cliente c : banco.getClientes()) {
            lista.append(c).append("\n");
        }

        navegador.mostrarMensagem("Lista de Clientes", lista.toString());
        navegador.voltar();
    }

    private void selecionarConta() {
    if (atualCliente == null) {
        navegador.mostrarMensagem("Erro", "Nenhum cliente selecionado.");
        navegador.voltar();
        return;
    }

    ArrayList<Pagina> opcoes = new ArrayList<>();
    opcoes.add(new Pagina("Criar Conta", this::criarConta));
    opcoes.add(new Pagina("Listar Contas", this::listarContas));
    opcoes.add(new Pagina("Selecionar Conta", this::selecionarContaCliente));
    opcoes.add(new Pagina("Voltar", () -> {
        atualCliente = null;
        atualConta = null;
        navegador.voltar(3);
    }));

    navegador.mostrarMenu("Conta", "Escolha uma opção:", opcoes);
}

    private void criarConta() {
        if (atualCliente == null) {
            navegador.mostrarMensagem("Erro", "Nenhum cliente selecionado.");
            navegador.voltar();
            return;
        }

        String tipo = navegador.pedirInput("Tipo de Conta", "Digite o tipo (p) Poupança, (c) Corrente, (i) Investimento:", String.class);
        Conta conta;

        if (tipo.equalsIgnoreCase("p")) {
            conta = new ContaPoupanca(atualCliente);
        } else if (tipo.equalsIgnoreCase("c")) {
            conta = new ContaCorrente(atualCliente);
        } else {
            conta = new ContaInvestimento(atualCliente);
        }

        atualCliente.addConta(conta);
        banco.addConta(conta);
        atualConta = conta;

        navegador.mostrarMensagem("Sucesso", "Conta criada com sucesso!");
        navegador.voltar();
    }

    private void listarContas() {
        if (atualCliente == null) {
            navegador.mostrarMensagem("Erro", "Nenhum cliente selecionado.");
            navegador.voltar();
            return;
        }

        StringBuilder lista = new StringBuilder("Contas:\n");
        for (Conta c : atualCliente.getContas()) {
            lista.append(c).append("\n");
        }

        navegador.mostrarMensagem("Lista de Contas", lista.toString());
        navegador.voltar();
    }

    private void selecionarContaCliente() {
        if (atualCliente == null) {
            navegador.mostrarMensagem("Erro", "Nenhum cliente selecionado.");
            navegador.voltar();
            return;
        }

        List<Conta> contas = atualCliente.getContas();
        if (contas.isEmpty()) {
            navegador.mostrarMensagem("Aviso", "Cliente não possui contas.");
            navegador.voltar();
            return;
        }

        ArrayList<Pagina> opcoes = new ArrayList<>();
        for (Conta c : contas) {
            opcoes.add(new Pagina(c.toString(), () -> {
                atualConta = c;
                navegador.mostrarMensagem("Conta selecionada", c.getId());
                // Menu de operações
                realizarOperacoes();
            }));
        }
        opcoes.add(new Pagina("Voltar", () -> {
            atualConta = null;
            navegador.voltar(3);
        }));


        navegador.mostrarMenu("Selecionar Conta", "Escolha uma conta:", opcoes);
    }

    private void realizarOperacoes() {
        // Menu de operações
        Pagina menuOperacoes = new Pagina("Menu Operações", () -> {
            ArrayList<Pagina> opcoes = new ArrayList<>();
            opcoes.add(new Pagina("Depositar", this::depositar));
            opcoes.add(new Pagina("Sacar", this::sacar));
            opcoes.add(new Pagina("Render", this::render));
            opcoes.add(new Pagina("Voltar", () -> {
                atualConta = null;
                navegador.voltar(3);
            }));
            navegador.mostrarMenu("Operações", "Escolha uma operação:", opcoes);
        });

        navegador.irPara(menuOperacoes);
    }

    private void depositar() {
        if (atualConta == null) {
            navegador.mostrarMensagem("Erro", "Nenhuma conta selecionada.");
            navegador.voltar();
            return;
        }

        double valor = navegador.pedirInput("Depositar", "Valor do depósito:", Double.class);
        atualConta.depositar(valor);
        navegador.mostrarMensagem("Sucesso", "Valor depositado com sucesso.");
        navegador.voltar();
    }

    private void sacar() {
        if (atualConta == null) {
            navegador.mostrarMensagem("Erro", "Nenhuma conta selecionada.");
            navegador.voltar();
            return;
        }

        double valor = navegador.pedirInput("Sacar", "Valor do saque:", Double.class);
        atualConta.sacar(valor);
        navegador.mostrarMensagem("Sucesso", "Valor sacado com sucesso.");
        navegador.voltar();
    }

    private void render() {
        banco.getContas().forEach(c -> {
            if (c instanceof Rendimento) {
                ((Rendimento) c).render();
            }
        });
        navegador.mostrarMensagem("Rendimento", "Rendimentos aplicados nas contas com rendimento.");
        navegador.voltar();
    }
}
