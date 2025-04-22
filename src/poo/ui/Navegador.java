package poo.ui;
import javax.swing.*;
import java.util.*;

    public class Navegador {
        private final Stack<Pagina> historico = new Stack<>();

        public void irPara(Pagina pagina) {
            irPara(pagina, false);
        }

        
        // se caso não quiser voltar automaticamente
        public void irPara(Pagina pagina, boolean manterNaTela) {
            if (pagina != null && pagina.getAcao() != null) {
                historico.push(pagina);
                if (manterNaTela) {
                    pagina.getAcao().run();
                } else {
                    executarEAvoltar(pagina.getAcao());
                }
            } else {
                mostrarMensagem("Erro", "Página inválida ou sem ação.");
            }
        }
        

        public void voltar() {
            if (historico.size() > 1) {
                historico.pop();
                Pagina anterior = historico.peek();
                executarEAvoltar(anterior.getAcao());
            } else {
                encerrarPrograma();
            }
        }

        public String escolherOpcao(String titulo, String mensagem, ArrayList<String> opcoes) {
            String[] opcoesArray = opcoes.toArray(new String[0]);
        
            String escolha = (String) JOptionPane.showInputDialog(
                    null,
                    mensagem,
                    titulo,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoesArray,
                    opcoesArray.length > 0 ? opcoesArray[0] : null
            );
        
            if (escolha == null) {
                this.voltar();
                return null;
            }
        
            return escolha;
        }

        public void mostrarMenu(String titulo, String mensagem, ArrayList<Pagina> opcoes) {
            int tamanho = opcoes.size();
            String[] titulos = new String[tamanho];

            for(int i = 0; i < tamanho; i++){
                titulos[i] = opcoes.get(i).getTitulo();
            }

            int escolha = JOptionPane.showOptionDialog(
                    null,
                    mensagem,
                    titulo,
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    titulos,
                    titulos[0]
            );

            if (escolha >= 0 && escolha < tamanho) {

                Pagina selecionada = opcoes.get(escolha);
                if (selecionada == Pagina.SAIR) {
                    voltar();
                } else {
                    irPara(selecionada);
                }
            }
        }

        // alternativa a sobrecarga de metodo
        public <T> T pedirInput(String titulo, String mensagem, Class<T> tipo) {
            String input = JOptionPane.showInputDialog(null, mensagem, titulo, JOptionPane.QUESTION_MESSAGE);

            if (input == null) {
                voltar();
                return null;
            }

            if (input.trim().isEmpty()) {
                mostrarMensagem(titulo, "Erro: Preencha o campo");
                return pedirInput(titulo, mensagem, tipo);
            }

            try {
                if (tipo == Integer.class) return (T) Integer.valueOf(input);
                if (tipo == Double.class) return (T) Double.valueOf(input);
                if (tipo == String.class) return (T) input;
                if (tipo == Boolean.class) {
                    input = input.trim().toLowerCase();
                    if (input.equals("sim") || input.equals("s") || input.equals("1")) return (T) Boolean.TRUE;
                    if (input.equals("nao") || input.equals("n") || input.equals("0") || input.equals("não")) return (T) Boolean.FALSE;
                    mostrarMensagem(titulo, "Digite 'sim' ou 'não'");
                    return pedirInput(titulo, mensagem, tipo);
                }
            } catch (Exception e) {
                mostrarMensagem(titulo, "Erro: Valor inesperado. Tente novamente.");
                return pedirInput(titulo, mensagem, tipo);
            }

            mostrarMensagem("Erro: " + tipo, "Tipo não suportado.");
            return null;
        }

        public void mostrarMensagem(String titulo, String mensagem) {
            JOptionPane.showMessageDialog(null, mensagem, titulo, JOptionPane.INFORMATION_MESSAGE);
        }

        public void executarEAvoltar(Runnable acao) {
            try {
                acao.run();
            } catch (Exception e) {
                mostrarMensagem("Erro", e.getMessage());
            } finally {
                voltar();
            }
        }

        private  <T> T preencherComInputs() {
            //metodo que preenche que cria o formulario automaticamente a partir da classe
            return null;
        }

        private void encerrarPrograma() {
            mostrarMensagem("Acabouuuu", "Tchau senhor das areias. Na proxima vez eu implmento os submenus");
            System.exit(0);
        }
    }
