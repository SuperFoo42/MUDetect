package de.tu_darmstadt.stg.mudetect.overlapsfinder;

import de.tu_darmstadt.stg.mudetect.aug.model.TestAUGBuilder;
import de.tu_darmstadt.stg.mudetect.matcher.EquallyLabelledNodeMatcher;
import de.tu_darmstadt.stg.mudetect.model.Overlap;
import org.junit.Test;

import java.util.List;

import static de.tu_darmstadt.stg.mudetect.aug.model.Edge.Type.ORDER;
import static de.tu_darmstadt.stg.mudetect.aug.model.TestAUGBuilder.buildAUG;
import static de.tu_darmstadt.stg.mudetect.aug.model.controlflow.ConditionEdge.ConditionType.SELECTION;
import static de.tu_darmstadt.stg.mudetect.model.TestOverlapBuilder.buildOverlap;
import static de.tu_darmstadt.stg.mudetect.overlapsfinder.OverlapsFinderTestUtils.contains;
import static edu.iastate.cs.mudetect.mining.TestPatternBuilder.somePattern;
import static org.junit.Assert.assertThat;

public class OrderMappingTest {
    @Test
    public void doesNotMapOrderOnlyConnection() {
        TestAUGBuilder target = buildAUG().withActionNodes("A.m()", "C.f()").withEdge("A.m()", ORDER, "C.f()");
        TestAUGBuilder pattern = buildAUG().withActionNodes("A.m()", "C.f()")
                .withEdge("A.m()", ORDER, "C.f()").withCondEdge("A.m()", SELECTION, "C.f()");

        AlternativeMappingsOverlapsFinder overlapsFinder = new AlternativeMappingsOverlapsFinder(new AlternativeMappingsOverlapsFinder.Config() {{
            nodeMatcher = new EquallyLabelledNodeMatcher();
        }});
        // SMELL why does this return a list? The order is arbitrary, isn't it?
        List<Overlap> overlaps = overlapsFinder.findOverlaps(target.build(), somePattern(pattern));

        assertThat(overlaps, contains(
                buildOverlap(target, pattern).withNode("A.m()"),
                buildOverlap(target, pattern).withNode("C.f()")));
    }

    @Test
    public void mapsOrderIfOtherConnectionExists() {
        TestAUGBuilder target = buildAUG().withActionNodes("A.m()", "C.f()")
                .withEdge("A.m()", ORDER, "C.f()").withCondEdge("A.m()", SELECTION, "C.f()");
        TestAUGBuilder pattern = buildAUG().withActionNodes("A.m()", "C.f()")
                .withEdge("A.m()", ORDER, "C.f()").withCondEdge("A.m()", SELECTION, "C.f()");

        AlternativeMappingsOverlapsFinder overlapsFinder = new AlternativeMappingsOverlapsFinder(new AlternativeMappingsOverlapsFinder.Config() {{
            nodeMatcher = new EquallyLabelledNodeMatcher();
        }});
        List<Overlap> overlaps = overlapsFinder.findOverlaps(target.build(), somePattern(pattern));

        assertThat(overlaps, contains(buildOverlap(target, pattern).withNodes("A.m()", "C.f()")
                .withEdge("A.m()", ORDER, "C.f()").withEdge("A.m()", SELECTION, "C.f()")));
    }
}