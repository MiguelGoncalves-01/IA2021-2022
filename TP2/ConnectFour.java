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
     * Número de espaços disponíveis para jogar no início do jogo
     */
    int availablePlays;

    /**
     * Guarda a última jogada. X em [0], Y em [1];
     */
    int[] lastPlay;

    /**
     * Inicia o tabuleiro com todas as posições a 0, representadas por "-"
     */
    Board(){
        matrix = new char[7][6];
        availablePlays = 42;
        lastPlay = new int[2];

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
     * Função que vê se o ponto a verificar está outOfBounds ou não
     * @param x
     * @param y
     * @return false or true
     */
    public static boolean outOfBounds(int x, int y){
        if(x < 0 || y < 0 || x > 6 || y > 5) return true;
        return false;
    }

    /**
     * Função que verifica se o último ponto colocado formou uma linha
     * @param lastPlayType
     * @return false se não formou linha de 4. true se formou linha de 4
     */
    public static boolean formedLine(char lastPlayType){
        int counter = 1;
        int tempX = board.lastPlay[0];
        int tempY = board.lastPlay[1];

        /*
        //UP
        for(int i = 0; i < 3; i++){
            tempY--;
            if(outOfBounds(tempX, tempY)) break;
            if(board.matrix[tempX][tempY] != lastPlayType) break;
            counter++;
        }
        if(counter == 4) return true;
        tempX = board.lastPlay[0];
        tempY = board.lastPlay[1];
        counter = 1;
        */
        //DOWN
        for(int i = 0; i < 3; i++){
            tempY++;
            if(outOfBounds(tempX, tempY)) break;
            if(board.matrix[tempX][tempY] != lastPlayType) break;
            counter++;
        }
        if(counter == 4) return true;
        tempX = board.lastPlay[0];
        tempY = board.lastPlay[1];
        counter = 1;

        //RIGHT
        for(int i = 0; i < 3; i++){
            tempX++;
            if(outOfBounds(tempX, tempY)) break;
            if(board.matrix[tempX][tempY] != lastPlayType) break;
            counter++;
        }
        if(counter == 4) return true;
        tempX = board.lastPlay[0];
        tempY = board.lastPlay[1];
        counter = 1;

        //LEFT
        for(int i = 0; i < 3; i++){
            tempX--;
            if(outOfBounds(tempX, tempY)) break;
            if(board.matrix[tempX][tempY] != lastPlayType) break;
            counter++;
        }
        if(counter == 4) return true;
        tempX = board.lastPlay[0];
        tempY = board.lastPlay[1];
        counter = 1;

        //DIAGONAL RIGHT UP
        for(int i = 0; i < 3; i++){
            tempX++;
            tempY--;
            if(outOfBounds(tempX, tempY)) break;
            if(board.matrix[tempX][tempY] != lastPlayType) break;
            counter++;
        }
        if(counter == 4) return true;
        tempX = board.lastPlay[0];
        tempY = board.lastPlay[1];
        counter = 1;

        //DIAGONAL LEFT UP
        for(int i = 0; i < 3; i++){
            tempX--;
            tempY--;
            if(outOfBounds(tempX, tempY)) break;
            if(board.matrix[tempX][tempY] != lastPlayType) break;
            counter++;
        }
        if(counter == 4) return true;
        tempX = board.lastPlay[0];
        tempY = board.lastPlay[1];
        counter = 1;

        //DIAGONAL RIGHT DOWN
        for(int i = 0; i < 3; i++){
            tempX++;
            tempY++;
            if(outOfBounds(tempX, tempY)) break;
            if(board.matrix[tempX][tempY] != lastPlayType) break;
            counter++;
        }
        if(counter == 4) return true;
        tempX = board.lastPlay[0];
        tempY = board.lastPlay[1];
        counter = 1;

        //DIAGONAL LEFT DOWN
        for(int i = 0; i < 3; i++){
            tempX--;
            tempY++;
            if(outOfBounds(tempX, tempY)) break;
            if(board.matrix[tempX][tempY] != lastPlayType) break;
            counter++;
        }
        if(counter == 4) return true;
        return false;
    }

    /**
     * Verifica se o jogo acabou com algum dos jogadores vencedores.
     * Retorna TRUE se o jogo acabou.
     * @return false or true
     */
    public static boolean checkGameState(){
        char lastPlayType = board.matrix[board.lastPlay[0]][board.lastPlay[1]];
        boolean formedLine = formedLine(lastPlayType);

        if(board.availablePlays == 0 && !formedLine){
            System.out.print("The game ended!\nIt was a DRAW!\n--------------\n");
            return true;
        }
        if(formedLine){
            if(lastPlayType == 'X'){
                System.out.print("The game ended!\nJogador WON!\n--------------\n");
                return true;
            }
            System.out.print("The game ended!\nPC WON!\n--------------\n");
            return true;
        }
        return false;
    }

    /**
     * Função invocada depois do nextPlay(). Processa o pedido e atualiza o tabuleiro de acordo.
     */
    public static void updateBoard(int column){
        for(int i = 5; i >= 0; i--){
            if(board.matrix[column][i] == '-'){
                if(PC){
                    board.matrix[column][i] = 'O';
                    board.lastPlay[0] = column;
                    board.lastPlay[1] = i;
                    break;
                }
                else{
                    board.matrix[column][i] = 'X';
                    board.lastPlay[0] = column;
                    board.lastPlay[1] = i;
                    break;
                }
            }
        }
        printBoard();
        board.availablePlays--;
    }

    /**
     * Função de próxima jogada. Ou pede ao jogador a sua próxima jogada, ou ao PC. Invoca a função updateBoard()
     */
    public static void nextPlay(){
        int column;
        if(!PC){
            //Jogador escolhe a próxima jogada e coloca o valor em 'column'
            System.out.println("Player: Jogador");
            System.out.print("Next Play: ");
            column = sc.nextInt();
            System.out.println();
        }
        else{
            //PC escolhe a próxima jogada e coloca o valor em 'column'
            System.out.println("Player: PC");
            System.out.print("Next Play: ");
            column = sc.nextInt();
            //column = findNextPCPlay();
            //System.out.print(column);
            System.out.println();
        }
        //Verifica se a coluna já está cheia
        if(board.matrix[column][0] != '-'){
            System.out.println("Column full! Choose another spot!");
            nextPlay();
        }
        else{
            updateBoard(column);
            PC = !PC;
        }
    }

    /**
     * Função cíclica que chama as outras funções para correr o jogo.
     */
    public static void gameCycle(){
        nextPlay();
        if(checkGameState()) return;
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
        printBoard();
        gameCycle();
    }
}