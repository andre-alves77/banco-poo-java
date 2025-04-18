abstract class Cliente {
    String nome;
    String cpfOuCnpj;

    public Cliente(String nome, String cpfOuCnpj) {
        this.nome = nome;
        this.cpfOuCnpj = cpfOuCnpj;
    }

    public abstract String getTipo();
}

class PessoaFisica extends Cliente {
    public PessoaFisica(String nome, String cpf) {
        super(nome, cpf);
    }

    public String getTipo() {
        return "Pessoa Física";
    }
}

class PessoaJuridica extends Cliente {
    public PessoaJuridica(String nome, String cnpj) {
        super(nome, cnpj);
    }

    public String getTipo() {
        return "Pessoa Jurídica";
    }
}
