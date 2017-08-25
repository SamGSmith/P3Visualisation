package p3.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ClusterTest.class, CTrapsTest.class, HiffTest.class,
				HillClimberTest.class,	JumpKTest.class, MaxSatTest.class,
				ModelTest.class, OneMaxTest.class, PopulationTest.class, 
				PyramidTest.class, SolutionTest.class, StepTrackerTest.class,
				p3Test.class })
public class AllTests {

}
