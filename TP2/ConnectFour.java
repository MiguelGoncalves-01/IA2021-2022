import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
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
     * Número de espaços disponíveis para jogar no início do jogo
     */
    int availablePlays;

    /**
     * Guarda a última jogada. X em [0], Y em [1];
     */
    int[] lastPlay;

    Board fatherNode;
    LinkedList<Board> children;

    int depth;

    int column;

    int value;

    /**
     * Inicia o tabuleiro com todas as posições a 0, representadas por "-"
     */
    Board(){
        this.matrix = new char[7][6];
        this.availablePlays = 42;
        this.lastPlay = new int[2];
        this.fatherNode = null;
        this.children = new LinkedList<Board>();
        this.depth = 0;
        this.column = 0;
        this.value = 0;

        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 7; j++){
                this.matrix[j][i] = '-';
            }
        }
    }

    Board(Board newFatherNode, int inputDepth, int inputColumn){
        this.matrix = new char[7][6];
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 6; j++){
                this.matrix[i][j] = newFatherNode.matrix[i][j];
            }
        }
        this.availablePlays = newFatherNode.availablePlays - 1;
        this.lastPlay = new int[2];
        this.fatherNode = newFatherNode;
        this.children = new LinkedList<Board>();
        this.depth = inputDepth;
        this.column = inputColumn;
        this.value = 0;
    }
}

/**
 * Class main
 */
public class ConnectFour{
    public static Scanner sc = new Scanner(System.in);
    public static Board board = new Board();
    public static Queue<Board> list = new LinkedList<Board>();

    /**
     * Jogador atual.
     * false = Humano,
     * true = PC
     */
    public static boolean PC;
    public static boolean PCtree;

    public static void cloneMatriz(char[][] matriz1, char[][] matriz2)
    {
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 6; j++){
                matriz1[i][j] = matriz2[i][j];
            }
        }
    }

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
    public static boolean checkGameState(Board currentBoard){
        char lastPlayType = currentBoard.matrix[currentBoard.lastPlay[0]][currentBoard.lastPlay[1]];
        boolean formedLine = formedLine(lastPlayType);

        if(currentBoard.availablePlays == 0 && !formedLine){
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
        printBoard(board);
        board.availablePlays--;
    }

    /**
     * Criar o primeiro estado da árvore no início do jogo
     */
    public static void treeStartup(int dificultyLevel){
        int currentDepth = 0;
        int previousDepth = 0;

        list.add(board);

        while(!list.isEmpty()){
            Board cNode = list.remove();
            currentDepth = cNode.depth + 1;
            if(currentDepth != previousDepth){
                PCtree = !PCtree;
                previousDepth++;
            }
            if(currentDepth <= dificultyLevel){
                findChildren(cNode, currentDepth);
            }
            else break;
        }
    }
    
    /**
     * Encontrar a próxima jogada do computador
     * @return int (Valor da Coluna)
     */
    /*
    public static int findNextPCPlay(){
        return ;
    }*/

    /**
     * Encontra todos os nós filhos do estado dado e adiciona-os à árvore
     * @param currentBoard
     */
    public static void findChildren(Board currentBoard, int depth){
        Board child;
        for(int i = 0; i < 7; i++)
        {
            if(currentBoard.matrix[i][0] == '-'){
                for(int j = 5; j >= 0; j--){
                    if(currentBoard.matrix[i][j] == '-'){
                        char[][] matrixTemp = new char[7][6];
                        cloneMatriz(matrixTemp, currentBoard.matrix);
                        if(PCtree) matrixTemp[i][j] = 'O';
                        else matrixTemp[i][j] = 'X';
                        child = new Board(currentBoard, depth, i);
                        cloneMatriz(child.matrix, matrixTemp);
                        child.lastPlay[0] = i;
                        child.lastPlay[1] = j;
                        currentBoard.children.add(child);
                        list.add(child);
                        matrixTemp[i][j] = '-';
                        break;
                    }
                }
            }
        }
    }

    
    /**
     * Função MiniMax
     * @param board
     * @param depth
     * @param maximizingPlayer
     * @return
     */
    /*
    public static int minimax(Board board, int depth, boolean maximizingPlayer){
        int value;
        boolean isTerminal = checkGameState(); // é terminal se estiver o tabuleiro estiver cheio ou tiver sido atingido vitória no estado atual do jogo
        if (depth == 0 || isTerminal ) // or board está num estado terminal
            return 0; // return node value

        if (maximizingPlayer)
        {
            value = Integer.MIN_VALUE;      // min int value
            // for each child node
                {
                    value = max(value, minimax(child, depth - 1, false));
                }
            return value;
        }
        else    // minimizing player
        {
            value = Integer.MAX_VALUE;        // max int value
            // for each child node
            {   
                value  = min(value minimax(child, depth - 1 , true));
            }
            return value;
        }
    }
    */

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
            //column = findNextPCPlay();
           // System.out.print(column);
            column = sc.nextInt();
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
        if(checkGameState(board)) return;
        gameCycle();
    }

    /**
     * Imprimir estado do jogo atual
     */
    public static void printBoard(Board currentBoard){
        System.out.println("--------------");
        for(int i = 0; i < 7; i++){
            System.out.print(i + " ");
        }
        System.out.println();
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 7; j++){
                System.out.print(currentBoard.matrix[j][i] + " ");
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
        int randomNum = rand.nextInt((1 - 0) + 1) + 0;

        if(randomNum == 0){
            PC = false;
            PCtree = false;
        }
        else{
            PC = true;
            PCtree = true;
        }
    }
    
    public static int chooseDificulty(){
        System.out.println("\nChoose level of AI: \nFor Easy press 1\nFor Medium press 2\nFor Hard press 3");

        int dificultyLevel = sc.nextInt();

        if(dificultyLevel == 1) return 2;
        else if(dificultyLevel == 2) return 3;
        else return 5;
    }
    public static void main(String[] args)
    {
        int dificultyLevel = chooseDificulty();
        chooseFirstPlayer();
        treeStartup(dificultyLevel);
        printBoard(board);
        gameCycle();
    }
}