public class TilePuzzleExperiment {

    private I_Algo_SearchAlgorithm _algo;
    private TilePuzzle _tp;
    private Solution _solution;

    public TilePuzzleExperiment(I_Algo_SearchAlgorithm algo, TilePuzzle tp){
        _algo = algo;
        _algo.setGoalHash(tp);
        _tp = tp;
    }

    public void run() throws Exception {
        _solution = _algo.calculate(_tp);
    }

    public Solution getSolution() throws Exception {
        if (_solution == null){
            run();
        }
        return _solution;
    }
}
