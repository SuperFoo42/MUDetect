package de.tu_darmstadt.stg.mubench;

import de.tu_darmstadt.stg.mubench.cli.DetectorArgs;
import de.tu_darmstadt.stg.mudetect.AlternativeMappingsOverlapsFinder;
import de.tu_darmstadt.stg.mudetect.MissingElementViolationPredicate;
import de.tu_darmstadt.stg.mudetect.MuDetect;
import de.tu_darmstadt.stg.mudetect.matcher.EquallyLabelledNodeMatcher;
import de.tu_darmstadt.stg.mudetect.mining.AUGMiner;
import de.tu_darmstadt.stg.mudetect.mining.DefaultAUGMiner;
import de.tu_darmstadt.stg.mudetect.mining.MinPatternActionsModel;
import de.tu_darmstadt.stg.mudetect.mining.Model;
import de.tu_darmstadt.stg.mudetect.ranking.*;
import egroum.EGroumBuilder;
import egroum.EGroumGraph;
import mining.Configuration;

import java.io.IOException;
import java.util.Collection;

class IntraProjectStrategy extends MuDetectStrategy {
    @Override
    Collection<EGroumGraph> loadTrainingExamples(DetectorArgs args) throws IOException {
        return new EGroumBuilder(new DefaultAUGConfiguration())
                .buildBatch(args.getTargetPath().srcPath, args.getDependencyClassPath());
    }

    @Override
    AUGMiner createMiner() {
        return new DefaultAUGMiner(new DefaultMiningConfiguration());
    }

    @Override
    MuDetect createDetector(Model model) {
        return new MuDetect(
                new MinPatternActionsModel(model, 2),
                new AlternativeMappingsOverlapsFinder(new AlternativeMappingsOverlapsFinder.Config() {{
                    nodeMatcher = new EquallyLabelledNodeMatcher(((Configuration) new DefaultMiningConfiguration()).nodeToLabel);
                }}),
                new MissingElementViolationPredicate(),
                new WeightRankingStrategy(
                        new ProductWeightFunction(
                                new PatternSupportWeightFunction(),
                                new PatternViolationsWeightFunction(),
                                new OverlapWithoutEdgesToMissingNodesWeightFunction(
                                        new ConstantNodeWeightFunction()
                                ))));
    }
}
