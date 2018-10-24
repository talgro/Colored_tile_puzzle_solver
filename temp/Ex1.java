import java.io.PrintWriter;

public class Ex1 {

    public static void main(String[] args) throws Exception {
        TilePuzzleExperiment experiment = inputReader.createTilePuzzleExperiment();
        experiment.run();
        Solution solution = experiment.getSolution();
        PrintWriter out = new PrintWriter("output.txt");
        out.println(solution.toString());
        out.close();
    }
}
