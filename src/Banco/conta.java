interface Conta {
    void depositar(double valor);
    void sacar(double valor);
    void exibirInformacoes();
}

abstract class ContaBancaria implements Conta {
    protected double saldo;
    protected Cliente cliente;

    public ContaBancaria(Cliente cliente) {
        this.cliente = cliente;
        this.saldo = 0;
    }

    public void depositar(double valor) {
        saldo += valor;
    }

    public double getSaldo() {
        return saldo;
    }

    public Cliente getCliente() {
        return cliente;
    }
}

class ContaCorrente extends ContaBancaria {
    private double limite;

    public ContaCorrente(Cliente cliente) {
        this(cliente, 0);
    }

    public ContaCorrente(Cliente cliente, double limite) {
        super(cliente);
        this.limite = limite;
    }

    public void sacar(double valor) {
        if (saldo + limite >= valor) {
            saldo -= valor;
        } else {
            JOptionPane.showMessageDialog(null, "Saldo insuficiente!");
        }
    }

    public void exibirInformacoes() {
        JOptionPane.showMessageDialog(null, "Conta Corrente - Cliente: " + cliente.nome +
                "\nSaldo: " + saldo + "\nLimite: " + limite);
    }
}

class ContaPoupanca extends ContaBancaria {
    public ContaPoupanca(Cliente cliente) {
        super(cliente);
    }

    public void sacar(double valor) {
        if (saldo >= valor) {
            saldo -= valor;
        } else {
            JOptionPane.showMessageDialog(null, "Saldo insuficiente!");
        }
    }

    public void exibirInformacoes() {
        JOptionPane.showMessageDialog(null, "Conta Poupança - Cliente: " + cliente.nome +
                "\nSaldo: " + saldo);
    }
}

class ContaRendimento extends ContaBancaria {
    public ContaRendimento(Cliente cliente) {
        super(cliente);
    }

    public void sacar(double valor) {
        JOptionPane.showMessageDialog(null, "Não é possível sacar de uma conta rendimento.");
    }

    public void exibirInformacoes() {
        JOptionPane.showMessageDialog(null, "Conta Rendimento - Cliente: " + cliente.nome +
                "\nSaldo: " + saldo);
    }
}
