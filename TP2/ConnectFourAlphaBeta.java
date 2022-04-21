import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;


/*
OBJETIVOS

FUNÇÂO DE AVALIAÇAO PARA MINIMAX

GERAÇÂO DE MAIS ARVORE DE NOS

*/

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
public class ConnectFourAlphaBeta{
    public static Scanner sc = new Scanner(System.in);
    public static Board board = new Board();
    public static Queue<Board> list = new LinkedList<Board>();
    public static boolean humanFirstPlay;
    public static Board currentNode;

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
        int[] lastPlayTemp = new int[2];
        for(int i = 5; i >= 0; i--){
            if(board.matrix[column][i] == '-'){
                lastPlayTemp[0] = column;
                lastPlayTemp[1] = i;
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

        for(int i = 0; i < currentNode.children.size(); i++){
            if(currentNode.children.get(i).lastPlay[0] == lastPlayTemp[0] && currentNode.children.get(i).lastPlay[1] == lastPlayTemp[1]){
                currentNode = currentNode.children.get(i);
            }
        }
        updateTree();
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

    public static void updateTree(){
        LinkedList<Board> tempList = new LinkedList<Board>();
        Board tempNode;

        tempList.add(currentNode);

        while(!tempList.isEmpty()){
            tempNode = tempList.remove();

            if(tempNode.children.size() != 0){
                for(int i = 0; i < tempNode.children.size(); i++){
                    tempList.add(tempNode.children.get(i));
                }
            }
            else{
                findChildren(tempNode, tempNode.depth + 1);
            }
        }
    }
    
    /**
     * Encontrar a próxima jogada do computador
     * @return int (Valor da Coluna)
     */
    public static int findNextPCPlay(){
        int column = 0;
        int max = Integer.MIN_VALUE;

        for(int i = 0; i < currentNode.children.size(); i++){
            Board childrenNode = currentNode.children.get(i);
            int tempValue = minimax(childrenNode, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            if(tempValue >= max){
                max = tempValue;
                column = childrenNode.lastPlay[0];
            }
        }
        return column;
    }

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
     * Function that evaluates value of given Board, which is then used for minimax algorithm
     * @return value
     */
    public static int evaluateBoard(Board currentBoard)
    {
        int value = 0;
        boolean alone= true;
        char[][] matrixTemp = new char[7][6];
        cloneMatriz(matrixTemp, currentBoard.matrix);

        //Casos isolados
        for(int i = 0; i < 7; i++)
        {
             for(int j = 0; j < 6; j++)
             {
                 if(matrixTemp[i][j] == 'X'){
                     alone = true;
                     if(!outOfBounds(i + 1,     j)) if(matrixTemp[i + 1][j]     != '-') alone = false;
                     if(!outOfBounds(i - 1,     j)) if(matrixTemp[i - 1][j]     != '-') alone = false;
                     if(!outOfBounds(i + 1, j + 1)) if(matrixTemp[i + 1][j + 1] != '-') alone = false;
                     if(!outOfBounds(i - 1, j - 1)) if(matrixTemp[i - 1][j - 1] != '-') alone = false;
                     if(!outOfBounds(i + 1, j - 1)) if(matrixTemp[i + 1][j - 1] != '-') alone = false;
                     if(!outOfBounds(i - 1, j + 1)) if(matrixTemp[i - 1][j + 1] != '-') alone = false;
                     if(!outOfBounds(i    , j + 1)) if(matrixTemp[i][j + 1]     != '-') alone = false;
                     if(!outOfBounds(i    , j - 1)) if(matrixTemp[i][j - 1]     != '-') alone = false;
                     if(!outOfBounds(i + 1, j + 1)) if(matrixTemp[i + 1][j + 1] != '-') alone = false;
                     if(!outOfBounds(i - 1, j - 1)) if(matrixTemp[i - 1][j - 1] != '-') alone = false;
                     if(!outOfBounds(i - 1, j + 1)) if(matrixTemp[i - 1][j + 1] != '-') alone = false;
                     if(!outOfBounds(i + 1, j - 1)) if(matrixTemp[i + 1][j - 1] != '-') alone = false;
                     if(alone) value += 1;           
                }
                if(matrixTemp[i][j] == 'O'){
                    alone = true;
                    if(!outOfBounds(i + 1,     j)) if(matrixTemp[i + 1][j]     != '-') alone = false;
                    if(!outOfBounds(i - 1,     j)) if(matrixTemp[i - 1][j]     != '-') alone = false;
                    if(!outOfBounds(i + 1, j + 1)) if(matrixTemp[i + 1][j + 1] != '-') alone = false;
                    if(!outOfBounds(i - 1, j - 1)) if(matrixTemp[i - 1][j - 1] != '-') alone = false;
                    if(!outOfBounds(i + 1, j - 1)) if(matrixTemp[i + 1][j - 1] != '-') alone = false;
                    if(!outOfBounds(i - 1, j + 1)) if(matrixTemp[i - 1][j + 1] != '-') alone = false;
                    if(!outOfBounds(i    , j + 1)) if(matrixTemp[i][j + 1]     != '-') alone = false;
                    if(!outOfBounds(i    , j - 1)) if(matrixTemp[i][j - 1]     != '-') alone = false;
                    if(!outOfBounds(i + 1, j + 1)) if(matrixTemp[i + 1][j + 1] != '-') alone = false;
                    if(!outOfBounds(i - 1, j - 1)) if(matrixTemp[i - 1][j - 1] != '-') alone = false;
                    if(!outOfBounds(i - 1, j + 1)) if(matrixTemp[i - 1][j + 1] != '-') alone = false;
                    if(!outOfBounds(i + 1, j - 1)) if(matrixTemp[i + 1][j - 1] != '-') alone = false;
                    if(alone) value -= 1;           
               }
            }
        }
                
        //HORIZONTAIS
        for(int i = 0; i < 7; i++)                
        {
            for(int j = 0; j < 6; j++) 
            {
                int counter = 1;
                int temp = i;
                if(matrixTemp[temp][j] == 'X')
                {
                    if(!outOfBounds(temp + 1, j))
                    {
                        while(matrixTemp[temp+1][j] == 'X')
                        {
                            counter++;
                            matrixTemp[temp + 1][j] = '-';
                            temp++;
                            if(outOfBounds(temp + 1, j)) break;    
                        }
                        temp = i;
                    }
                    if(!outOfBounds(temp - 1, j))
                    {
                        while(matrixTemp[temp-1][j] == 'X')
                        {
                            counter++;
                            matrixTemp[temp - 1][j] = '-';
                            temp--;
                            if(outOfBounds(temp - 1, j)) break;    
                        }
                    }
                        
                    if(counter == 2) value += 10;
                    else if(counter == 3) value += 50;
                    else if(counter == 4) value += 512;
                }

                counter = 1;
                temp = i;
                if(matrixTemp[temp][j] == 'O')
                {
                    if(!outOfBounds(temp + 1, j))
                    {
                        while(matrixTemp[temp+1][j] == 'O')
                        {
                            counter++;
                            matrixTemp[temp + 1][j] = '-';
                            temp++;
                            if(outOfBounds(temp + 1, j)) break;    
                        }
                        temp = i;
                    }
                    if(!outOfBounds(temp - 1, j))
                    {
                        while(matrixTemp[temp-1][j] == 'O')
                        {
                            counter++;
                            matrixTemp[temp - 1][j] = '-';
                            temp--;
                            if(outOfBounds(temp - 1, j)) break;    
                        }
                    }  
                    if(counter == 2) value -= 10;
                    else if(counter == 3) value -= 50;
                    else if(counter == 4) value -= 512;
                }
            }
        }
        
        //Verticais
        for(int i = 0; i < 7; i++)                
        {
            for(int j = 0; j < 6; j++)
            {
                int counter = 1;
                int temp = j;
                if(matrixTemp[i][temp] == 'X')
                {
                    if(!outOfBounds(i, temp + 1))
                    {
                        while(matrixTemp[i][temp + 1] == 'X')
                        {
                            counter++;
                            matrixTemp[i][temp + 1] = '-';
                            temp++;
                            if(outOfBounds(i, temp + 1)) break;    
                        }
                        temp = j;
                    }
                    if(!outOfBounds(i, temp - 1))
                    {
                        while(matrixTemp[i][temp - 1] == 'X')
                        {    
                            counter++;
                            matrixTemp[i][temp - 1] = '-';
                            temp--;
                            if(outOfBounds(i, temp - 1)) break;    
                        }
                    }   
                    if(counter == 2) value += 10;
                    else if(counter == 3) value += 50;
                    else if(counter == 4) value += 512;
                }

                counter = 1;
                temp = j;
                if(matrixTemp[i][temp] == 'O')
                {
                    if(!outOfBounds(i, temp + 1))
                    {
                        while(matrixTemp[i][temp + 1] == 'O')
                        {
                            counter++;
                            matrixTemp[i][temp + 1] = '-';
                            temp++;
                            if(outOfBounds(i, temp + 1)) break;    
                        }
                        temp = j;
                    }
                    if(!outOfBounds(i, temp - 1))
                    {
                        while(matrixTemp[i][temp - 1] == 'O')
                        {
                            counter++;
                            matrixTemp[i][temp - 1] = '-';
                            temp--;
                            if(outOfBounds(i, temp - 1)) break;    
                        }
                    }   
                    if(counter == 2) value -= 10;
                    else if(counter == 3) value -= 50;
                    else if(counter == 4) value -= 512;
                }
            }
        }

        // obliqua  
        for(int i = 0; i < 7; i++)                
        {
            for(int j = 0; j < 6; j++)
            {
                int counter = 1;
                int tempi = i;
                int tempj = j;
                int upRight = 0;
                int downRight = 0;
                int upLeft = 0;
                int downLeft = 0;
                int dir = 0;

                if(matrixTemp[tempi][tempj] == 'X')
                {
                    if((!outOfBounds(tempi + 1, tempj + 1)) && (matrixTemp[tempi + 1][tempj + 1] == 'X')) upRight++;
                    if((!outOfBounds(tempi + 1, tempj - 1)) && (matrixTemp[tempi + 1][tempj - 1] == 'X')) downRight++;
                    if((!outOfBounds(tempi - 1, tempj + 1)) && (matrixTemp[tempi - 1][tempj + 1] == 'X')) upLeft++;
                    if((!outOfBounds(tempi - 1, tempj - 1)) && (matrixTemp[tempi - 1][tempj - 1] == 'X')) downLeft++;

                    dir = upRight + downRight + upLeft + downLeft;
                    if (dir == 1)
                    {
                            if(!outOfBounds(tempi + 1, tempj + 1))
                            {
                                while(matrixTemp[tempi + 1][tempj + 1] == 'X')
                                {
                                    counter++;
                                    matrixTemp[tempi + 1][tempj + 1] = '-';
                                    tempi++;
                                    tempj++;
                                    if(outOfBounds(tempi + 1, tempj + 1)) break;
                                    
                                }
                                tempi = i;
                                tempj = j;
                            }
                            if(!outOfBounds(tempi - 1, tempj - 1))
                            {
                                while(matrixTemp[tempi - 1][tempj - 1] == 'X')
                                {
                                    counter++;
                                    matrixTemp[tempi - 1][tempj - 1] = '-';
                                    tempi--;
                                    tempj--;
                                    if(outOfBounds(tempi - 1, tempj - 1)) break;
                                    
                                }
                                tempi = i;
                                tempj = j;
                            }
                            if(!outOfBounds(tempi + 1, tempj - 1))
                            {
                                while(matrixTemp[tempi + 1][tempj - 1] == 'X')
                                {
                                    counter++;
                                    matrixTemp[tempi + 1][tempj - 1] = '-';
                                    tempi++;
                                    tempj--;
                                    if(outOfBounds(tempi + 1, tempj - 1)) break;
                                    
                                }
                                tempi = i;
                                tempj = j;
                            }
                            if(!outOfBounds(tempi - 1, tempj + 1))
                            {
                                while(matrixTemp[tempi - 1][tempj + 1] == 'X')
                                {
                                    counter++;
                                    matrixTemp[tempi - 1][tempj + 1] = '-';
                                    tempi--;
                                    tempj++;
                                    if(outOfBounds(tempi - 1, tempj + 1)) break;
                                    
                                }
                                tempi = i;
                                tempj = j;
                            }
                            if(counter == 2) value += 10;
                            else if(counter == 3) value += 50;
                            else if(counter == 4) value += 512;
                        
                    }
                    if(matrixTemp[tempi][tempj] == 'O')
                    {
                        if((!outOfBounds(tempi + 1, tempj + 1)) && (matrixTemp[tempi + 1][tempj + 1] == 'X')) upRight++;
                        if((!outOfBounds(tempi + 1, tempj - 1)) && (matrixTemp[tempi + 1][tempj - 1] == 'X')) downRight++;
                        if((!outOfBounds(tempi - 1, tempj + 1)) && (matrixTemp[tempi - 1][tempj + 1] == 'X')) upLeft++;
                        if((!outOfBounds(tempi - 1, tempj - 1)) && (matrixTemp[tempi - 1][tempj - 1] == 'X')) downLeft++;

                        dir = upRight + downRight + upLeft + downRight;
                        if (dir == 1)
                        {
                            if(!outOfBounds(tempi + 1, tempj + 1))
                            {
                                while(matrixTemp[tempi + 1][tempj + 1] == 'O')
                                {
                                    counter++;
                                    matrixTemp[tempi + 1][tempj + 1] = '-';
                                    tempi++;
                                    tempj++;
                                    if(outOfBounds(tempi + 1, tempj + 1)) break;
                                    
                                }
                                tempi = i;
                                tempj = j;
                            }
                            if(!outOfBounds(tempi - 1, tempj - 1))
                            {
                                while(matrixTemp[tempi - 1][tempj - 1] == 'O')
                                {
                                    counter++;
                                    matrixTemp[tempi - 1][tempj - 1] = '-';
                                    tempi--;
                                    tempj--;
                                    if(outOfBounds(tempi - 1, tempj - 1)) break;
                                    
                                }
                                tempi = i;
                                tempj = j;
                            }
                            if(!outOfBounds(tempi + 1, tempj - 1))
                            {
                                while(matrixTemp[tempi + 1][tempj - 1] == 'O')
                                {
                                    counter++;
                                    matrixTemp[tempi + 1][tempj - 1] = '-';
                                    tempi++;
                                    tempj--;
                                    if(outOfBounds(tempi + 1, tempj - 1)) break;
                                    
                                }
                                tempi = i;
                                tempj = j;
                            }
                            if(!outOfBounds(tempi - 1, tempj + 1))
                            {
                                while(matrixTemp[tempi - 1][tempj + 1] == 'O')
                                {
                                    counter++;
                                    matrixTemp[tempi - 1][tempj + 1] = '-';
                                    tempi--;
                                    tempj++;
                                    if(outOfBounds(tempi - 1, tempj + 1)) break;
                                    
                                }
                                tempi = i;
                                tempj = j;
                            }
                            if(counter == 2) value += 10;
                            else if(counter == 3) value += 50;
                            else if(counter == 4) value += 512;
                        
                        }
                    }   
                } 
            }
        }
        return value;
    }

    /**
     * Função MiniMax
     * @param board
     * @param depth
     * @param maximizingPlayer
     * @return
     */
    public static int minimax(Board currentBoard, int alpha, int beta, boolean maximizingPlayer){
        int value;
        Board child;
        boolean isTerminal = checkGameState(currentBoard); // é terminal se estiver o tabuleiro estiver cheio ou tiver sido atingido vitória no estado atual do jogo
        if (currentBoard.children.size() == 0 || isTerminal ) // or board está num estado terminal
            return evaluateBoard(currentBoard); // return node value

        if (maximizingPlayer)
        {
            value = Integer.MIN_VALUE;      // min int value
            for(int i = 0; i < currentBoard.children.size(); i++){
                    child = currentBoard.children.get(i);
                    value = Math.max(value, minimax(child, alpha, beta, false));
                    if(value >= beta) break;
                    alpha = Math.max(alpha, value);
                }
            return value;
        }
        else    // minimizing player
        {
            value = Integer.MAX_VALUE;        // max int value
            for(int i = 0; i < currentBoard.children.size(); i++){   
                child = currentBoard.children.get(i);
                value  = Math.min(value, minimax(child, alpha, beta, true));
                if(value <= alpha) break;
                beta = Math.min(beta, value);
            }
            return value;
        }
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
            column = findNextPCPlay();
            System.out.print(column);
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
        if(checkGameState(board)) System.exit(1);
        gameCycle();
    }

    public static void gameCycle(int dificultyLevel){
        nextPlay();
        if(checkGameState(board)) System.exit(1);
        treeStartup(dificultyLevel);
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
        System.out.println("Choose First Player. For Human type 1, for PC type 2, for random type 3. ");
        int aux = sc.nextInt();
        if(aux == 1)
        {
            PC = false;
            PCtree = false;
            humanFirstPlay = false;
        }
        if(aux == 2)
        {
            PC = true;
            PCtree = true;
        }
        if(aux == 3)
        {
            if(randomNum == 0)
            {
                PC = false;
                PCtree = false;
                humanFirstPlay = false;
            }
            else
            {
                PC = true;
                PCtree = true;
            }
        }
    }
    
    public static int chooseDificulty(){
        System.out.println("\nChoose level of AI: \nFor Easy press 1\nFor Medium press 2\nFor Hard press 3\nFor Very Hard press 4 (generates whole tree)");

        int dificultyLevel = sc.nextInt();

        if(dificultyLevel == 1) return 2;
        else if(dificultyLevel == 2) return 5;
        else if(dificultyLevel == 3) return 7;
        else return Integer.MAX_VALUE;
    }
    public static void main(String[] args)
    {
        currentNode = board;
        humanFirstPlay = true;
        int dificultyLevel = chooseDificulty();
        chooseFirstPlayer();
        printBoard(board);
        if(humanFirstPlay) treeStartup(dificultyLevel);
        else gameCycle(dificultyLevel);
        gameCycle();
    }
}