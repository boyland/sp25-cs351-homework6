import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import edu.uwm.cs.random.AbstractRandomTest;
import edu.uwm.cs.random.Command;
import edu.uwm.cs351.LinkedSequence;
import edu.uwm.cs351.test.ArraySequence;
 	
public class RandomTest extends AbstractRandomTest<ArraySequence<String>,LinkedSequence<String>>{

	private static final int MAX_TESTS = 1_000_000;
	private static final int DEFAULT_MAX_TEST_LENGTH = 1_000;
		
	public static final int NUM_TILES = 10;
	private List<String> tiles = new ArrayList<>();
		
	@SuppressWarnings("unchecked")
	private static Class<ArraySequence<String>> refClass = (Class<ArraySequence<String>>)(Class<?>)ArraySequence.class;

	@SuppressWarnings("unchecked")
	protected RandomTest() {
		super(refClass, (Class<LinkedSequence<String>>)(Class<?>)LinkedSequence.class, "LinkedSequence<String>", "bs", MAX_TESTS, DEFAULT_MAX_TEST_LENGTH);
		tiles.add(null);
		for (int i=1; i < NUM_TILES; ++i) {
			tiles.add("S" + i);
		}
	}
	
	protected String randomTile(Random r) {
		return tiles.get(r.nextInt(tiles.size()));
	}
	

	private Command<?> newSequenceCommand = newCommand();
    private Function<Integer,Command<?>> sizeCommand = build(lift(ArraySequence<String>::size), lift(LinkedSequence<String>::size), "size"); 
    private Function<Integer, Command<?>> startCommand = build(lift(ArraySequence<String>::start), lift(LinkedSequence<String>::start), "start");
    private Function<Integer, Command<?>> isCurrentCommand = build(lift(ArraySequence<String>::isCurrent), lift(LinkedSequence<String>::isCurrent), "isCurrent");
    private Function<Integer, Command<?>> getCurrentCommand = build(lift(ArraySequence<String>::getCurrent), lift(LinkedSequence<String>::getCurrent), "getCurrent");
    private Function<Integer, Command<?>> advanceCommand = build(lift(ArraySequence<String>::advance), lift(LinkedSequence<String>::advance), "advance");
    private Function<Integer, Command<?>> removeCurrentCommand = build(lift(ArraySequence<String>::removeCurrent), lift(LinkedSequence<String>::removeCurrent), "removeCurrent");
    private BiFunction<Integer, String, Command<?>> addBeforeCommand = build(lift(ArraySequence<String>::addBefore), lift(LinkedSequence<String>::addBefore), "addBefore");
    private BiFunction<Integer, String, Command<?>> addAfterCommand = build(lift(ArraySequence<String>::addAfter), lift(LinkedSequence<String>::addAfter), "addAfter");
    private BiFunction<Integer, Integer, Command<?>> addAllCommand = build(lift(ArraySequence<String>::addAll), lift(LinkedSequence<String>::addAll), mainClass, "addAll");
    private Function<Integer,Command<?>> cloneCommand = clone(ArraySequence<String>::clone, LinkedSequence<String>::clone, "clone");

    @Override
    protected Command<?> randomCommand(Random r) {
    	int n = mainClass.size();
    	if (n == 0) return newSequenceCommand;
    	int w = r.nextInt(n);

    	switch (r.nextInt(18)) {
    	default:
    	case 0:
    		return newSequenceCommand;
    	case 1:
    		return cloneCommand.apply(w);
    	case 2:
    		return startCommand.apply(w);
    	case 3:
    		return addAllCommand.apply(w, r.nextInt(n+1)-1);
    	case 4:
			return addAfterCommand.apply(w,randomTile(r));
    	case 5:
			return addBeforeCommand.apply(w,randomTile(r));
    	case 6:
    	case 7:
    		return removeCurrentCommand.apply(w);
    	case 8:
    	case 9:
    	case 10:
    		return getCurrentCommand.apply(w);
    	case 11:
    	case 12:
    		return sizeCommand.apply(w);
    	case 13:
    	case 14:
    	case 15:
    		return isCurrentCommand.apply(w);
    	case 16:
    	case 17:
    		return advanceCommand.apply(w);
    	}
    }

	@Override
	public void printImports() {	
		super.printImports();
		System.out.println("import edu.uwm.cs351.LinkedSequence;\n");
	}
		 
	public static void main(String[] args) {
		RandomTest rt = new RandomTest();
		rt.run();
	}

}
