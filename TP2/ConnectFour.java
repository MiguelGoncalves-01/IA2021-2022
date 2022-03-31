import java.util.Scanner;

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
        matrix = new char[6][7];

        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 7; j++){
                matrix[i][j] = '-';
            }
        }
    }
}

/**
 * Class main
 */
public class ConnectFour{

    public static Board board = new Board();

    /**
     * Imprimir estado do jogo atual
     */
    public static void printBoard(){
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 7; j++){
                System.out.print(board.matrix[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in); 
        printBoard();
    }
}