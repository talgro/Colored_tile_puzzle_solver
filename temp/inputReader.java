import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class inputReader {

    public static TilePuzzleExperiment createTilePuzzleExperiment() throws Exception {
        //import input file content to a linked list of lines
        LinkedList<String> inputLines = fileToList();
        //Algorithm Choice
        final I_Algo_SearchAlgorithm algorithm = readAlgoChoice(inputLines.pop());
        //TilePuzzle Size
        final TilePuzzle.BoardSize boardSize = readBoardSize(inputLines.pop());
        //Tiles
        Tile[] orderedTiles = readAndOrderTiles(inputLines.pop(), inputLines.pop(), boardSize);
        //set blocks location
        ArrayList<Tile> locatedBlocks = placeBlocks(orderedTiles, inputLines);
        TilePuzzle beginState = new TilePuzzle(boardSize, locatedBlocks);
        beginState.initBoard(locatedBlocks);
        //finally, return TilePuzzle Experiment
        return new TilePuzzleExperiment(algorithm, beginState);
    }

    private static LinkedList<String> fileToList() throws IOException {
        LinkedList<String> inputLines = new LinkedList<>();
        Scanner s = new Scanner(new File("input.txt"));
        while (s.hasNext()) {
            String nextStr = s.nextLine();
            inputLines.add(nextStr);
        }
        s.close();
        return inputLines;
    }

    private static I_Algo_SearchAlgorithm readAlgoChoice(String line) throws Exception {
        final int choice = Integer.parseInt(line);
        switch (choice) {
            case 1:
                return new Algo_BFS();
            case 2:
                return new Algo_DFID();
            case 3:
                return new Algo_A_Star();
            case 4:
                return new Algo_IDA_Star();
            case 5:
                return new Algo_DFBnB();
            default:
                throw new Exception("Invalid Algorithm choice!");
        }
    }

    private static TilePuzzle.BoardSize readBoardSize(String line) throws Exception {
        int Xindex = line.indexOf("x");
        int rows = Integer.parseInt(line.substring(0, Xindex));
        int cols = Integer.parseInt(line.substring(Xindex + 1, line.length()));
        return new TilePuzzle.BoardSize(rows,cols);
    }

    private static void textToBlockArr(String line, Tile[] blocksArrToFill){
        //read color
        String[] splittedLine;
        final Tile.Color color;
        splittedLine = StrAndTxtManipulator.splitAndLoseSpaces(line, ":");
        color = Tile.Color.valueOf(splittedLine[0].toUpperCase());
        line = line.substring(line.indexOf(":") + 1, line.length());   //remove "Color: " from line
        if (line.length() > 1) {
            //read indexes
            splittedLine = StrAndTxtManipulator.splitAndLoseSpaces(line, ",");
            for (String indexSTR : splittedLine) {
                int index = Integer.parseInt(indexSTR);
                Tile newTile = new Tile(color, index);
                blocksArrToFill[index] = newTile;
            }
        }

    }

    private static Tile[] readAndOrderTiles(String red, String yellow, TilePuzzle.BoardSize size) throws Exception {
        //fill blocks-array with red and yellow blocks
        Tile[] ans = new Tile[size._cols*size._rows];
        //add blank blocks to array
        String black = "BLACK: 0";
        textToBlockArr(black, ans);
        //add red blocks to array
        textToBlockArr(red, ans);
        //add yellow blocks to array
        textToBlockArr(yellow, ans);
        //add white blocks to array
        String white = "WHITE: ";
        for (int i = 0; i < ans.length; i++){
            if (ans[i] == null){
                white = white + i + ", ";
            }
        }
        white = white.substring(0, white.length() - 1);
        textToBlockArr(white, ans);
        //finish and return
        return ans;
    }

    private static ArrayList<Tile> placeBlocks(Tile[] orderedTiles, LinkedList<String> inputLines){
        ArrayList<Tile> placedTiles = new ArrayList<>();
        while (!inputLines.isEmpty()){
            String currLine = inputLines.pop();
            String[] indexes = StrAndTxtManipulator.splitAndLoseSpaces(currLine, ",");
            for (String indexSTR : indexes){
                int index = 0;
                if (!indexSTR.equals("_")){
                    index = Integer.parseInt(indexSTR);;
                }
                Tile tileToBePlaced = orderedTiles[index];
                placedTiles.add(tileToBePlaced);
            }
        }
        return placedTiles;
    }

}
