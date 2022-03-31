import java.util.*;

/**
 * Class do Tabuleiro do jogo Connect4. Construção do tabuleiro e guardar o estado de jogo atual
 */
class Board{

    /**
     * Matrix que guarda o tabuleiro
     */
    char[][] matrix;

    /**
     * Inicia o tabuleiro com todas as posições a 0, representadas por "-"
     */
    Board(){
        matrix = new char[7][6];

        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 7; j++){
                matrix[j][i] = '-';
            }
        }
    }
}

/**
 * Class main
 */
public class ConnectFour{
    public static Scanner sc = new Scanner(System.in);
    public static Board board = new Board();

    /**
     * Jogador atual.
     * false = Humano,
     * true = PC
     */
    public static boolean PC;

    /**
     * Verifica se o jogo acabou com algum dos jogadores vencedores e imprime o resultado.
     * Retorna TRUE se o jogo acabou.
     */
    public static boolean checkGameState(){
        return false;
        return true;
    }

    /**
     * Função invocada depois do updateBoard(). Processa o pedido e atualiza o tabuleiro de acordo.
     */
    public static void updateBoard(int column){
        if(board.matrix[column][0] == 'X'){
            System.out.println("Column full! Choose another spot!");
            System.out.print("Next Play: ");
            updateBoard(sc.nextInt());
        }
        for(int i = 5; i >= 0; i--){
            if(board.matrix[column][i] == '-'){
                board.matrix[column][i] = 'X';
                break;
            }
        }
        printBoard();
    }

    /**
     * Função de próxima jogada. Ou pede ao jogador a sua próxima jogada, ou ao PC.
     */
    public static void nextPlay(){
        int column;
        if(!PC){
            System.out.println("Player: Jogador");
            System.out.print("Next Play: ");
            column = sc.nextInt();
            System.out.println();
        }
        else{
            System.out.println("Player: PC");
            column = 5;
        }
        updateBoard(column);
    }

    /**
     * Função que chama as outras funções para correr o jogo.
     */
    public static void gameCycle(){
        nextPlay();

        if(checkGameState()){
            System.out.println("O Jogo acabou!\nVencedor: ");
            if(PC) System.out.print("PC");
            else System.out.print("Jogador");
        }
        gameCycle();
    }

    /**
     * Imprimir estado do jogo atual
     */
    public static void printBoard(){
        System.out.println("--------------");
        for(int i = 0; i < 7; i++){
            System.out.print(i + " ");
        }
        System.out.println();
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 7; j++){
                System.out.print(board.matrix[j][i] + " ");
            }
            System.out.println();
        }
        System.out.println("--------------");
    }

    /**
     * Escolhe o primeiro jogador
     */
    public static void chooseFirstPlayer(){
        Random rand = new Random();
        int randomNum = rand.nextInt(0, 1 + 1);

        if(randomNum == 0) PC = false;
        else PC = true;
    }
    
    public static void main(String[] args)
    {
        chooseFirstPlayer();

        PC = false;
        if(PC) System.exit(1);
        printBoard();
        gameCycle();
    }
}