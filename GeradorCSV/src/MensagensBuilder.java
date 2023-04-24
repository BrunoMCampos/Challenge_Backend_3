import java.util.*;

public class MensagensBuilder {

    public static String exibirMensagem(String mensagemParaEscolhaDeOpcoes, List<String> opcoes) {
        Scanner scanner = new Scanner(System.in);
        String opcoesGeradas = gerarOpcoes(opcoes);

        mensagemParaEscolhaDeOpcoes = mensagemParaEscolhaDeOpcoes.toUpperCase();

        int opcaoSelecionada = 0;

        while(opcaoSelecionada == 0){
            try{
                System.out.println(mensagemParaEscolhaDeOpcoes + " " + opcoesGeradas);
                opcaoSelecionada = scanner.nextInt();
            } catch (InputMismatchException | IllegalStateException ex){
                System.out.println("Valor selecionado inválido, favor digitar apenas o número referente a opção desejada. " + opcoesGeradas);
                scanner.next();
            }
        }

        while (opcaoSelecionada > opcoes.size() || opcaoSelecionada < 1) {
            System.out.println("Valor selecionado inválido, favor digitar apenas o número referente a opção desejada. " + opcoesGeradas);
            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.println(mensagemParaEscolhaDeOpcoes + " " + opcoesGeradas);
            try{
                opcaoSelecionada = scanner.nextInt();
            } catch (InputMismatchException | IllegalStateException ex){
                scanner.next();
            }
        }
        System.out.println("Opção escolhida: " + opcoes.get(opcaoSelecionada - 1).toUpperCase());
        System.out.println("--------------------------------------------------------------------------------------------------");
        return opcoes.get(opcaoSelecionada - 1).toUpperCase();
    }

    public static int exibirMensagem(String mensagemParaEscolhaDeNumero, int valorMinimo, int valorMaximo, int exemploValido){
        Scanner scanner = new Scanner(System.in);

        mensagemParaEscolhaDeNumero = mensagemParaEscolhaDeNumero.toUpperCase();
        String exemploValidoString = "[Ex.: " + exemploValido + "]";
        String valoresMaxEMinString = "[Min.: " + valorMinimo + " - Max.: " + valorMaximo + "]";

        int valorSelecionado = -1;


        while(valorSelecionado == -1){
            try{
                System.out.println(mensagemParaEscolhaDeNumero + " " + exemploValidoString + " " + valoresMaxEMinString);
                valorSelecionado = scanner.nextInt();
            } catch (InputMismatchException | IllegalStateException ex){
                System.out.println("Valor selecionado inválido, favor digitar um valor válido conforme exemplo fornecido. ".toUpperCase() + exemploValidoString + valoresMaxEMinString);
                scanner.next();
            }
        }

        while (valorSelecionado > valorMaximo || valorSelecionado < valorMinimo) {
            System.out.println("Valor selecionado inválido, favor digitar um valor válido conforme exemplo fornecido. ".toUpperCase() + exemploValidoString + valoresMaxEMinString);
            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.println(mensagemParaEscolhaDeNumero + " " + exemploValidoString);
            try{
                valorSelecionado = scanner.nextInt();
            } catch (InputMismatchException | IllegalStateException ex){
                scanner.next();
            }
        }
        System.out.println("Valor selecionado: " + valorSelecionado);
        System.out.println("--------------------------------------------------------------------------------------------------");
        return valorSelecionado;
    }

    public static Boolean exibirMensagemDeSimOuNao(String mensagem) {
        Scanner scanner = new Scanner(System.in);

        mensagem = mensagem.toUpperCase();
        String opcoes = gerarOpcoes(List.of("Sim","Não"));

        int opcaoSelecionada = 0;

        System.out.println(mensagem + opcoes);
        while(opcaoSelecionada == 0){
            try{
                opcaoSelecionada = scanner.nextInt();
            } catch (InputMismatchException | IllegalStateException ex){
                System.out.println("Valor selecionado inválido, favor digitar apenas o número referente a opção desejada. " + opcoes);
                scanner.next();
            }
        }

        while (opcaoSelecionada > 2 || opcaoSelecionada < 1) {
            System.out.println("Valor selecionado inválido, favor digitar apenas o número referente a opção desejada. " + opcoes);
            System.out.println("--------------------------------------------------------------------------------------------------");
            System.out.println(mensagem + opcoes);
            try{
                opcaoSelecionada = scanner.nextInt();
            } catch (InputMismatchException | IllegalStateException ex){
                scanner.next();
            }
        }
        if(opcaoSelecionada == 1){
            System.out.println("Opção escolhida: Sim");
            System.out.println("--------------------------------------------------------------------------------------------------");
            return true;
        } else {
            System.out.println("Opção escolhida: Não");
            System.out.println("--------------------------------------------------------------------------------------------------");
            return false;
        }
    }

    private static String gerarOpcoes(List<String> opcoes) {
        String mensagemOpcoes = "[ ";
        int contador = 1;
        for (String opcao : opcoes) {
            mensagemOpcoes = mensagemOpcoes.concat(contador + " - " + opcao.toUpperCase());
            if(contador < opcoes.size()) {
                mensagemOpcoes = mensagemOpcoes.concat(" / ");
                contador++;
            }
        }
        mensagemOpcoes = mensagemOpcoes.concat(" ]");
        return mensagemOpcoes;
    }
}
