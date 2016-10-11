package de.tu_darmstadt.stg.mudetect;

import de.tu_darmstadt.stg.mudetect.model.AUG;
import de.tu_darmstadt.stg.mudetect.model.Instance;
import de.tu_darmstadt.stg.mudetect.model.Pattern;
import org.junit.Test;

import java.util.List;

import static de.tu_darmstadt.stg.mudetect.model.TestAUGBuilder.buildAUG;
import static de.tu_darmstadt.stg.mudetect.model.TestPatternBuilder.somePattern;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class FindNoInstancesTest {
    @Test
    public void ignoresNonOverlappingTarget() throws Exception {
        Pattern pattern = somePattern(buildAUG().withActionNode("F.foo()").build());
        AUG target = buildAUG().withActionNode("B.bar()").build();

        assertNoInstance(pattern, target);
    }

    @Test
    public void ignoresTrivialOverlap() throws Exception {
        Pattern pattern = somePattern(buildAUG().withDataNode("Object").build());
        AUG target = buildAUG().withDataNode("Object").build();

        assertNoInstance(pattern, target);
    }

    private void assertNoInstance(Pattern pattern, AUG target) {
        List<Instance> instances = new GreedyInstanceFinder().findInstances(target, pattern);

        assertThat(instances, is(empty()));
    }
}
